package kz.maks.core.back;

import com.google.common.base.Predicate;
import kz.maks.core.back.annotations.*;
import kz.maks.core.back.annotations.Remote;
import kz.maks.core.back.db.hibernate.Hibernate;
import kz.maks.core.back.jobs.IJob;
import kz.maks.core.back.services.ServiceProxyFactory;
import kz.maks.core.shared.models.ITreeNode;
import org.flywaydb.core.Flyway;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;

import javax.persistence.Entity;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.util.*;

public final class DIServerCore {
    private final Reflections reflections;

    private final Map<Class, List<Object>> beans = new HashMap<>();
    private final Map<Class, Object> containers = new HashMap<>();
    private final Map<Object, Object> beanToProxy = new HashMap<>();
    private final List<Thread> jobThreads = new ArrayList<>();
    private final List<Class> treeEntities = new ArrayList<>();
    private final IServerConfig config;

    public DIServerCore(IServerConfig config) {
        this.config = config;
        reflections = new Reflections(
                new ConfigurationBuilder()
                        .forPackages("kz.maks.core.back", config.basePackage())
                        .addScanners(new FieldAnnotationsScanner(), new SubTypesScanner())
        );
    }

    void init() {
        try {
            migrateDB();

            LocateRegistry.createRegistry(config.rmiRegistryPort());

            initializeHibernate();
            initializeComponents();
            injectBeans();

        } catch (IllegalAccessException | InstantiationException | RemoteException | MalformedURLException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private void migrateDB() {
        Configuration configuration = new Configuration();
        configuration.configure(config.hibernateConfigFileName());

        Flyway flyway = new Flyway();

        flyway.setDataSource(
                configuration.getProperty("connection.url"),
                configuration.getProperty("connection.username"),
                configuration.getProperty("connection.password")
        );

        flyway.migrate();
    }

    void startJobs() {
        for (Thread jobThread : jobThreads) {
            jobThread.start();
        }
    }

    private void addBean(Class clazz, Object bean) {
        List<Object> beans = this.beans.get(clazz);

        if (beans == null) {
            beans = new ArrayList<>();
            this.beans.put(clazz, beans);
        }

        beans.add(bean);

        for (Class<?> interfaceClass : clazz.getInterfaces()) {
            addBean(interfaceClass, bean);
        }
    }

    private void initializeHibernate() throws NoSuchFieldException, IllegalAccessException {
        Configuration configuration = new Configuration();
        configuration.configure(config.hibernateConfigFileName());

        Set<Class<?>> entityClasses = reflections.getTypesAnnotatedWith(Entity.class);

        for (Class<?> entityClass : entityClasses) {
            configuration.addAnnotatedClass(entityClass);

            if (Arrays.asList(entityClass.getInterfaces()).contains(ITreeNode.class)) {
                treeEntities.add(entityClass);
            }

            setTreeEntities(treeEntities.toArray(new Class[] {}));
        }

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        Hibernate.setSessionFactory(configuration.buildSessionFactory(serviceRegistry));
        {
            List list = new ArrayList();
            list.add(Hibernate.getSessionFactory());
            beans.put(SessionFactory.class, list);
        }
    }

    private void setTreeEntities(Class[] treeEntityClasses) throws NoSuchFieldException, IllegalAccessException {
        // TODO possibly need refactoring
        Field field = TreeEntities.class.getDeclaredField("trees");

        boolean wasNotAccessible = !field.isAccessible();

        if (wasNotAccessible) {
            field.setAccessible(true);
        }

        field.set(null, treeEntityClasses);

        if (wasNotAccessible) {
            field.setAccessible(false);
        }
    }

    public <T> T getBean(Class<T> clazz) {
        List<Object> beans = this.beans.get(clazz);

        if (beans == null || beans.size() == 0) {
            throw new RuntimeException("No implementation bean found for type: " + clazz.getName());
        }

        if (beans.size() > 1) {
            throw new RuntimeException("More than one beans found for type: " + clazz.getName());
        }

        return (T) beans.get(0);
    }

    public <T> T getProxy(Class<T> clazz) {
        return (T) beanToProxy.get(getBean(clazz));
    }

    private void bindRemote(Object remote) throws IllegalAccessException, InstantiationException, MalformedURLException, RemoteException {
        if (remote.getClass().getInterfaces().length != 1) {
            throw new RuntimeException("Remote class must implement exactly one interface");
        }
        Naming.rebind("//localhost:" + config.rmiRegistryPort() + "/"
                + remote.getClass().getInterfaces()[0].getSimpleName(), (java.rmi.Remote) remote);
    }

    private void createJob(final Object jobObj) throws IllegalAccessException, InstantiationException {
        if (!IJob.class.isAssignableFrom(jobObj.getClass())) {
            throw new RuntimeException(jobObj.getClass().getName() + " job must implement IJob interface");
        }

        final Job jobMeta = jobObj.getClass().getAnnotation(Job.class);
        final IJob job = (IJob) jobObj;

        Thread jobThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        job.execute();

                        Thread.sleep(jobMeta.interval());

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        jobThreads.add(jobThread);
    }

    /**
     * return types annotated with annotations annotated with given annotation
     * recursively
     */
    private Set<Class<?>> getTypesAnnotatedWithRecursive(Class<? extends Annotation> annotation) {
        Set<Class<?>> beanClasses = reflections.getTypesAnnotatedWith(annotation);
        Set<Class<?>> annotations = new HashSet<>();

        for (Class<?> beanClass : beanClasses) {
            if (beanClass.isAnnotation()) {
                annotations.add(beanClass);
                beanClasses.addAll(getTypesAnnotatedWithRecursive((Class<? extends Annotation>) beanClass));
            }
        }

        beanClasses.removeAll(annotations);

        return beanClasses;
    }

    /**
     * check whether given class or one of its recursive annotations has given annotation
     */
    private Set<Class<? extends Annotation>> getClassAnnotationsRecursive(Class<?> clazz) {
        Set<Class<? extends Annotation>> annotationClasses = new HashSet<>();

        for (Annotation annotation : clazz.getAnnotations()) {
            if (annotation.annotationType() == Target.class
                    || annotation.annotationType() == Retention.class) {
                continue;
            }
            annotationClasses.add(annotation.annotationType());
            annotationClasses.addAll(getClassAnnotationsRecursive(annotation.annotationType()));
        }

        return annotationClasses;
    }

    private void initializeComponents() throws IllegalAccessException, InstantiationException, MalformedURLException, RemoteException {
        Set<Class<?>> componentClasses = getTypesAnnotatedWithRecursive(CoreComponent.class);

        for (Class<?> componentClass : componentClasses) {
            if (componentClass.isAnnotation()) {
                throw new RuntimeException("Unexpected case, something wrong in algorithm");
            }

            if (Modifier.isAbstract(componentClass.getModifiers()) || componentClass.isInterface()) {
                throw new RuntimeException(componentClass.getName() + " bean must be concrete class");
            }

            Object component = componentClass.newInstance();

            containers.put(componentClass, component);

            Set<Class<? extends Annotation>> annotations = getClassAnnotationsRecursive(componentClass);

            if (annotations.contains(Bean.class)) {
                if (annotations.contains(Service.class)) {
                    Object proxy = ServiceProxyFactory.getProxy(component);
                    beanToProxy.put(component, proxy);
                }
                addBean(componentClass, component);
            } else {
                if (annotations.contains(Remote.class)) {
                    bindRemote(component);
                } else if (annotations.contains(Job.class)) {
                    createJob(component);
                } else {
                    throw new RuntimeException("Unexpected case");
                }
            }
        }
    }

    private Set<FieldContainerPair> getFieldsAnnotatedWith(final Class<? extends Annotation> annotationClass) {
        Set<FieldContainerPair> allFields = new HashSet<>();

        for (Class containerClass : containers.keySet()) {
            Set<Field> classFields = ReflectionUtils.getAllFields(containerClass, new Predicate<Field>() {
                @Override
                public boolean apply(Field input) {
                    return input.isAnnotationPresent(annotationClass);
                }
            });

            for (Field field : classFields) {
                allFields.add(new FieldContainerPair(field, containerClass));
            }
        }

        return allFields;
    }

    private class FieldContainerPair {
        public final Field field;
        public final Class containerClass;

        public FieldContainerPair(Field field, Class containerClass) {
            this.field = field;
            this.containerClass = containerClass;
        }
    }

    private void injectBeans() throws IllegalAccessException {
        Set<FieldContainerPair> fields = getFieldsAnnotatedWith(Inject.class);

        for (FieldContainerPair fieldContainerPair : fields) {
            Field field = fieldContainerPair.field;
            Class<?> declaringClass = fieldContainerPair.containerClass;

            Set<Class<? extends Annotation>> declaringClassAnnotations = getClassAnnotationsRecursive(declaringClass);

            if (!declaringClassAnnotations.contains(CoreComponent.class)) {
                throw new RuntimeException(declaringClass.getName() + " must be core component to use injection");
            }

            Object containerBean = containers.get(declaringClass);

            if (containerBean == null) {
                throw new RuntimeException("Unexpected case");
            }

            Object injectedBean = getBean(field.getType());

            if (field.getAnnotation(Inject.class).proxy()) {
                Object proxy = beanToProxy.get(injectedBean);

                if (proxy == null) {
                    throw new RuntimeException("There is no proxy for bean: " + injectedBean.getClass().getName());
                }

                injectedBean = proxy;
            }

            boolean wasNotAccessible = !field.isAccessible();

            if (wasNotAccessible) {
                field.setAccessible(true);
            }

            field.set(containerBean, injectedBean);

            if (wasNotAccessible) {
                field.setAccessible(false);
            }
        }
    }

}

package kz.maks.core.back;

import org.flywaydb.core.Flyway;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.reflections.Reflections;

import javax.persistence.Entity;
import java.util.Set;

public class DiffLauncher {

    public static void launch(String hibernateCfgFileName) {
        Configuration configuration = new Configuration();
        configuration.configure(hibernateCfgFileName);

        configuration.setProperty("hibernate.hbm2ddl.auto", "update");

        cleanAndMigrate(configuration);

        Reflections reflections = new Reflections();
        Set<Class<?>> entityClasses = reflections.getTypesAnnotatedWith(Entity.class);

        for (Class<?> entityClass : entityClasses) {
            configuration.addAnnotatedClass(entityClass);
        }

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        configuration.buildSessionFactory(serviceRegistry);

        System.out.println("END");
    }

    private static void cleanAndMigrate(Configuration configuration) {
        Flyway flyway = new Flyway();

        flyway.setDataSource(
                configuration.getProperty("connection.url"),
                configuration.getProperty("connection.username"),
                configuration.getProperty("connection.password")
        );

        flyway.clean();
        flyway.migrate();
    }

}

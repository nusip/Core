package kz.maks.core.front;

import kz.maks.core.front.services.CoreRemotes;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.lang.reflect.*;
import java.net.MalformedURLException;
import java.rmi.*;

public abstract class BaseClient {

    protected final IClientConfig config;

    private Logger log = Logger.getLogger(BaseClient.class);

    public BaseClient(IClientConfig clientConfig) {
        this.config = clientConfig;
    }

    public final void start() {
        try {
            System.setSecurityManager(new RMISecurityManager());

            lookupRemotes(CoreRemotes.class);
            lookupRemotes(config.remoteStoreClass());

            if (config.useCache()) {
                Cache.cache();
            }

            afterInit();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка!", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    private void lookupRemotes(Class<?> remotesStoreClass) throws IllegalAccessException {
        Field[] fields = remotesStoreClass.getDeclaredFields();

        for (Field field : fields) {
            if (field.getType().isInterface() && Remote.class.isAssignableFrom(field.getType())) {
                Class<? extends Remote> remoteClass = (Class<? extends Remote>) field.getType();

                boolean wasNotAccessible = !field.isAccessible();

                if (wasNotAccessible) {
                    field.setAccessible(true);
                }

                field.set(null, getRemoteProxy(remoteClass));

                if (wasNotAccessible) {
                    field.setAccessible(false);
                }

            } else {
                throw new RuntimeException("Unexpected field type in remotes store: " + field.getType());
            }
        }
    }

    private <T extends Remote> T getRemoteProxy(final Class<T> iface) {
        final Object[] remote = {getRemote(iface)};

        T proxy = (T) Proxy.newProxyInstance(remote[0].getClass().getClassLoader(), remote[0].getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                try {
                    return method.invoke(remote[0], args);

                } catch (InvocationTargetException e) {
                    if (e.getCause() instanceof ConnectException) {
                        remote[0] = getRemote(iface);
                        return null;

                    } else {
                        throw e;
                    }
                }
            }
        });

        return proxy;
    }

    private <T extends Remote> T getRemote(Class<T> iface) {
        try {
            return (T) Naming.lookup("//" + config.rmiRemoteHost() + ":" + config.rmiRemotePort() + "/" + iface.getSimpleName());

        } catch (RemoteException | MalformedURLException | NotBoundException e) {
            log.error(null, e);
            throw new RuntimeException(e);
        }
    }

    public abstract void afterInit();

}

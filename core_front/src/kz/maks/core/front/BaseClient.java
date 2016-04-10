package kz.maks.core.front;

import kz.maks.core.front.services.CoreRemotes;

import javax.swing.*;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.rmi.*;

public abstract class BaseClient {

    protected final IClientConfig config;

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

                field.set(null, getRemote(remoteClass));

                if (wasNotAccessible) {
                    field.setAccessible(false);
                }

            } else {
                throw new RuntimeException("Unexpected field type in remotes store: " + field.getType());
            }
        }
    }

    private <T extends Remote> T getRemote(Class<T> iface) {
        try {
            return (T) Naming.lookup("//" + config.rmiRemoteHost() + ":" + config.rmiRemotePort() + "/" + iface.getSimpleName());

        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void afterInit();

}

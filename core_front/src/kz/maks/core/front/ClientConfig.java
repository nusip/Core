package kz.maks.core.front;

import kz.maks.core.shared.Props;

public class ClientConfig implements IClientConfig {
    private final Class<?> remoteStoreClass;
    private final boolean cacheTrees;

    public ClientConfig(Class<?> remoteStoreClass, boolean cacheTrees) {
        this.remoteStoreClass = remoteStoreClass;
        this.cacheTrees = cacheTrees;
    }

    @Override
    public String rmiRemoteHost() {
        return Props.get("rmi.remote.host");
    }

    @Override
    public Class<?> remoteStoreClass() {
        return remoteStoreClass;
    }

    @Override
    public boolean cacheTrees() {
        return cacheTrees;
    }

    @Override
    public int rmiRemotePort() {
        return Integer.parseInt(Props.get("rmi.remote.port"));
    }
}

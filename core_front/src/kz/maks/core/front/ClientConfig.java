package kz.maks.core.front;

import kz.maks.core.shared.Props;

public class ClientConfig implements IClientConfig {
    private final Class<?> remoteStoreClass;
    private final boolean useCache;

    public ClientConfig(Class<?> remoteStoreClass, boolean useCache) {
        this.remoteStoreClass = remoteStoreClass;
        this.useCache = useCache;
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
    public boolean useCache() {
        return useCache;
    }

    @Override
    public int rmiRemotePort() {
        return Integer.parseInt(Props.get("rmi.remote.port"));
    }
}

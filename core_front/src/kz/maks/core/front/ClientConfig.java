package kz.maks.core.front;

public class ClientConfig implements IClientConfig {
    private final String backendHost;
    private final Class<?> remoteStoreClass;
    private final boolean cacheTrees;
    private final int rmiRegistryPort;

    public ClientConfig(String backendHost, Class<?> remoteStoreClass, boolean cacheTrees, int rmiRegistryPort) {
        this.backendHost = backendHost;
        this.remoteStoreClass = remoteStoreClass;
        this.cacheTrees = cacheTrees;
        this.rmiRegistryPort = rmiRegistryPort;
    }

    @Override
    public String backendHost() {
        return backendHost;
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
    public int rmiRegistryPort() {
        return rmiRegistryPort;
    }
}

package kz.maks.core.front;

public interface IClientConfig {

    String backendHost();

    Class<?> remoteStoreClass();

    boolean cacheTrees();

    int rmiRegistryPort();

}

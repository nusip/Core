package kz.maks.core.front;

public interface IClientConfig {

    String rmiRemoteHost();

    int rmiRemotePort();

    Class<?> remoteStoreClass();

    boolean useCache();

}

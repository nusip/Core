package kz.maks.core.back;

public interface IServerConfig {

    String basePackage();

    String hibernateConfigFileName();

    int rmiRegistryPort();

}

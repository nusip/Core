package kz.maks.core.back;

public class ServerConfig implements IServerConfig {
    private final String basePackage;
    private final String hibernateConfigFileName;
    private final int rmiRegistryPort;

    public ServerConfig(String basePackage, String hibernateConfigFileName, int rmiRegistryPort) {
        this.basePackage = basePackage;
        this.hibernateConfigFileName = hibernateConfigFileName;
        this.rmiRegistryPort = rmiRegistryPort;
    }

    @Override
    public String basePackage() {
        return basePackage;
    }

    @Override
    public String hibernateConfigFileName() {
        return hibernateConfigFileName;
    }

    @Override
    public int rmiRegistryPort() {
        return rmiRegistryPort;
    }

}

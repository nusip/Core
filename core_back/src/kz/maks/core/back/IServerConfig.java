package kz.maks.core.back;

public interface IServerConfig {

    int rmiLocalPort();

    String dbURL();

    String dbUsername();

    String dbPassword();

}

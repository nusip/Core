package kz.maks.core.back;

import kz.maks.core.shared.Props;

public class ServerConfig implements IServerConfig {

    @Override
    public int rmiLocalPort() {
        return Integer.parseInt(Props.get("rmi.local.port"));
    }

    @Override
    public String dbURL() {
        return Props.get("db.url");
    }

    @Override
    public String dbUsername() {
        return Props.get("db.username");
    }

    @Override
    public String dbPassword() {
        return Props.get("db.password");
    }

}

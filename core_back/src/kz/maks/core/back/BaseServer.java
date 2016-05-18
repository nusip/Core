package kz.maks.core.back;

import org.apache.log4j.Logger;

import javax.swing.*;

public abstract class BaseServer {

    protected final DIServerCore diServerCore;

    private Logger log = Logger.getLogger(BaseServer.class);

    public BaseServer(IServerConfig config) {
        diServerCore = new DIServerCore(config);
    }

    public final void start() {
        try {
            diServerCore.init();
            afterInit();
            diServerCore.startJobs();

        } catch (Exception e) {
            log.error(null, e);
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка!", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    public abstract void afterInit();

}

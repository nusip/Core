package kz.maks.core.back;

import javax.swing.*;

public abstract class BaseServer {

    protected final DIServerCore diServerCore;

    public BaseServer(IServerConfig config) {
        diServerCore = new DIServerCore(config);
    }

    public final void start() {
        try {
            diServerCore.init();
            afterInit();
            diServerCore.startJobs();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка!", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    public abstract void afterInit();

}

package kz.maks.core.front.services;

import javax.swing.*;

public abstract class Callback<RESULT> {

    public void beforeCall() {}

    public void onSuccess(RESULT result) {}

    public void onFailure(Throwable t) {
        JOptionPane.showMessageDialog(null, t.getMessage(), "Ошибка!", JOptionPane.ERROR_MESSAGE);
        t.printStackTrace();
    }

    public void atTheEnd() {}

    public static Callback standard() {
        return new Callback() {};
    }

}

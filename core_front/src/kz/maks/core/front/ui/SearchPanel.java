package kz.maks.core.front.ui;

import kz.maks.core.shared.Utils;

import javax.swing.*;

public class SearchPanel<T> {
    public final Form<T> form;
    public final JButton btnSearch;

    public SearchPanel(Class<T> searchParamsClass, Form<T> form, JButton btnSearch) {
        this.form = form;
        this.btnSearch = btnSearch;

        form.set(Utils.newInstance(searchParamsClass));
    }
}

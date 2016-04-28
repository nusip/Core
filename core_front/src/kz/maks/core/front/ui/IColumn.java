package kz.maks.core.front.ui;

import kz.maks.core.shared.models.HasName;
import kz.maks.core.shared.models.HasTitle;

public interface IColumn<T> extends HasName, HasTitle {

    int DEFAULT_WIDTH = -1;

    Class<T> tableClass();

    boolean isEditable();

    int width();

}

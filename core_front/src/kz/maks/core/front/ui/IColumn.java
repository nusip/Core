package kz.maks.core.front.ui;

import kz.maks.core.shared.models.HasName;
import kz.maks.core.shared.models.HasTitle;

public interface IColumn<T> extends HasName, HasTitle {

    Class<T> tableClass();

}

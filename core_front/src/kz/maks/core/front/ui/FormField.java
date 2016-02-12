package kz.maks.core.front.ui;

import kz.maks.core.shared.models.HasName;
import kz.maks.core.shared.models.HasTitle;

public interface FormField<T> extends HasName, HasTitle {

    Class<T> formClass();

}

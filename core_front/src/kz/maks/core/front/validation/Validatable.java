package kz.maks.core.front.validation;

import java.util.List;

public interface Validatable {

    List<String> errorMessages();

    boolean isValid();

}

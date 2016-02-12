package kz.maks.core.back.annotations;

import java.lang.annotation.*;

/**
 * Bean is an injectable core component
 */
@CoreComponent
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Bean {
}

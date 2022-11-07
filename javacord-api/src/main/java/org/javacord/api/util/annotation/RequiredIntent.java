package org.javacord.api.util.annotation;

import org.javacord.api.entity.intent.Intent;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RequiredIntent {
    /**
     * The intent that is required.
     *
     * @return The intent that is required.
     */
    Intent[] value();
}

package org.javacord.api.util.annotation;

import org.javacord.api.entity.intent.Intent;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies the intents that are required for the listeners and methods to work.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RequiredIntent {
    /**
     * The intents that are required.
     *
     * @return The intents that are required.
     */
    Intent[] value();
}

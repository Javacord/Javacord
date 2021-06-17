package org.javacord.api.entity.message.component;

import org.javacord.api.entity.message.component.internal.ComponentBuilderDelegate;

public interface ComponentBuilder {
    /**
     * Get the type of component being built.
     *
     * @return The type of component being built.
     */
    ComponentType getType();

    /**
     * Get the builder delegate.
     *
     * @return The builder delegate.
     */
    ComponentBuilderDelegate getDelegate();
}

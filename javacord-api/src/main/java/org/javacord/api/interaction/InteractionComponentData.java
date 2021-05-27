package org.javacord.api.interaction;

import org.javacord.api.entity.message.component.ComponentType;

public interface InteractionComponentData extends InteractionData {
    /**
     * Get the identifier of the component clicked.
     *
     * @return The component identifier.
     */
    String getCustomId();

    /**
     * Get the type of component.
     *
     * @return The component type.
     */
    ComponentType getType();
}

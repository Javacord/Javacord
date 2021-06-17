package org.javacord.api.interaction;

import org.javacord.api.util.SafeSpecializable;

import java.util.Optional;

public interface MessageComponentInteraction
        extends MessageComponentInteractionBase, SafeSpecializable<InteractionBase> {

    /**
     * Get this interaction as button interaction if the type matches.
     *
     * @return the interaction as button interaction if the type matches; an empty optional otherwise
     */
    default Optional<ButtonInteraction> asButtonInteraction() {
        return as(ButtonInteraction.class);
    }
}
package org.javacord.api.interaction;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.component.ComponentType;
import org.javacord.api.interaction.callback.ComponentInteractionOriginalMessageUpdater;

import java.util.concurrent.CompletableFuture;

public interface MessageComponentInteractionBase extends InteractionBase {
    /**
     * Gets the message that this interaction is related to.
     *
     * @return The message.
     */
    Message getMessage();

    /**
     * Get the identifier of the clicked component.
     *
     * @return The component identifier.
     */
    String getCustomId();

    /**
     * Get the type of the component.
     *
     * @return The component type.
     */
    ComponentType getComponentType();

    /**
     * Acknowledges the incoming component interaction, for example a click on a button. The user will not see a loading
     * state.
     * You can still send follow up messages within a 15 minute timeframe.
     *
     * @return A CompletableFuture that completes if the acknowledgement has been submitted successfully.
     */
    CompletableFuture<Void> acknowledge();

    /**
     * Creates a message updater that can be used to update the message the component of this interaction is attached
     * to.
     *
     * @return The new message updater
     */
    ComponentInteractionOriginalMessageUpdater createOriginalMessageUpdater();
}

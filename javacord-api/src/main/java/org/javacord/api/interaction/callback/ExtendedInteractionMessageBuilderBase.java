package org.javacord.api.interaction.callback;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.internal.MessageBuilderAttachment;
import org.javacord.api.interaction.InteractionBase;

public interface ExtendedInteractionMessageBuilderBase<T extends ExtendedInteractionMessageBuilderBase<T>> extends
        InteractionMessageBuilderBase<T>,
        MessageBuilderAttachment<T> {

    /**
     * Copy a message's values into this build instance.
     *
     * @param message The message to copy.
     * @return The current instance in order to chain call methods.
     */
    T copy(Message message);

    /**
     * Copy an interaction's message.
     *
     * @param interaction The interaction to copy.
     * @return The current instance in order to chain call methods/
     */
    T copy(InteractionBase interaction);

}

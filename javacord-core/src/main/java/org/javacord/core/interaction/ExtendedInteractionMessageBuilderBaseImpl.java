package org.javacord.core.interaction;

import org.javacord.api.entity.message.Message;
import org.javacord.api.interaction.InteractionBase;
import org.javacord.api.interaction.callback.ExtendedInteractionMessageBuilderBase;

@SuppressWarnings("unchecked")
public abstract class ExtendedInteractionMessageBuilderBaseImpl<T extends ExtendedInteractionMessageBuilderBase<T>>
        extends InteractionMessageBuilderBaseImpl<T>
        implements ExtendedInteractionMessageBuilderBase<T> {

    @Override
    public T copy(Message message) {
        delegate.copy(message);
        return (T) this;
    }

    @Override
    public T copy(InteractionBase interaction) {
        delegate.copy(interaction);
        return (T) this;
    }

}

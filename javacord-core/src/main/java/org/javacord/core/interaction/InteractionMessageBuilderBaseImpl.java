package org.javacord.core.interaction;

import org.javacord.api.entity.message.internal.InteractionMessageBuilderDelegate;
import org.javacord.api.interaction.callback.InteractionMessageBuilderBase;
import org.javacord.api.util.internal.DelegateFactory;

public abstract class InteractionMessageBuilderBaseImpl<T extends InteractionMessageBuilderBase<T>> implements InteractionMessageBuilderBase<T> {

    protected final InteractionMessageBuilderDelegate delegate
            = DelegateFactory.createInteractionMessageBuilderDelegate();

    /**
     * Class constructor.
     */
    protected InteractionMessageBuilderBaseImpl() {
    }

    @Override
    public InteractionMessageBuilderDelegate getDelegate() {
        return delegate;
    }
}

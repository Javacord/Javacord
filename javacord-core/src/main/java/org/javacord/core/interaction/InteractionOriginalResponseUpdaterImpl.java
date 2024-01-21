package org.javacord.core.interaction;

import org.javacord.api.entity.message.Message;
import org.javacord.api.interaction.InteractionBase;
import org.javacord.api.interaction.callback.InteractionOriginalResponseUpdater;
import java.util.concurrent.CompletableFuture;

public class InteractionOriginalResponseUpdaterImpl
        extends ExtendedInteractionMessageBuilderBaseImpl<InteractionOriginalResponseUpdater>
        implements InteractionOriginalResponseUpdater {

    private final InteractionImpl interaction;

    /**
     * Class constructor.
     *
     * @param interaction The interaction to use.
     */
    public InteractionOriginalResponseUpdaterImpl(InteractionBase interaction) {
        this.interaction = (InteractionImpl) interaction;
    }

    @Override
    public CompletableFuture<Message> update() {
        return delegate.editOriginalResponse(interaction);
    }

    @Override
    public CompletableFuture<Void> delete() {
        return delegate.deleteInitialResponse(interaction);
    }

    @Override
    public InteractionOriginalResponseUpdater copy(Message message) {
        delegate.copy(message);
        return this;
    }

    @Override
    public InteractionOriginalResponseUpdater copy(InteractionBase interaction) {
        delegate.copy(interaction);
        return this;
    }

}

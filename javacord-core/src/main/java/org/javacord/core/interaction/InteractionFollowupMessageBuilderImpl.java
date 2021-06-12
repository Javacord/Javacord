package org.javacord.core.interaction;

import org.javacord.api.entity.message.Message;
import org.javacord.api.interaction.InteractionBase;
import org.javacord.api.interaction.callback.InteractionFollowupMessageBuilder;

import java.util.concurrent.CompletableFuture;

public class InteractionFollowupMessageBuilderImpl
        extends ExtendedInteractionMessageBuilderBaseImpl<InteractionFollowupMessageBuilder>
        implements InteractionFollowupMessageBuilder {
    private final InteractionImpl interaction;

    /**
     * Class constructor.
     *
     * @param interaction The interaction to use.
     */
    public InteractionFollowupMessageBuilderImpl(InteractionBase interaction) {
        super(InteractionFollowupMessageBuilder.class);
        this.interaction = (InteractionImpl) interaction;
    }

    @Override
    public CompletableFuture<Message> send() {
        return this.delegate.sendFollowupMessage(interaction);
    }

    @Override
    public CompletableFuture<Message> update(long messageId) {
        return update(String.valueOf(messageId));
    }

    @Override
    public CompletableFuture<Message> update(String messageId) {
        return this.delegate.editFollowupMessage(interaction, messageId);
    }
}

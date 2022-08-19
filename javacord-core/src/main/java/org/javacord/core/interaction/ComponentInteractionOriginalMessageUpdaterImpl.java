package org.javacord.core.interaction;

import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.callback.ComponentInteractionOriginalMessageUpdater;

import java.util.concurrent.CompletableFuture;

public class ComponentInteractionOriginalMessageUpdaterImpl
        extends ExtendedInteractionMessageBuilderBaseImpl<ComponentInteractionOriginalMessageUpdater>
        implements ComponentInteractionOriginalMessageUpdater {
    private final MessageComponentInteractionImpl interaction;

    /**
     * Class constructor.
     *
     * @param interaction The interaction to use.
     */
    public ComponentInteractionOriginalMessageUpdaterImpl(MessageComponentInteraction interaction) {
        super(ComponentInteractionOriginalMessageUpdater.class);
        this.interaction = (MessageComponentInteractionImpl) interaction;
    }

    @Override
    public CompletableFuture<Void> update() {
        return this.delegate.updateOriginalMessage(interaction);
    }
}

package org.javacord.core.interaction;

import org.javacord.api.interaction.InteractionBase;
import org.javacord.api.interaction.callback.InteractionImmediateResponseBuilder;
import org.javacord.api.interaction.callback.InteractionOriginalResponseUpdater;

import java.util.concurrent.CompletableFuture;

public class InteractionImmediateResponseBuilderImpl
        extends ExtendedInteractionMessageBuilderBaseImpl<InteractionImmediateResponseBuilder>
        implements InteractionImmediateResponseBuilder {
    private final InteractionImpl interaction;

    /**
     * Class constructor.
     *
     * @param interaction The interaction to use.
     */
    public InteractionImmediateResponseBuilderImpl(InteractionBase interaction) {
        super(InteractionImmediateResponseBuilder.class);
        this.interaction = (InteractionImpl) interaction;
    }

    @Override
    public CompletableFuture<InteractionOriginalResponseUpdater> respond() {
        CompletableFuture<InteractionOriginalResponseUpdater> future = new CompletableFuture<>();

        CompletableFuture<Void> job = delegate.sendInitialResponse(interaction)
                .thenRun(() -> {
                    future.complete(new InteractionOriginalResponseUpdaterImpl(interaction, delegate));
                })
                .exceptionally(e -> {
                    future.completeExceptionally(e);
                    return null;
                });
        return future;
    }
}

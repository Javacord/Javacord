package org.javacord.api.interaction.callback;

import java.util.concurrent.CompletableFuture;

public interface ComponentInteractionOriginalMessageUpdater
        extends ExtendedInteractionMessageBuilderBase<ComponentInteractionOriginalMessageUpdater> {
    /**
     * Updates the message this component is attached to.
     * You must call this method within 3 seconds after receiving the interaction.
     *
     * @return A CompletableFuture that completes as soon as the request has been submitted successfully
     */
    CompletableFuture<Void> update();
}

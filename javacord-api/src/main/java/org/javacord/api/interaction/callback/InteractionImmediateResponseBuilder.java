package org.javacord.api.interaction.callback;

import java.util.concurrent.CompletableFuture;

public interface InteractionImmediateResponseBuilder
        extends InteractionMessageBuilderBase<InteractionImmediateResponseBuilder> {
    /**
     * Send the response.
     *
     * @return A CompletableFuture that completes as soon as your message was sent. The future contains a
     *     {@link InteractionOriginalResponseUpdater} that you can use to update your original response later on.
     *     Note that you can only update your response within 15 minutes after receiving the interaction.
     */
    CompletableFuture<InteractionOriginalResponseUpdater> respond();
}

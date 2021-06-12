package org.javacord.api.interaction.callback;

import org.javacord.api.entity.message.Message;

import java.util.concurrent.CompletableFuture;

public interface InteractionFollowupMessageBuilder
        extends ExtendedInteractionMessageBuilderBase<InteractionFollowupMessageBuilder> {
    /**
     * Sends the follow-up message.
     *
     * @return A CompletableFuture of the new message.
     */
    CompletableFuture<Message> send();

    /**
     * Edits a followup message that has already been sent for this interaction.
     *
     * @param messageId The message id of the followup message which should be edited. It must be a message id of an
     *                  already sent follow up message id of this interaction and can not be any other message id.
     * @return The edited message.
     */
    CompletableFuture<Message> update(long messageId);

    /**
     * Edits a followup message that has already been sent for this interaction.
     *
     * @param messageId The message id of the followup message which should be edited. It must be a message id of an
     *                  already sent follow up message id of this interaction and can not be any other message id.
     * @return The edited message.
     */
    CompletableFuture<Message> update(String messageId);
}

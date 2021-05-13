package org.javacord.api.entity.message.internal;

import org.javacord.api.command.Interaction;
import org.javacord.api.entity.message.Flag;
import org.javacord.api.entity.message.Message;

import java.util.concurrent.CompletableFuture;

public interface InteractionMessageBuilderDelegate extends WebhookMessageBuilderBaseDelegate {

    /**
     * Sets the flag of the message.
     *
     * @param flag The flag of the message.
     */
    void setFlag(Flag flag);

    /**
     * Sends the message.
     *
     * @param interaction The interaction where the message should be sent to.
     * @return The completable future when the message has been sent.
     */
    CompletableFuture<Void> sendInitialResponse(Interaction interaction);

    /**
     * Edits the message.
     *
     * @param interaction The interaction where the message should be sent to.
     * @return The sent message.
     */
    CompletableFuture<Message> editOriginalResponse(Interaction interaction);

    /**
     * Sends the message as a followup message.
     *
     * @param interaction The interaction where the message should be sent to.
     * @return The sent message.
     */
    CompletableFuture<Message> sendFollowupMessage(Interaction interaction);

    /**
     * Edits the message.
     *
     * @param interaction The interaction where the message should be sent to.
     * @param messageId The message id of the followup message which should be edited.
     * @return The sent message.
     */
    CompletableFuture<Message> editFollowupMessage(Interaction interaction, String messageId);

}

package org.javacord.api.entity.message.internal;

import org.javacord.api.command.Interaction;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageFlag;

import java.util.EnumSet;
import java.util.concurrent.CompletableFuture;

public interface InteractionMessageBuilderDelegate extends WebhookMessageBuilderBaseDelegate {

    /**
     * Sets the message flags of the message.
     *
     * @param messageFlags The message flag of the message.
     */
    void setFlags(EnumSet<MessageFlag> messageFlags);

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

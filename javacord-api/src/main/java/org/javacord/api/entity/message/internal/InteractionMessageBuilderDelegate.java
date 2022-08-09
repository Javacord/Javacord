package org.javacord.api.entity.message.internal;

import org.javacord.api.entity.Attachment;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.interaction.InteractionBase;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface InteractionMessageBuilderDelegate extends MessageBuilderBaseDelegate {

    /**
     * Sets the interaction message flags of the message.
     *
     * @param messageFlags The message flags of the message.
     */
    void setFlags(EnumSet<MessageFlag> messageFlags);

    /**
     * Sends the message.
     *
     * @param interaction The interaction.
     * @return The completable future when the message has been sent.
     */
    CompletableFuture<Void> sendInitialResponse(InteractionBase interaction);

    /**
     * Delete the original response message.
     *
     * @param interaction The interaction.
     * @return The completable future when the message has been deleted.
     */
    CompletableFuture<Void> deleteInitialResponse(InteractionBase interaction);

    /**
     * Edits the message.
     *
     * @param interaction The interaction.
     * @return The sent message.
     */
    CompletableFuture<Message> editOriginalResponse(InteractionBase interaction);

    /**
     * Sends the message as a followup message.
     *
     * @param interaction The interaction.
     * @return The message that was sent.
     */
    CompletableFuture<Message> sendFollowupMessage(InteractionBase interaction);

    /**
     * Edit the message the component was attached to.
     *
     * @param interaction The interaction.
     * @return The completable future to determine if the message was updated.
     */
    //TODO: implement this
    CompletableFuture<Void> updateOriginalMessage(InteractionBase interaction);

    /**
     * Edit the message the component was attached to.
     *
     * @param interaction The interaction.
     * @param attachmentsToKeep used to keep specific attachments, and remove the others.
     * @param addNewAttachments used to add new attachments.
     * @return The completable future to determine if the message was updated.
     */
    CompletableFuture<Void> updateOriginalMessage(InteractionBase interaction, List<Attachment> attachmentsToKeep,
                                                  List<Attachment> addNewAttachments);

    /**
     * Edit the message the component was attached to.
     *
     * @param interaction The interaction.
     * @param keepAttachments Used to tell discord if you want to keep
     *                        the attachments in the message.
     * @return The completable future to determine if the message was updated.
     */
    //TODO: implement this method
    CompletableFuture<Void> updateOriginalMessage(InteractionBase interaction, boolean keepAttachments);

    /**
     * Delete a follow-up message.
     *
     * @param interaction The interaction.
     * @param messageId   The message id of the followup message which should be edited.
     * @return The message that was sent.
     */
    CompletableFuture<Void> deleteFollowupMessage(InteractionBase interaction, String messageId);

    /**
     * Edits the message.
     *
     * @param interaction The interaction.
     * @param messageId   The message id of the followup message which should be edited.
     * @return The message that was sent.
     */
    CompletableFuture<Message> editFollowupMessage(InteractionBase interaction, String messageId);

    /**
     * Fill the builder's values with the message from a given interaction,
     *     if the type of the interaction offers a message.
     *
     * @param interaction The Interaction to copy from.
     */
    void copy(InteractionBase interaction);
}

package org.javacord.api.interaction.callback;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.message.internal.InteractionMessageBuilderDelegate;
import org.javacord.api.entity.message.internal.MessageBuilderBaseDelegate;
import org.javacord.api.interaction.InteractionBase;
import org.javacord.api.util.internal.DelegateFactory;
import java.io.File;
import java.util.concurrent.CompletableFuture;

/**
 * This class is intended to be used by advanced users that desire full control over interaction responses.
 * We strongly recommend using the offered methods on your received interactions instead of this class.
 */
public class InteractionMessageBuilder implements ExtendedInteractionMessageBuilderBase<InteractionMessageBuilder> {

    protected final InteractionMessageBuilderDelegate delegate =
            DelegateFactory.createInteractionMessageBuilderDelegate();

    /**
     * Sends the first response message.
     * This can only be done once after you want to respond to an interaction the FIRST time.
     * Responding directly to an interaction limits you to not being able to upload anything.
     * Therefore, i.e. {@link EmbedBuilder#setFooter(String, File)} will not work, and you have to use the
     * String methods for attachments like {@link EmbedBuilder#setFooter(String, String)} if available.
     * If you want to upload attachments use {@link #editOriginalResponse(InteractionBase)} instead.
     *
     * @param interaction The interaction.
     * @return The CompletableFuture when your message was sent.
     */
    public CompletableFuture<InteractionMessageBuilder> sendInitialResponse(InteractionBase interaction) {
        CompletableFuture<InteractionMessageBuilder> future = new CompletableFuture<>();
        delegate.sendInitialResponse(interaction)
                .thenRun(() -> future.complete(this))
                .exceptionally(e -> {
                    future.completeExceptionally(e);
                    return null;
                });
        return future;
    }

    /**
     * Edits your original sent response.
     * Your original response may be sent by {@link #sendInitialResponse(InteractionBase)}
     * or by deciding to respond through {@link InteractionBase#respondLater()} which sends a "loading state"
     * as your first response.
     * In comparison to {@link #sendInitialResponse(InteractionBase)} this method allows you to upload attachments
     * with your message.
     *
     * @param interaction The interaction.
     * @return The edited message.
     */
    public CompletableFuture<Message> editOriginalResponse(InteractionBase interaction) {
        return delegate.editOriginalResponse(interaction);
    }

    /**
     * Sends a followup message to an interaction.
     *
     * @param interaction The interaction.
     * @return The sent message.
     */
    public CompletableFuture<Message> sendFollowupMessage(InteractionBase interaction) {
        return delegate.sendFollowupMessage(interaction);
    }

    /**
     * Edits a followup message from an interaction.
     *
     * @param interaction The interaction.
     * @param messageId   The message id of the followup message which should be edited.
     * @return The edited message.
     */
    public CompletableFuture<Message> editFollowupMessage(InteractionBase interaction, long messageId) {
        return editFollowupMessage(interaction, Long.toUnsignedString(messageId));
    }

    /**
     * Edits a followup message from an interaction.
     *
     * @param interaction The interaction.
     * @param messageId   The message id of the followup message which should be edited.
     * @return The edited message.
     */
    public CompletableFuture<Message> editFollowupMessage(InteractionBase interaction, String messageId) {
        return delegate.editFollowupMessage(interaction, messageId);
    }

    /**
     * Update the message the components were attached to.
     *
     * @param interaction The original interaction.
     * @return The completable future to determine if the message was updated.
     */
    public CompletableFuture<Void> updateOriginalMessage(InteractionBase interaction) {
        return delegate.updateOriginalMessage(interaction);
    }

    /**
     * Delete the original response.
     *
     * @param interaction The interaction.
     * @return The completable future when the message has been deleted.
     */
    public CompletableFuture<Void> deleteInitialResponse(InteractionBase interaction) {
        return delegate.deleteInitialResponse(interaction);
    }

    /**
     * Delete a followup message from an interaction.
     *
     * @param interaction The interaction.
     * @param messageId   The message id of the followup message which should be deleted.
     * @return The deleted message.
     */
    public CompletableFuture<Void> deleteFollowupMessage(InteractionBase interaction, String messageId) {
        return delegate.deleteFollowupMessage(interaction, messageId);
    }

    @Override
    public MessageBuilderBaseDelegate getDelegate() {
        return delegate;
    }

    @Override
    public InteractionMessageBuilder copy(Message message) {
        delegate.copy(message);
        return this;
    }

    @Override
    public InteractionMessageBuilder copy(InteractionBase interaction) {
        delegate.copy(interaction);
        return this;
    }

}
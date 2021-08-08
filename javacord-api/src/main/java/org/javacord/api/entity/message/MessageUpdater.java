package org.javacord.api.entity.message;

import java.util.concurrent.CompletableFuture;

public class MessageUpdater extends MessageBuilderBase<MessageUpdater> {
    /**
     * The message to update.
     */
    private final Message message;

    /**
     * Class constructor.
     *
     * @param m The message to update.
     */
    public MessageUpdater(Message m) {
        super(MessageUpdater.class);
        this.message = m;
    }

    /**
     * Edits the message, updating all fields that have been changed by a method call.
     * For example, the content of the message will only be patched if you called a method on this updater
     * that affects the content.
     *
     * @return The edited message.
     */
    public CompletableFuture<Message> applyChanges() {
        return delegate.edit(message, false);
    }

    /**
     * Edits the message, updating all fields.
     * Fields that have not been set will be dropped and removed from the actual message on Discord.
     *
     * @return The edited message.
     */
    public CompletableFuture<Message> replaceMessage() {
        return delegate.edit(message, true);
    }
}

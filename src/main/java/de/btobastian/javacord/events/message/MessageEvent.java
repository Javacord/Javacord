package de.btobastian.javacord.events.message;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.channels.TextChannel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import de.btobastian.javacord.entities.message.emoji.Emoji;
import de.btobastian.javacord.events.Event;

import java.util.concurrent.CompletableFuture;

/**
 * A message event.
 */
public abstract class MessageEvent extends Event {

    /**
     * The id of the message.
     */
    private final long messageId;

    /**
     * The text channel in which the message was sent.
     */
    private final TextChannel channel;

    /**
     * Creates a new message event.
     *
     * @param api The discord api instance.
     * @param messageId The id of the message.
     * @param channel The text channel in which the message was sent.
     */
    public MessageEvent(DiscordApi api, long messageId, TextChannel channel) {
        super(api);
        this.messageId = messageId;
        this.channel = channel;
    }

    /**
     * Gets the id of the message.
     *
     * @return The id of the message.
     */
    public long getMessageId() {
        return messageId;
    }

    /**
     * Gets the channel in which the message was sent.
     *
     * @return The channel in which the message was sent.
     */
    public TextChannel getChannel() {
        return channel;
    }

    /**
     * Deletes the message involved in the event.
     *
     * @return A future to tell us if the deletion was successful.
     */
    public CompletableFuture<Void> deleteMessage() {
        return Message.delete(getApi(), getChannel().getId(), getMessageId());
    }

    /**
     * Updates the content of the message involved in the event.
     *
     * @param content The new content of the message.
     * @return A future to check if the update was successful.
     */
    public CompletableFuture<Void> editMessage(String content) {
        return Message.edit(getApi(), getChannel().getId(), getMessageId(), content, null);
    }

    /**
     * Updates the embed of the message involved in the event.
     *
     * @param embed The new embed of the message.
     * @return A future to check if the update was successful.
     */
    public CompletableFuture<Void> editMessage(EmbedBuilder embed) {
        return Message.edit(getApi(), getChannel().getId(), getMessageId(), null, embed);
    }

    /**
     * Updates the content and the embed of the message involved in the event.
     *
     * @param content The new content of the message.
     * @param embed The new embed of the message.
     * @return A future to check if the update was successful.
     */
    public CompletableFuture<Void> editMessage(String content, EmbedBuilder embed) {
        return Message.edit(getApi(), getChannel().getId(), getMessageId(), content, embed);
    }

    /**
     * Adds a unicode reaction to the message involved in the event.
     *
     * @param unicodeEmoji The unicode emoji string.
     * @return A future to tell us if the action was successful.
     */
    public CompletableFuture<Void> addReactionToMessage(String unicodeEmoji) {
        return Message.addReaction(getApi(), getChannel().getId(), getMessageId(), unicodeEmoji);
    }

    /**
     * Adds a reaction to the message involved in the event.
     *
     * @param emoji The emoji.
     * @return A future to tell us if the action was successful.
     */
    public CompletableFuture<Void> addReactionToMessage(Emoji emoji) {
        return Message.addReaction(getApi(), getChannel().getId(), getMessageId(), emoji);
    }

    /**
     * Deletes all reactions on the message involved in the event..
     *
     * @return A future to tell us if the deletion was successful.
     */
    public CompletableFuture<Void> removeAllReactionsFromMessage() {
        return Message.removeAllReactions(getApi(), getChannel().getId(), getMessageId());
    }

}

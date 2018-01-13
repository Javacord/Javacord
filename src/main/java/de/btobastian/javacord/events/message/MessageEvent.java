package de.btobastian.javacord.events.message;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.ServerChannel;
import de.btobastian.javacord.entities.channels.TextChannel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.Reaction;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import de.btobastian.javacord.entities.message.emoji.Emoji;
import de.btobastian.javacord.entities.message.emoji.impl.ImplUnicodeEmoji;
import de.btobastian.javacord.events.Event;

import java.util.Arrays;
import java.util.Optional;
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
     * Gets the server of the message.
     *
     * @return The server of the message.
     */
    public Optional<Server> getServer() {
        return getChannel().asServerChannel().map(ServerChannel::getServer);
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
     * Deletes all reactions on the message involved in the event.
     *
     * @return A future to tell us if the deletion was successful.
     */
    public CompletableFuture<Void> removeAllReactionsFromMessage() {
        return Message.removeAllReactions(getApi(), getChannel().getId(), getMessageId());
    }

    /**
     * Removes a user from the list of reactors of a given emoji reaction from the message.
     *
     * @param user The user to remove.
     * @param emoji The emoji of the reaction.
     * @return A future to tell us if the deletion was successful.
     */
    public CompletableFuture<Void> removeReactionByEmojiFromMessage(User user, Emoji emoji) {
        return Reaction.removeUser(getApi(), getChannel().getId(), getMessageId(), emoji, user);
    }

    /**
     * Removes a user from the list of reactors of the given emoji reactions from the message.
     *
     * @param user The user to remove.
     * @param emojis The emojis of the reactions.
     * @return A future to tell us if the deletion was successful.
     */
    public CompletableFuture<Void> removeReactionsByEmojiFromMessage(User user, Emoji... emojis) {
        return CompletableFuture.allOf(
                Arrays.stream(emojis)
                        .map(emoji -> removeReactionByEmojiFromMessage(user, emoji))
                        .toArray(CompletableFuture[]::new));
    }

    /**
     * Removes a user from the list of reactors of a given unicode emoji reaction from the message.
     *
     * @param user The user to remove.
     * @param unicodeEmoji The unicode emoji of the reaction.
     * @return A future to tell us if the deletion was successful.
     */
    public CompletableFuture<Void> removeReactionByEmojiFromMessage(User user, String unicodeEmoji) {
        return removeReactionByEmojiFromMessage(user, ImplUnicodeEmoji.fromString(unicodeEmoji));
    }

    /**
     * Removes a user from the list of reactors of the given unicode emoji reactions from the message.
     *
     * @param unicodeEmojis The unicode emojis of the reactions.
     * @param user The user to remove.
     * @return A future to tell us if the deletion was successful.
     */
    public CompletableFuture<Void> removeReactionsByEmojiFromMessage(User user, String... unicodeEmojis) {
        return removeReactionsByEmojiFromMessage(user, Arrays.stream(unicodeEmojis).map(ImplUnicodeEmoji::fromString).toArray(Emoji[]::new));
    }

    /**
     * Removes all reactors of a given emoji reaction from the message.
     *
     * @param emoji The emoji of the reaction.
     * @return A future to tell us if the deletion was successful.
     */
    public CompletableFuture<Void> removeReactionByEmojiFromMessage(Emoji emoji) {
        return Reaction.getUsers(getApi(), getChannel().getId(), getMessageId(), emoji)
                .thenCompose(users -> CompletableFuture.allOf(
                        users.stream()
                                .map(user -> Reaction.removeUser(getApi(), getChannel().getId(), getMessageId(), emoji, user))
                                .toArray(CompletableFuture[]::new)));
    }

    /**
     * Removes all reactors of the given emoji reactions from the message.
     *
     * @param emojis The emojis of the reactions.
     * @return A future to tell us if the deletion was successful.
     */
    public CompletableFuture<Void> removeReactionsByEmojiFromMessage(Emoji... emojis) {
        return CompletableFuture.allOf(
                Arrays.stream(emojis)
                        .map(this::removeReactionByEmojiFromMessage)
                        .toArray(CompletableFuture[]::new));
    }

    /**
     * Removes all reactors of a given unicode emoji reaction from the message.
     *
     * @param unicodeEmoji The unicode emoji of the reaction.
     * @return A future to tell us if the deletion was successful.
     */
    public CompletableFuture<Void> removeReactionByEmojiFromMessage(String unicodeEmoji) {
        return removeReactionByEmojiFromMessage(ImplUnicodeEmoji.fromString(unicodeEmoji));
    }

    /**
     * Removes all reactors of the given unicode emoji reactions from the message.
     *
     * @param unicodeEmojis The unicode emojis of the reactions.
     * @return A future to tell us if the deletion was successful.
     */
    public CompletableFuture<Void> removeReactionsByEmojiFromMessage(String... unicodeEmojis) {
        return removeReactionsByEmojiFromMessage(Arrays.stream(unicodeEmojis).map(ImplUnicodeEmoji::fromString).toArray(Emoji[]::new));
    }

    /**
     * Removes you from the list of reactors of a given emoji reaction from the message.
     *
     * @param emoji The emoji of the reaction.
     * @return A future to tell us if the deletion was successful.
     */
    public CompletableFuture<Void> removeOwnReactionByEmojiFromMessage(Emoji emoji) {
        return removeReactionByEmojiFromMessage(getApi().getYourself(), emoji);
    }

    /**
     * Removes you from the list of reactors of the given emoji reactions from the message.
     *
     * @param emojis The emojis of the reactions.
     * @return A future to tell us if the deletion was successful.
     */
    public CompletableFuture<Void> removeOwnReactionsByEmojiFromMessage(Emoji... emojis) {
        return removeReactionsByEmojiFromMessage(getApi().getYourself(), emojis);
    }

    /**
     * Removes you from the list of reactors of a given unicode emoji reaction from the message.
     *
     * @param unicodeEmoji The unicode emoji of the reaction.
     * @return A future to tell us if the deletion was successful.
     */
    public CompletableFuture<Void> removeOwnReactionByEmojiFromMessage(String unicodeEmoji) {
        return removeOwnReactionByEmojiFromMessage(ImplUnicodeEmoji.fromString(unicodeEmoji));
    }

    /**
     * Removes you from the list of reactors of the given unicode emoji reactions from the message.
     *
     * @param unicodeEmojis The unicode emojis of the reactions.
     * @return A future to tell us if the deletion was successful.
     */
    public CompletableFuture<Void> removeOwnReactionsByEmojiFromMessage(String... unicodeEmojis) {
        return removeOwnReactionsByEmojiFromMessage(Arrays.stream(unicodeEmojis).map(ImplUnicodeEmoji::fromString).toArray(Emoji[]::new));
    }

    /**
     * Pins the message involved in the event.
     *
     * @return A future to tell us if the pin was successful.
     */
    public CompletableFuture<Void> pinMessage() {
        return Message.pin(getApi(), getChannel().getId(), getMessageId());
    }

    /**
     * Unpins the message involved in the event.
     *
     * @return A future to tell us if the action was successful.
     */
    public CompletableFuture<Void> unpinMessage() {
        return Message.unpin(getApi(), getChannel().getId(), getMessageId());
    }

}

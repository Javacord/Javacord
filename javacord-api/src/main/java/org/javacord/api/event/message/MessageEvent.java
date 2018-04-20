package org.javacord.api.event.message;

import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.channel.TextChannelEvent;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * A message event.
 */
public interface MessageEvent extends TextChannelEvent {

    /**
     * Gets the id of the message.
     *
     * @return The id of the message.
     */
    long getMessageId();

    /**
     * Gets the server of the message.
     *
     * @return The server of the message.
     */
    Optional<Server> getServer();

    /**
     * Deletes the message involved in the event.
     *
     * @return A future to tell us if the deletion was successful.
     */
    CompletableFuture<Void> deleteMessage();

    /**
     * Deletes the message involved in the event.
     *
     * @param reason The audit log reason for the deletion.
     * @return A future to tell us if the deletion was successful.
     */
    CompletableFuture<Void> deleteMessage(String reason);

    /**
     * Updates the content of the message involved in the event.
     *
     * @param content The new content of the message.
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> editMessage(String content);

    /**
     * Updates the embed of the message involved in the event.
     *
     * @param embed The new embed of the message.
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> editMessage(EmbedBuilder embed);

    /**
     * Updates the content and the embed of the message involved in the event.
     *
     * @param content The new content of the message.
     * @param embed The new embed of the message.
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> editMessage(String content, EmbedBuilder embed);

    /**
     * Adds a unicode reaction to the message involved in the event.
     *
     * @param unicodeEmoji The unicode emoji string.
     * @return A future to tell us if the action was successful.
     */
    CompletableFuture<Void> addReactionToMessage(String unicodeEmoji);

    /**
     * Adds a reaction to the message involved in the event.
     *
     * @param emoji The emoji.
     * @return A future to tell us if the action was successful.
     */
    CompletableFuture<Void> addReactionToMessage(Emoji emoji);

    /**
     * Adds reactions to the message involved in the event.
     *
     * @param emojis The emojis.
     * @return A future to tell us if the action was successful.
     */
    CompletableFuture<Void> addReactionsToMessage(Emoji... emojis);

    /**
     * Adds unicode reactions to the message involved in the event.
     *
     * @param unicodeEmojis The unicode emoji strings.
     * @return A future to tell us if the action was successful.
     */
    CompletableFuture<Void> addReactionsToMessage(String... unicodeEmojis);

    /**
     * Deletes all reactions on the message involved in the event.
     *
     * @return A future to tell us if the deletion was successful.
     */
    CompletableFuture<Void> removeAllReactionsFromMessage();

    /**
     * Removes a user from the list of reactors of a given emoji reaction from the message.
     *
     * @param user The user to remove.
     * @param emoji The emoji of the reaction.
     * @return A future to tell us if the deletion was successful.
     */
    CompletableFuture<Void> removeReactionByEmojiFromMessage(User user, Emoji emoji);

    /**
     * Removes a user from the list of reactors of a given unicode emoji reaction from the message.
     *
     * @param user The user to remove.
     * @param unicodeEmoji The unicode emoji of the reaction.
     * @return A future to tell us if the deletion was successful.
     */
    CompletableFuture<Void> removeReactionByEmojiFromMessage(User user, String unicodeEmoji);

    /**
     * Removes all reactors of a given emoji reaction from the message.
     *
     * @param emoji The emoji of the reaction.
     * @return A future to tell us if the deletion was successful.
     */
    CompletableFuture<Void> removeReactionByEmojiFromMessage(Emoji emoji);

    /**
     * Removes all reactors of a given unicode emoji reaction from the message.
     *
     * @param unicodeEmoji The unicode emoji of the reaction.
     * @return A future to tell us if the deletion was successful.
     */
    CompletableFuture<Void> removeReactionByEmojiFromMessage(String unicodeEmoji);

    /**
     * Removes a user from the list of reactors of the given emoji reactions from the message.
     *
     * @param user The user to remove.
     * @param emojis The emojis of the reactions.
     * @return A future to tell us if the deletion was successful.
     */
    CompletableFuture<Void> removeReactionsByEmojiFromMessage(User user, Emoji... emojis);

    /**
     * Removes a user from the list of reactors of the given unicode emoji reactions from the message.
     *
     * @param unicodeEmojis The unicode emojis of the reactions.
     * @param user The user to remove.
     * @return A future to tell us if the deletion was successful.
     */
    CompletableFuture<Void> removeReactionsByEmojiFromMessage(User user, String... unicodeEmojis);

    /**
     * Removes all reactors of the given emoji reactions from the message.
     *
     * @param emojis The emojis of the reactions.
     * @return A future to tell us if the deletion was successful.
     */
    CompletableFuture<Void> removeReactionsByEmojiFromMessage(Emoji... emojis);

    /**
     * Removes all reactors of the given unicode emoji reactions from the message.
     *
     * @param unicodeEmojis The unicode emojis of the reactions.
     * @return A future to tell us if the deletion was successful.
     */
    CompletableFuture<Void> removeReactionsByEmojiFromMessage(String... unicodeEmojis);

    /**
     * Removes you from the list of reactors of a given emoji reaction from the message.
     *
     * @param emoji The emoji of the reaction.
     * @return A future to tell us if the deletion was successful.
     */
    CompletableFuture<Void> removeOwnReactionByEmojiFromMessage(Emoji emoji);

    /**
     * Removes you from the list of reactors of a given unicode emoji reaction from the message.
     *
     * @param unicodeEmoji The unicode emoji of the reaction.
     * @return A future to tell us if the deletion was successful.
     */
    CompletableFuture<Void> removeOwnReactionByEmojiFromMessage(String unicodeEmoji);

    /**
     * Removes you from the list of reactors of the given emoji reactions from the message.
     *
     * @param emojis The emojis of the reactions.
     * @return A future to tell us if the deletion was successful.
     */
    CompletableFuture<Void> removeOwnReactionsByEmojiFromMessage(Emoji... emojis);

    /**
     * Removes you from the list of reactors of the given unicode emoji reactions from the message.
     *
     * @param unicodeEmojis The unicode emojis of the reactions.
     * @return A future to tell us if the deletion was successful.
     */
    CompletableFuture<Void> removeOwnReactionsByEmojiFromMessage(String... unicodeEmojis);

    /**
     * Pins the message involved in the event.
     *
     * @return A future to tell us if the pin was successful.
     */
    CompletableFuture<Void> pinMessage();

    /**
     * Unpins the message involved in the event.
     *
     * @return A future to tell us if the action was successful.
     */
    CompletableFuture<Void> unpinMessage();

}

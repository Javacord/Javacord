package org.javacord.api.entity.message;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.user.User;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a reaction.
 */
public interface Reaction {

    /**
     * Gets a list with all users who used this reaction.
     *
     * @param api The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param emoji The emoji of the reaction.
     * @return A list with all users who used this reaction.
     */
    static CompletableFuture<List<User>> getUsers(DiscordApi api, long channelId, long messageId, Emoji emoji) {
        return api.getUncachedMessageUtil().getUsersWhoReactedWithEmoji(channelId, messageId, emoji);
    }

    /**
     * Gets a list with all users who used this reaction.
     *
     * @param api The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param emoji The emoji of the reaction.
     * @return A list with all users who used this reaction.
     */
    static CompletableFuture<List<User>> getUsers(DiscordApi api, String channelId, String messageId, Emoji emoji) {
        return api.getUncachedMessageUtil().getUsersWhoReactedWithEmoji(channelId, messageId, emoji);
    }

    /**
     * Gets a list with all users who used this reaction.
     *
     * @return A list with all users who used this reaction.
     */
    default CompletableFuture<List<User>> getUsers() {
        return Reaction.getUsers(
                getMessage().getApi(), getMessage().getChannel().getId(), getMessage().getId(), getEmoji());
    }

    /**
     * Removes a user from the list of reactors.
     *
     * @param api The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param emoji The emoji of the reaction.
     * @param userId The if of the user to remove.
     * @return A future to tell us if the action was successful.
     */
    static CompletableFuture<Void> removeUser(
            DiscordApi api, long channelId, long messageId, Emoji emoji, long userId) {
        return api.getUncachedMessageUtil().removeUserReactionByEmoji(channelId, messageId, emoji, userId);
    }

    /**
     * Removes a user from the list of reactors.
     *
     * @param api The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param emoji The emoji of the reaction.
     * @param userId The if of the user to remove.
     * @return A future to tell us if the action was successful.
     */
    static CompletableFuture<Void> removeUser(DiscordApi api, String channelId, String messageId, Emoji emoji,
                                              String userId) {
        return api.getUncachedMessageUtil().removeUserReactionByEmoji(channelId, messageId, emoji, userId);
    }

    /**
     * Removes a user from the list of reactors.
     *
     * @param user The user to remove.
     * @return A future to tell us if the action was successful.
     */
    default CompletableFuture<Void> removeUser(User user) {
        return Reaction.removeUser(getMessage().getApi(), getMessage().getChannel().getId(), getMessage().getId(),
                getEmoji(), user.getId());
    }

    /**
     * Gets the message, the reaction belongs to.
     *
     * @return The message, the reaction belongs to.
     */
    Message getMessage();

    /**
     * Gets the emoji of the reaction.
     *
     * @return The emoji of the reaction.
     */
    Emoji getEmoji();

    /**
     * Gets the amount of users who used this reaction.
     *
     * @return The amount of users who used this reaction.
     */
    int getCount();

    /**
     * Checks if you (the current account) used this reaction.
     *
     * @return Whether this reaction is used by you or not.
     */
    boolean containsYou();

    /**
     * Removes the user of the connected account from the list of reactors.
     *
     * @return A future to tell us if the action was successful.
     */
    default CompletableFuture<Void> removeYourself() {
        return removeUser(getMessage().getApi().getYourself());
    }

    /**
     * Removes all reactors.
     *
     * @return A future to tell us if the action was successful.
     */
    default CompletableFuture<Void> remove() {
        return getUsers()
                .thenCompose(users -> CompletableFuture.allOf(
                        users.stream()
                                .map(this::removeUser)
                                .toArray(CompletableFuture[]::new)));
    }
}

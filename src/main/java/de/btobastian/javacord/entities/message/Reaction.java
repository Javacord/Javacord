package de.btobastian.javacord.entities.message;

import com.mashape.unirest.http.HttpMethod;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.emoji.Emoji;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestRequest;
import org.json.JSONArray;

import java.util.ArrayList;
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
        String value = emoji.asUnicodeEmoji().orElse(
                emoji.asCustomEmoji().map(e -> e.getName() + ":" + String.valueOf(e.getId())).orElse("UNKNOWN"));
        return new RestRequest<List<User>>(api, HttpMethod.GET, RestEndpoint.REACTION)
                .setUrlParameters(
                        String.valueOf(channelId), String.valueOf(messageId), value)
                .setRatelimitRetries(250)
                .execute(res -> {
                    List<User> users = new ArrayList<>();
                    JSONArray usersJson = res.getBody().getArray();
                    for (int i = 0; i < usersJson.length(); i++) {
                        users.add(((ImplDiscordApi) api).getOrCreateUser(usersJson.getJSONObject(i)));
                    }
                    return users;
                });
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
        try {
            return getUsers(api, Long.parseLong(channelId), Long.parseLong(messageId), emoji);
        } catch (NumberFormatException e) {
            CompletableFuture<List<User>> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    /**
     * Removes a user from the list of reactors.
     *
     * @param api The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param emoji The emoji of the reaction.
     * @param user The user to remove.
     * @return A future to tell us if the action was successful.
     */
    static CompletableFuture<Void> removeUser(DiscordApi api, long channelId, long messageId, Emoji emoji, User user) {
        String value = emoji.asUnicodeEmoji().orElse(
                emoji.asCustomEmoji().map(e -> e.getName() + ":" + String.valueOf(e.getId())).orElse("UNKNOWN"));
        return new RestRequest<Void>(api, HttpMethod.DELETE, RestEndpoint.REACTION)
                .setUrlParameters(
                        String.valueOf(channelId),
                        String.valueOf(messageId),
                        value,
                        user.isYourself() ? "@me" : String.valueOf(user.getId()))
                .setRatelimitRetries(250)
                .execute(res -> null);
    }

    /**
     * Removes a user from the list of reactors.
     *
     * @param api The discord api instance.
     * @param channelId The id of the message's channel.
     * @param messageId The id of the message.
     * @param emoji The emoji of the reaction.
     * @param user The user to remove.
     * @return A future to tell us if the action was successful.
     */
    static CompletableFuture<Void> removeUser(DiscordApi api, String channelId, String messageId, Emoji emoji, User user) {
        try {
            return removeUser(api, Long.parseLong(channelId), Long.parseLong(messageId), emoji, user);
        } catch (NumberFormatException e) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
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
     * @param user The user to remove.
     * @return A future to tell us if the action was successful.
     */
    default CompletableFuture<Void> removeUser(User user) {
        return Reaction.removeUser(
                getMessage().getApi(), getMessage().getChannel().getId(), getMessage().getId(), getEmoji(), user);
    }

}

package org.javacord.entity.message;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.DiscordApi;
import org.javacord.ImplDiscordApi;
import org.javacord.entity.emoji.Emoji;
import org.javacord.entity.user.User;
import org.javacord.util.rest.RestEndpoint;
import org.javacord.util.rest.RestMethod;
import org.javacord.util.rest.RestRequest;

import java.util.ArrayList;
import java.util.Collections;
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
        CompletableFuture<List<User>> future = new CompletableFuture<>();
        api.getThreadPool().getExecutorService().submit(() -> {
            try {
                final String value = emoji.asUnicodeEmoji().orElseGet(() -> emoji.asCustomEmoji()
                        .map(e -> e.getName() + ":" + e.getIdAsString()).orElse("UNKNOWN"));
                List<User> users = new ArrayList<>();
                boolean requestMore = true;
                while (requestMore) {
                    RestRequest<List<User>> request =
                            new RestRequest<List<User>>(api, RestMethod.GET, RestEndpoint.REACTION)
                                    .setUrlParameters(
                                            Long.toUnsignedString(channelId), Long.toUnsignedString(messageId), value)
                                    .addQueryParameter("limit", "100")
                                    .setRatelimitRetries(250);
                    if (!users.isEmpty()) {
                        request.addQueryParameter("after", users.get(users.size()-1).getIdAsString());
                    }
                    List<User> incompleteUsers = request.execute(result -> {
                        List<User> paginatedUsers = new ArrayList<>();
                        for (JsonNode userJson : result.getJsonBody()) {
                            paginatedUsers.add(((ImplDiscordApi) api).getOrCreateUser(userJson));
                        }
                        return Collections.unmodifiableList(paginatedUsers);
                    }).join();
                    users.addAll(incompleteUsers);
                    requestMore = incompleteUsers.size() >= 100;
                }
                future.complete(Collections.unmodifiableList(users));
            } catch (Throwable t) {
                future.completeExceptionally(t);
            }
        });
        return future;
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
        String value = emoji.asUnicodeEmoji().orElseGet(() ->
                emoji.asCustomEmoji().map(e -> e.getName() + ":" + e.getIdAsString()).orElse("UNKNOWN"));
        return new RestRequest<Void>(api, RestMethod.DELETE, RestEndpoint.REACTION)
                .setUrlParameters(
                        Long.toUnsignedString(channelId),
                        Long.toUnsignedString(messageId),
                        value,
                        user.isYourself() ? "@me" : user.getIdAsString())
                .setRatelimitRetries(250)
                .execute(result -> null);
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

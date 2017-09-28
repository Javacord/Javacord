package de.btobastian.javacord.entities.message;

import com.mashape.unirest.http.HttpMethod;
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
        String value = getEmoji().asUnicodeEmoji().orElse(
                getEmoji().asCustomEmoji().map(e -> e.getName() + ":" + String.valueOf(e.getId())).orElse("UNKNOWN"));
        return new RestRequest<List<User>>(getMessage().getApi(), HttpMethod.GET, RestEndpoint.REACTION)
                .setUrlParameters(
                        String.valueOf(getMessage().getChannel().getId()), String.valueOf(getMessage().getId()), value)
                .setRatelimitRetries(25)
                .execute(res -> {
                    List<User> users = new ArrayList<>();
                    JSONArray usersJson = res.getBody().getArray();
                    for (int i = 0; i < usersJson.length(); i++) {
                        users.add(((ImplDiscordApi) getMessage().getApi()).getOrCreateUser(usersJson.getJSONObject(i)));
                    }
                    return users;
                });
    }

    /**
     * Removes a user from the list of reactors.
     *
     * @param user The user to remove.
     * @return A future to tell us if the action was successful.
     */
    default CompletableFuture<Void> removeUser(User user) {
        String value = getEmoji().asUnicodeEmoji().orElse(
                getEmoji().asCustomEmoji().map(e -> e.getName() + ":" + String.valueOf(e.getId())).orElse("UNKNOWN"));
        return new RestRequest<Void>(getMessage().getApi(), HttpMethod.DELETE, RestEndpoint.REACTION)
                .setUrlParameters(
                        String.valueOf(getMessage().getChannel().getId()),
                        String.valueOf(getMessage().getId()),
                        value,
                        user.isYourself() ? "@me" : String.valueOf(user.getId()))
                .setRatelimitRetries(25)
                .execute(res -> null);
    }

}

package de.btobastian.javacord.entities.channels;

import com.mashape.unirest.http.HttpMethod;
import de.btobastian.javacord.entities.Mentionable;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.permissions.PermissionType;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestRequest;
import org.json.JSONObject;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a server text channel.
 */
public interface ServerTextChannel extends ServerChannel, TextChannel, Mentionable {

    @Override
    default ChannelType getType() {
        return ChannelType.SERVER_TEXT_CHANNEL;
    }

    /**
     * Checks is the channel is "not safe for work".
     *
     * @return Whether the channel is "not safe for work" or not.
     */
    boolean isNsfw();

    /**
     * Gets the category of the channel.
     *
     * @return The category of the channel.
     */
    Optional<ChannelCategory> getCategory();

    /**
     * Gets the topic of the channel.
     *
     * @return The topic of the channel.
     */
    String getTopic();

    /**
     * Updates the topic of the channel.
     *
     * @param topic The new topic of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateTopic(String topic) {
        return new RestRequest<Void>(getApi(), HttpMethod.PATCH, RestEndpoint.CHANNEL)
                .setUrlParameters(String.valueOf(getId()))
                .setBody(new JSONObject().put("topic", topic == null ? JSONObject.NULL : topic))
                .execute(res -> null);
    }

    /**
     * Updates the nsfw (not safe for work) flag of the channel.
     *
     * @param nsfw The new nsfw flag to set.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateNsfwFlag(boolean nsfw) {
        return new RestRequest<Void>(getApi(), HttpMethod.PATCH, RestEndpoint.CHANNEL)
                .setUrlParameters(String.valueOf(getId()))
                .setBody(new JSONObject().put("nsfw", nsfw))
                .execute(res -> null);
    }

    /**
     * Updates the category of the channel.
     *
     * @param category The new category of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateCategory(ChannelCategory category) {
        return new RestRequest<Void>(getApi(), HttpMethod.PATCH, RestEndpoint.CHANNEL)
                .setUrlParameters(String.valueOf(getId()))
                .setBody(new JSONObject()
                        .put("parent_id", category == null ? JSONObject.NULL : String.valueOf(category.getId())))
                .execute(res -> null);
    }

    /**
     * Removes the channel from its current category.
     *
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> removeCategory() {
        return updateCategory(null);
    }

    /**
     * Checks if the given user can send messages in this channel.
     *
     * @param user The user to check.
     * @return Whether the given user can write messages or not.
     */
    default boolean canWrite(User user) {
        return hasPermissions(user, PermissionType.ADMINISTRATOR) ||
                hasPermissions(user, PermissionType.READ_MESSAGES, PermissionType.SEND_MESSAGES);
    }

}

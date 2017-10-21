package de.btobastian.javacord.entities.channels;

import com.mashape.unirest.http.HttpMethod;
import de.btobastian.javacord.entities.Mentionable;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.permissions.PermissionType;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestRequest;
import org.json.JSONObject;

import java.util.Collection;
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
     * Checks if the given user can send messages in this channel.
     *
     * @param user The user to check.
     * @return Whether the given user can write messages or not.
     */
    default boolean canWrite(User user) {
        Collection<PermissionType> allowed = getEffectiveAllowedPermissions(user);
        return allowed.contains(PermissionType.ADMINISTRATOR) ||
                allowed.contains(PermissionType.READ_MESSAGES) && allowed.contains(PermissionType.SEND_MESSAGES);
    }

}

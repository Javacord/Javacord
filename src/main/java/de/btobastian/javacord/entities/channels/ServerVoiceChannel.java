package de.btobastian.javacord.entities.channels;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestMethod;
import de.btobastian.javacord.utils.rest.RestRequest;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a server voice channel.
 */
public interface ServerVoiceChannel extends ServerChannel, VoiceChannel {

    @Override
    default ChannelType getType() {
        return ChannelType.SERVER_VOICE_CHANNEL;
    }

    /**
     * Gets the category of the channel.
     *
     * @return The category of the channel.
     */
    Optional<ChannelCategory> getCategory();

    /**
     * Updates the user limit of the voice channel.
     *
     * @param limit The limit to set.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateUserLimit(int limit) {
        return new RestRequest<Void>(getApi(), RestMethod.PATCH, RestEndpoint.CHANNEL)
                .setUrlParameters(String.valueOf(getId()))
                .setBody(JsonNodeFactory.instance.objectNode().put("user_limit", limit))
                .execute(result -> null);
    }

    /**
     * Removes the user limit of the voice channel.
     *
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> removeUserLimit() {
        return updateUserLimit(0);
    }

    /**
     * Updates the category of the channel.
     *
     * @param category The new category of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateCategory(ChannelCategory category) {
        return new RestRequest<Void>(getApi(), RestMethod.PATCH, RestEndpoint.CHANNEL)
                .setUrlParameters(String.valueOf(getId()))
                .setBody(JsonNodeFactory.instance.objectNode()
                        .put("parent_id", category == null ? null : category.getIdAsString()))
                .execute(result -> null);
    }

    /**
     * Removes the channel from its current category.
     *
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> removeCategory() {
        return updateCategory(null);
    }

}

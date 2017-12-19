package de.btobastian.javacord.entities.channels;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import de.btobastian.javacord.entities.Icon;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestMethod;
import de.btobastian.javacord.utils.rest.RestRequest;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a group channel. Group channels are not supported by bot accounts!
 */
public interface GroupChannel extends TextChannel, VoiceChannel {

    @Override
    default ChannelType getType() {
        return ChannelType.GROUP_CHANNEL;
    }

    /**
     * Gets the members of the group channel.
     *
     * @return The members of the group channel.
     */
    Collection<User> getMembers();

    /**
     * Gets the name of the channel.
     *
     * @return The name of the channel.
     */
    Optional<String> getName();

    /**
     * Gets the icon of the group channel.
     *
     * @return The icon of the group channel.
     */
    Optional<Icon> getIcon();

    /**
     * Updates the name of the channel.
     *
     * @param name The new name of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateName(String name) {
        return new RestRequest<Void>(getApi(), RestMethod.PATCH, RestEndpoint.CHANNEL)
                .setUrlParameters(String.valueOf(getId()))
                .setBody(JsonNodeFactory.instance.objectNode().put("name", name))
                .execute((res, json) -> null);
    }

}

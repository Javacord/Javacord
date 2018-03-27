package org.javacord.api.event.channel.server.voice;

import java.util.Optional;

/**
 * A server voice channel change user limit event.
 */
public interface ServerVoiceChannelChangeUserLimitEvent extends ServerVoiceChannelEvent {

    /**
     * Gets the new user limit of the channel.
     *
     * @return The new user limit of the channel.
     */
    Optional<Integer> getNewUserLimit();

    /**
     * Gets the old user limit of the channel.
     *
     * @return The old user limit of the channel.
     */
    Optional<Integer> getOldUserLimit();

}

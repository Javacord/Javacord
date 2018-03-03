package org.javacord.event.channel.server.voice;

import org.javacord.entity.channel.ServerVoiceChannel;

import java.util.Optional;

/**
 * A server voice channel change user limit event.
 */
public class ServerVoiceChannelChangeUserLimitEvent extends ServerVoiceChannelEvent {

    /**
     * The new user limit of the channel.
     */
    private final int newUserLimit;

    /**
     * The old user limit of the channel.
     */
    private final int oldUserLimit;

    /**
     * Creates a new server voice channel change userLimit event.
     *
     * @param channel The channel of the event.
     * @param newUserLimit The new user limit of the channel.
     * @param oldUserLimit The old user limit of the channel.
     */
    public ServerVoiceChannelChangeUserLimitEvent(ServerVoiceChannel channel, int newUserLimit, int oldUserLimit) {
        super(channel);
        this.newUserLimit = newUserLimit;
        this.oldUserLimit = oldUserLimit;
    }

    /**
     * Gets the new user limit of the channel.
     *
     * @return The new user limit of the channel.
     */
    public Optional<Integer> getNewUserLimit() {
        return newUserLimit == 0 ? Optional.empty() : Optional.of(newUserLimit);
    }

    /**
     * Gets the old user limit of the channel.
     *
     * @return The old user limit of the channel.
     */
    public Optional<Integer> getOldUserLimit() {
        return oldUserLimit == 0 ? Optional.empty() : Optional.of(oldUserLimit);
    }
}

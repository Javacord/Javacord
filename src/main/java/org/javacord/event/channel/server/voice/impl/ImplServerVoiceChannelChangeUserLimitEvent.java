package org.javacord.event.channel.server.voice.impl;

import org.javacord.entity.channel.ServerVoiceChannel;
import org.javacord.event.channel.server.voice.ServerVoiceChannelChangeUserLimitEvent;

import java.util.Optional;

/**
 * The implementation of {@link ServerVoiceChannelChangeUserLimitEvent}.
 */
public class ImplServerVoiceChannelChangeUserLimitEvent extends ImplServerVoiceChannelEvent
        implements ServerVoiceChannelChangeUserLimitEvent {

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
    public ImplServerVoiceChannelChangeUserLimitEvent(ServerVoiceChannel channel, int newUserLimit, int oldUserLimit) {
        super(channel);
        this.newUserLimit = newUserLimit;
        this.oldUserLimit = oldUserLimit;
    }

    @Override
    public Optional<Integer> getNewUserLimit() {
        return newUserLimit == 0 ? Optional.empty() : Optional.of(newUserLimit);
    }

    @Override
    public Optional<Integer> getOldUserLimit() {
        return oldUserLimit == 0 ? Optional.empty() : Optional.of(oldUserLimit);
    }
}

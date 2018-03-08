package org.javacord.event.channel.server.voice.impl;

import org.javacord.entity.channel.ServerVoiceChannel;
import org.javacord.entity.user.User;

/**
 * The implementation of {@link ImplServerVoiceChannelEvent}.
 */
public abstract class ImplServerVoiceChannelMemberEvent extends ImplServerVoiceChannelEvent {

    /**
     * The user of the event.
     */
    private final User user;

    /**
     * Creates a new voice channel member event.
     *
     * @param user The user of the event.
     * @param channel The channel of the event.
     */
    public ImplServerVoiceChannelMemberEvent(User user, ServerVoiceChannel channel) {
        super(channel);
        this.user = user;
    }

    /**
     * Gets the user of the event.
     *
     * @return The user of the event.
     */
    public User getUser() {
        return user;
    }

}

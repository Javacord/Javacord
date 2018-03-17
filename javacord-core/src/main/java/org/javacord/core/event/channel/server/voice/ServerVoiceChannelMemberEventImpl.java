package org.javacord.core.event.channel.server.voice;

import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.user.User;

/**
 * The implementation of {@link ServerVoiceChannelEventImpl}.
 */
public abstract class ServerVoiceChannelMemberEventImpl extends ServerVoiceChannelEventImpl {

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
    public ServerVoiceChannelMemberEventImpl(User user, ServerVoiceChannel channel) {
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

package org.javacord.core.event.channel.server.voice;

import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.user.User;

/**
 * The implementation of {@link ServerVoiceChannelEventImpl}.
 */
public abstract class ServerVoiceChannelMemberEventImpl extends ServerVoiceChannelEventImpl {

    /**
     * The id of the user of the event.
     */
    private final Long userId;

    /**
     * Creates a new voice channel member event.
     *
     * @param userId The id of the user of the event.
     * @param channel The channel of the event.
     */
    public ServerVoiceChannelMemberEventImpl(Long userId, ServerVoiceChannel channel) {
        super(channel);
        this.userId = userId;
    }

    /**
     * Gets the user of the event.
     *
     * @return The user of the event.
     */
    public User getUser() {
        // server related events should only get dispatched after all members are cached
        return getApi().getCachedUserById(userId).orElseThrow(AssertionError::new);
    }

}

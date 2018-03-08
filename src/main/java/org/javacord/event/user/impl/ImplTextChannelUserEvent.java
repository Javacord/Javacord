package org.javacord.event.user.impl;

import org.javacord.entity.channel.TextChannel;
import org.javacord.entity.user.User;
import org.javacord.event.user.TextChannelUserEvent;

/**
 * The implementation of {@link TextChannelUserEvent}.
 */
public abstract class ImplTextChannelUserEvent extends ImplUserEvent implements TextChannelUserEvent {

    /**
     * The text channel of the event.
     */
    private final TextChannel channel;

    /**
     * Creates a new text channel user event.
     *
     * @param user The user of the event.
     * @param channel The text channel of the event.
     */
    public ImplTextChannelUserEvent(User user, TextChannel channel) {
        super(user);
        this.channel = channel;
    }

    @Override
    public TextChannel getChannel() {
        return channel;
    }

}

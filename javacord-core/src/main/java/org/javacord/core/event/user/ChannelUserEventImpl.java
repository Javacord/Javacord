package org.javacord.core.event.user;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.member.Member;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.user.TextChannelUserEvent;

/**
 * The implementation of {@link TextChannelUserEvent}.
 */
public abstract class ChannelUserEventImpl extends UserEventImpl implements TextChannelUserEvent {

    private final TextChannel channel;

    /**
     * Creates a new text channel user event.
     *
     * @param user The user of the event.
     * @param member The member of the event.
     * @param channel The text channel of the event.
     */
    public ChannelUserEventImpl(User user, Member member, TextChannel channel) {
        super(user);
        this.channel = channel;
    }

    @Override
    public TextChannel getChannel() {
        return channel;
    }
}

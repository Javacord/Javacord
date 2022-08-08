package org.javacord.core.event.server.member;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.member.Member;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.member.ServerMemberUserStartTypingEvent;
import org.javacord.core.event.EventImpl;
import java.util.Optional;

/**
 * The implementation of {@link ServerMemberUserStartTypingEvent}.
 */
public class ServerMemberUserStartTypingEventImpl extends EventImpl implements ServerMemberUserStartTypingEvent {

    private final TextChannel channel;
    private final long userId;
    private final Member member;

    /**
     * Creates a new user start typing event.
     *
     * @param channel The text channel of the event.
     * @param userId  The id of the user of the event.
     * @param member  The member of the event.
     */
    public ServerMemberUserStartTypingEventImpl(TextChannel channel, long userId, Member member) {
        super(channel.getApi());
        this.channel = channel;
        this.userId = userId;
        this.member = member;
    }

    @Override
    public TextChannel getChannel() {
        return channel;
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public Optional<User> getUser() {
        return Optional.ofNullable(member).map(Member::getUser);
    }
}

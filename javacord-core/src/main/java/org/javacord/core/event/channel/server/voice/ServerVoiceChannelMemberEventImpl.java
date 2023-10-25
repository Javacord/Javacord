package org.javacord.core.event.channel.server.voice;

import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.member.Member;

/**
 * The implementation of {@link ServerVoiceChannelEventImpl}.
 */
public abstract class ServerVoiceChannelMemberEventImpl extends ServerVoiceChannelEventImpl {

    /**
     * The member of the event.
     */
    private final Member member;

    /**
     * Creates a new voice channel member event.
     *
     * @param member The member of the event.
     * @param channel The channel of the event.
     */
    public ServerVoiceChannelMemberEventImpl(Member member, ServerVoiceChannel channel) {
        super(channel);
        this.member = member;
    }

    /**
     * Gets the user of the event.
     *
     * @return The user of the event.
     */
    public Member getMember() {
        return member;
    }

}

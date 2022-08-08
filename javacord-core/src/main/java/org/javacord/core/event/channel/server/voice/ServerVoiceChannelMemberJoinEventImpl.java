package org.javacord.core.event.channel.server.voice;

import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.member.Member;
import org.javacord.api.event.channel.server.voice.ServerVoiceChannelMemberJoinEvent;
import java.util.Optional;

/**
 * The implementation of {@link ServerVoiceChannelMemberJoinEvent}.
 */
public class ServerVoiceChannelMemberJoinEventImpl extends ServerVoiceChannelMemberEventImpl
        implements ServerVoiceChannelMemberJoinEvent {

    /**
     * The old channel of the event.
     */
    private final ServerVoiceChannel oldChannel;

    /**
     * Creates a new server voice channel member join event.
     *
     * @param member The member of the event.
     * @param newChannel The new channel of the event.
     * @param oldChannel The old channel of the event.
     */
    public ServerVoiceChannelMemberJoinEventImpl(
            Member member, ServerVoiceChannel newChannel, ServerVoiceChannel oldChannel) {
        super(member, newChannel);
        this.oldChannel = oldChannel;
    }

    @Override
    public Optional<ServerVoiceChannel> getOldChannel() {
        return Optional.ofNullable(oldChannel);
    }

    @Override
    public boolean isMove() {
        return oldChannel != null;
    }
}

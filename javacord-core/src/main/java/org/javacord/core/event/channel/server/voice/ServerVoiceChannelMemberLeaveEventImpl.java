
package org.javacord.core.event.channel.server.voice;

import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.member.Member;
import org.javacord.api.event.channel.server.voice.ServerVoiceChannelMemberLeaveEvent;
import java.util.Optional;

/**
 * The implementation of {@link ServerVoiceChannelMemberLeaveEvent}.
 */
public class ServerVoiceChannelMemberLeaveEventImpl extends ServerVoiceChannelMemberEventImpl
        implements ServerVoiceChannelMemberLeaveEvent {

    /**
     * The new channel of the event.
     */
    private final ServerVoiceChannel newChannel;

    /**
     * Creates a new server voice channel member leave event.
     *
     * @param member The member of the event.
     * @param newChannel The new channel of the event.
     * @param oldChannel The old channel of the event.
     */
    public ServerVoiceChannelMemberLeaveEventImpl(
            Member member, ServerVoiceChannel newChannel, ServerVoiceChannel oldChannel) {
        super(member, oldChannel);
        this.newChannel = newChannel;
    }

    @Override
    public Optional<ServerVoiceChannel> getNewChannel() {
        return Optional.ofNullable(newChannel);
    }

    @Override
    public boolean isMove() {
        return newChannel != null;
    }
}

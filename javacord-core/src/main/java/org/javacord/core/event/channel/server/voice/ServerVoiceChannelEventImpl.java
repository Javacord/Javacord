package org.javacord.core.event.channel.server.voice;

import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.event.channel.server.voice.ServerVoiceChannelEvent;
import org.javacord.core.event.server.ServerEventImpl;

/**
 * The implementation of {@link ServerVoiceChannelEvent}.
 */
public abstract class ServerVoiceChannelEventImpl extends ServerEventImpl implements ServerVoiceChannelEvent {

    /**
     * The channel of the event.
     */
    protected final ServerVoiceChannel channel;

    /**
     * Creates a new voice channel event.
     *
     * @param channel The channel of the event.
     */
    public ServerVoiceChannelEventImpl(ServerVoiceChannel channel) {
        super(channel.getServer());
        this.channel = channel;
    }

    @Override
    public ServerVoiceChannel getChannel() {
        return channel;
    }

}

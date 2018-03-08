package org.javacord.event.channel.server.voice.impl;

import org.javacord.entity.channel.ServerVoiceChannel;
import org.javacord.event.channel.server.voice.ServerVoiceChannelEvent;
import org.javacord.event.server.impl.ImplServerEvent;

/**
 * The implementation of {@link ServerVoiceChannelEvent}.
 */
public abstract class ImplServerVoiceChannelEvent extends ImplServerEvent implements ServerVoiceChannelEvent {

    /**
     * The channel of the event.
     */
    private final ServerVoiceChannel channel;

    /**
     * Creates a new voice channel event.
     *
     * @param channel The channel of the event.
     */
    public ImplServerVoiceChannelEvent(ServerVoiceChannel channel) {
        super(channel.getServer());
        this.channel = channel;
    }

    @Override
    public ServerVoiceChannel getChannel() {
        return channel;
    }

}

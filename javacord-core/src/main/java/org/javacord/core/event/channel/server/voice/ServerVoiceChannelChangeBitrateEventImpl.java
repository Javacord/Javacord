package org.javacord.core.event.channel.server.voice;

import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.event.channel.server.voice.ServerVoiceChannelChangeBitrateEvent;

/**
 * The implementation of {@link ServerVoiceChannelChangeBitrateEvent}.
 */
public class ServerVoiceChannelChangeBitrateEventImpl extends ServerVoiceChannelEventImpl
        implements ServerVoiceChannelChangeBitrateEvent {

    /**
     * The new bitrate of the channel.
     */
    private final int newBitrate;

    /**
     * The old bitrate of the channel.
     */
    private final int oldBitrate;

    /**
     * Creates a new server voice channel change bitrate event.
     *
     * @param channel The channel of the event.
     * @param newBitrate The new bitrate of the channel.
     * @param oldBitrate The old bitrate of the channel.
     */
    public ServerVoiceChannelChangeBitrateEventImpl(ServerVoiceChannel channel, int newBitrate, int oldBitrate) {
        super(channel);
        this.newBitrate = newBitrate;
        this.oldBitrate = oldBitrate;
    }

    @Override
    public int getNewBitrate() {
        return newBitrate;
    }

    @Override
    public int getOldBitrate() {
        return oldBitrate;
    }
}

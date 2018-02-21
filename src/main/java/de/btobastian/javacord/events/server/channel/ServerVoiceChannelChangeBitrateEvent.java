package de.btobastian.javacord.events.server.channel;

import de.btobastian.javacord.entities.channels.ServerVoiceChannel;

/**
 * A server voice channel change bitrate event.
 */
public class ServerVoiceChannelChangeBitrateEvent extends ServerVoiceChannelEvent {

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
    public ServerVoiceChannelChangeBitrateEvent(ServerVoiceChannel channel, int newBitrate, int oldBitrate) {
        super(channel);
        this.newBitrate = newBitrate;
        this.oldBitrate = oldBitrate;
    }

    /**
     * Gets the new bitrate of the channel.
     *
     * @return The new bitrate of the channel.
     */
    public int getNewBitrate() {
        return newBitrate;
    }

    /**
     * Gets the old bitrate of the channel.
     *
     * @return The old bitrate of the channel.
     */
    public int getOldBitrate() {
        return oldBitrate;
    }
}

package org.javacord.api.event.channel.server.voice;

/**
 * A server voice channel change bitrate event.
 */
public interface ServerVoiceChannelChangeBitrateEvent extends ServerVoiceChannelEvent {

    /**
     * Gets the new bitrate of the channel.
     *
     * @return The new bitrate of the channel.
     */
    int getNewBitrate();

    /**
     * Gets the old bitrate of the channel.
     *
     * @return The old bitrate of the channel.
     */
    int getOldBitrate();

}

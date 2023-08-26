package org.javacord.api.entity.channel.internal;

import org.javacord.api.entity.channel.ServerVoiceChannelUpdater;

/**
 * This class is internally used by the {@link ServerVoiceChannelUpdater} to update server voice channels.
 * You usually don't want to interact with this object.
 */
public interface ServerVoiceChannelUpdaterDelegate extends TextableRegularServerChannelUpdaterDelegate {

    /**
     * Queues the bitrate to be updated.
     *
     * @param bitrate The new bitrate of the channel.
     */
    void setBitrate(int bitrate);

    /**
     * Queues the user limit to be updated.
     *
     * @param userLimit The new user limit of the channel.
     */
    void setUserLimit(int userLimit);

    /**
     * Queues the user limit to be removed.
     */
    void removeUserLimit();
}

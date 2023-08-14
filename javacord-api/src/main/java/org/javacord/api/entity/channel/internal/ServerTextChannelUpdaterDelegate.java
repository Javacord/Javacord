package org.javacord.api.entity.channel.internal;

import org.javacord.api.entity.channel.ServerTextChannelUpdater;

/**
 * This class is internally used by the {@link ServerTextChannelUpdater} to update server text channels.
 * You usually don't want to interact with this object.
 */
public interface ServerTextChannelUpdaterDelegate extends TextableRegularServerChannelUpdaterDelegate {

    /**
     * Queues the topic to be updated.
     *
     * @param topic The new topic of the channel.
     */
    void setTopic(String topic);
}
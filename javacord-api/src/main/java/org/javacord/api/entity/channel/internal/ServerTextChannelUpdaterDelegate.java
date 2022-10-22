package org.javacord.api.entity.channel.internal;

import org.javacord.api.entity.channel.ServerTextChannelUpdater;

/**
 * This class is internally used by the {@link ServerTextChannelUpdater} to update server text channels.
 * You usually don't want to interact with this object.
 */
public interface ServerTextChannelUpdaterDelegate extends ServerMessageChannelUpdaterDelegate {
    /**
     * Sets the slowmode delay.
     *
     * @param delay The delay in seconds.
     */
    void setSlowmodeDelayInSeconds(int delay);
}

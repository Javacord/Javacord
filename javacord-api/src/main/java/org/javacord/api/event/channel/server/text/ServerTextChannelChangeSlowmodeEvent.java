package org.javacord.api.event.channel.server.text;

/**
 * An event signalling a change in the slowmode settings for a channel.
 */
public interface ServerTextChannelChangeSlowmodeEvent extends ServerTextChannelEvent {

    /**
     * Gets the old delay of the channel.
     *
     * <p>A delay of 0 means that the channel has not been in slowmode before.
     *
     * @return The delay in seconds.
     */
    int getOldDelayInSeconds();

    /**
     * Gets the new delay of the channel.
     *
     * <p>A delay of 0 means that the channel is no longer in slowmode.
     *
     * @return The delay in seconds.
     */
    int getNewDelayInSeconds();

}

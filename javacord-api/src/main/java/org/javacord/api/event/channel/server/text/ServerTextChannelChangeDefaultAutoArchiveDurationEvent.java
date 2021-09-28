package org.javacord.api.event.channel.server.text;


/**
 * An event signalling a change in the default auto archive duration settings for a channel.
 */
public interface ServerTextChannelChangeDefaultAutoArchiveDurationEvent extends ServerTextChannelEvent {

    /**
     * Gets the old default auto archive duration of the channel.
     *
     * @return The default auto archive duration in seconds.
     */
    int getOldDefaultAutoArchiveDuration();

    /**
     * Gets the new default auto archive duration of the channel.
     *
     * @return The default auto archive duration in seconds.
     */
    int getAutoArchiveDuration();

}

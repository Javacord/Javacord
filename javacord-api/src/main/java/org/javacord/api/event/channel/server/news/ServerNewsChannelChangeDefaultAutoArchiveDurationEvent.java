package org.javacord.api.event.channel.server.news;

public interface ServerNewsChannelChangeDefaultAutoArchiveDurationEvent extends ServerNewsChannelEvent {
    /**
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

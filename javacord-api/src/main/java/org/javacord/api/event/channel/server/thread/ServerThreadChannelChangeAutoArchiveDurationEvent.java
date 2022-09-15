package org.javacord.api.event.channel.server.thread;

public interface ServerThreadChannelChangeAutoArchiveDurationEvent extends ServerThreadChannelEvent {
    /**
     * Gets the new auto archive duration.
     *
     * @return The new auto archive duration.
     */
    int getNewAutoArchiveDuration();

    /**
     * Gets the old auto archive duration.
     *
     * @return The old auto archive duration.
     */
    int getOldAutoArchiveDuration();
}

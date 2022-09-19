package org.javacord.api.event.channel.server.thread;

import java.time.Instant;

public interface ServerThreadChannelChangeArchiveTimestampEvent extends ServerThreadChannelEvent {
    /**
     * Gets the new archive timestamp.
     *
     * @return The new archive timestamp.
     */
    Instant getNewArchiveTimestamp();

    /**
     * Gets the old archive timestamp.
     *
     * @return The old archive timestamp.
     */
    Instant getOldArchiveTimestamp();
}

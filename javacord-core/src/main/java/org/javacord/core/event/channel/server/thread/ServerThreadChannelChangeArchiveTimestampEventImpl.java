package org.javacord.core.event.channel.server.thread;

import org.javacord.api.entity.channel.ServerThreadChannel;
import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeArchiveTimestampEvent;

import java.time.Instant;

/**
 * The implementation of {@link ServerThreadChannelChangeArchiveTimestampEvent}.
 */
public class ServerThreadChannelChangeArchiveTimestampEventImpl extends ServerThreadChannelEventImpl implements
        ServerThreadChannelChangeArchiveTimestampEvent {
    /**
     * The new archive timestamp of the thread.
     */
    private final Instant newArchiveTimestamp;

    /**
     * The old archive timestamp of the thread.
     */
    private final Instant oldArchiveTimestamp;

    /**
     * Creates a new server text channel archive timestamp update event.
     *
     * @param channel The channel of the event.
     * @param newArchiveTimestamp The new archive timestamp of the thread.
     * @param oldArchiveTimestamp The old archive timestamp of the thread.
     */
    public ServerThreadChannelChangeArchiveTimestampEventImpl(ServerThreadChannel channel, Instant newArchiveTimestamp,
                                                              Instant oldArchiveTimestamp) {
        super(channel);
        this.newArchiveTimestamp = newArchiveTimestamp;
        this.oldArchiveTimestamp = oldArchiveTimestamp;
    }

    @Override
    public Instant getNewArchiveTimestamp() {
        return newArchiveTimestamp;
    }

    @Override
    public Instant getOldArchiveTimestamp() {
        return oldArchiveTimestamp;
    }
}

package org.javacord.core.event.channel.server.thread;

import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeAutoArchiveDurationEvent;
import org.javacord.core.entity.channel.ServerThreadChannelImpl;

/**
 * The implementation of {@link ServerThreadChannelChangeAutoArchiveDurationEvent}.
 */
public class ServerThreadChannelChangeAutoArchiveDurationEventImpl
        extends ServerThreadChannelEventImpl implements ServerThreadChannelChangeAutoArchiveDurationEvent {
    /**
     * The new auto archive duration.
     */
    private final int newAutoArchiveDuration;

    /**
     * The old auto archive duration.
     */
    private final int oldAutoArchiveDuration;

    /**
     * Creates a new instance of this class.
     *
     * @param channel The channel.
     * @param newAutoArchiveDuration The new auto archive duration.
     * @param oldAutoArchiveDuration The old auto archive duration.
     */
    public ServerThreadChannelChangeAutoArchiveDurationEventImpl(ServerThreadChannelImpl channel,
                                                                 int newAutoArchiveDuration,
                                                                 int oldAutoArchiveDuration) {
        super(channel);
        this.newAutoArchiveDuration = newAutoArchiveDuration;
        this.oldAutoArchiveDuration = oldAutoArchiveDuration;
    }

    @Override
    public int getNewAutoArchiveDuration() {
        return newAutoArchiveDuration;
    }

    @Override
    public int getOldAutoArchiveDuration() {
        return oldAutoArchiveDuration;
    }
}

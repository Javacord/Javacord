package org.javacord.core.event.channel.server.thread;

import org.javacord.api.entity.channel.ServerThreadChannel;
import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeArchivedEvent;

/**
 * The implementation of {@link ServerThreadChannelEventImpl}.
 */
public class ServerThreadChannelChangeArchivedEventImpl extends ServerThreadChannelEventImpl
        implements ServerThreadChannelChangeArchivedEvent {
    /**
     * The new archived state of the channel.
     */
    private final boolean isArchived;

    /**
     * The old archived state of the channel.
     */
    private final boolean wasArchived;

    /**
     * Creates a new server text channel archived change event.
     *
     * @param channel The channel of the event.
     * @param isArchived The new archived state of the channel.
     * @param wasArchived The old archived state of the channel.
     */
    public ServerThreadChannelChangeArchivedEventImpl(ServerThreadChannel channel, boolean isArchived,
                                                      boolean wasArchived) {
        super(channel);
        this.isArchived = isArchived;
        this.wasArchived = wasArchived;
    }

    @Override
    public boolean isArchived() {
        return isArchived;
    }

    @Override
    public boolean wasArchived() {
        return wasArchived;
    }
}

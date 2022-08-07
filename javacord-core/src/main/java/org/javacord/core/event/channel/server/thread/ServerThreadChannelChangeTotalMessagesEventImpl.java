package org.javacord.core.event.channel.server.thread;

import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeTotalMessagesEvent;
import org.javacord.core.entity.channel.ServerThreadChannelImpl;

/**
 * The implementation of {@link ServerThreadChannelChangeTotalMessagesEvent}.
 */
public class ServerThreadChannelChangeTotalMessagesEventImpl extends ServerThreadChannelEventImpl
        implements ServerThreadChannelChangeTotalMessagesEvent {
    /**
     * The new total message count.
     */
    private final int newTotalMessageCount;

    /**
     * The old total message count.
     */
    private final int oldTotalMessageCount;

    /**
     * Creates a new instance of this class.
     *
     * @param channel The channel.
     * @param newTotalMessageCount The new total message count.
     * @param oldTotalMessageCount The old total message count.
     */
    public ServerThreadChannelChangeTotalMessagesEventImpl(ServerThreadChannelImpl channel,
                                                           int newTotalMessageCount, int oldTotalMessageCount) {
        super(channel);
        this.newTotalMessageCount = newTotalMessageCount;
        this.oldTotalMessageCount = oldTotalMessageCount;
    }

    @Override
    public int getNewTotalMessageCount() {
        return newTotalMessageCount;
    }

    @Override
    public int getOldTotalMessageCount() {
        return oldTotalMessageCount;
    }
}

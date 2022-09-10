package org.javacord.core.event.channel.server.thread;

import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeTotalMessageSentEvent;
import org.javacord.core.entity.channel.ServerThreadChannelImpl;

/**
 * The implementation of {@link ServerThreadChannelChangeTotalMessageSentEvent}.
 */
public class ServerThreadChannelChangeTotalMessageSentEventImpl extends ServerThreadChannelEventImpl
        implements ServerThreadChannelChangeTotalMessageSentEvent {
    /**
     * The new total message count.
     */
    private final int newTotalMessageSent;

    /**
     * The old total message count.
     */
    private final int oldTotalMessageSent;

    /**
     * Creates a new instance of this class.
     *
     * @param channel The channel.
     * @param newTotalMessageSent The new total message count.
     * @param oldTotalMessageSent The old total message count.
     */
    public ServerThreadChannelChangeTotalMessageSentEventImpl(ServerThreadChannelImpl channel,
                                                              int newTotalMessageSent, int oldTotalMessageSent) {
        super(channel);
        this.newTotalMessageSent = newTotalMessageSent;
        this.oldTotalMessageSent = oldTotalMessageSent;
    }

    @Override
    public int getNewTotalMessageSent() {
        return newTotalMessageSent;
    }

    @Override
    public int getOldTotalMessageSent() {
        return oldTotalMessageSent;
    }
}

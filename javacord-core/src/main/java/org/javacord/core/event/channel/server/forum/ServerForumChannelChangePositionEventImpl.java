package org.javacord.core.event.channel.server.forum;

import org.javacord.api.entity.channel.ServerForumChannel;
import org.javacord.api.event.channel.server.forum.ServerForumChannelChangePositionEvent;

/**
 * The implementation of {@link ServerForumChannelChangePositionEvent}.
 */
public class ServerForumChannelChangePositionEventImpl extends ServerForumChannelEventImpl
        implements ServerForumChannelChangePositionEvent {

    /**
     * The old position of the channel.
     */
    private final int oldPosition;

    /**
     * The new position of the channel.
     */
    private final int newPosition;

    /**
     * Creates a new server forum channel change position event.
     *
     * @param channel The channel of the event.
     * @param oldPosition The old position of the channel.
     * @param newPosition The new position of the channel.
     */
    public ServerForumChannelChangePositionEventImpl(final ServerForumChannel channel,
                                                     final int oldPosition, final int newPosition) {
        super(channel);
        this.oldPosition = oldPosition;
        this.newPosition = newPosition;
    }

    @Override
    public int getOldPosition() {
        return oldPosition;
    }

    @Override
    public int getNewPosition() {
        return newPosition;
    }
}

package de.btobastian.javacord.events.server.channel;

import de.btobastian.javacord.entities.channels.ServerChannel;

/**
 * A server channel change position event.
 */
public class ServerChannelChangePositionEvent extends ServerChannelEvent {

    /**
     * The new position of the channel.
     */
    private final int newPosition;

    /**
     * The old position of the channel.
     */
    private final int oldPosition;

    /**
     * Creates a new server channel change name event.
     *
     * @param channel The channel of the event.
     * @param newPosition The new position of the channel.
     * @param oldPosition The old position of the channel.
     */
    public ServerChannelChangePositionEvent(ServerChannel channel, int newPosition, int oldPosition) {
        super(channel);
        this.newPosition = newPosition;
        this.oldPosition = oldPosition;
    }

    /**
     * Gets the new position of the channel.
     *
     * @return The new position of the channel.
     */
    public int getNewPosition() {
        return newPosition;
    }

    /**
     * Gets the old position of the channel.
     *
     * @return The old position of the channel.
     */
    public int getOldPosition() {
        return oldPosition;
    }
}

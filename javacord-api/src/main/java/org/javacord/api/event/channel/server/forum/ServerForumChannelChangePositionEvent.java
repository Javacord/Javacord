package org.javacord.api.event.channel.server.forum;

/**
 * A server forum channel change position event.
 */
public interface ServerForumChannelChangePositionEvent extends ServerForumChannelEvent {
    /**
     * Gets the new position of the forum channel.
     *
     * @return The new position of the forum channel.
     */
    int getNewPosition();

    /**
     * Gets the old position of the forum channel.
     *
     * @return The old position of the forum channel.
     */
    int getOldPosition();
}

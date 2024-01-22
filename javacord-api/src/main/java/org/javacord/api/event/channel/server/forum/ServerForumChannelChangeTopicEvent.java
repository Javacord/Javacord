package org.javacord.api.event.channel.server.forum;

/**
 * A server forum channel change topic event.
 */
public interface ServerForumChannelChangeTopicEvent extends ServerForumChannelEvent {

    /**
     * Gets the new topic of the forum channel.
     *
     * @return The new topic of the forum channel.
     */
    String getNewTopic();

    /**
     * Gets the old topic of the forum channel.
     *
     * @return The old topic of the forum channel.
     */
    String getOldTopic();
}

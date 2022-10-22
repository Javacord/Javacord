package org.javacord.api.event.channel.server.news;

public interface ServerNewsChannelChangeTopicEvent extends ServerNewsChannelEvent {
    /**
     * Gets the new topic of the channel.
     *
     * @return The new topic of the channel.
     */
    String getNewTopic();

    /**
     * Gets the old topic of the channel.
     *
     * @return The old topic of the channel.
     */
    String getOldTopic();
}

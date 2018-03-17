package org.javacord.api.event.channel.server.text;

/**
 * A server text channel change topic event.
 */
public interface ServerTextChannelChangeTopicEvent extends ServerTextChannelEvent {

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

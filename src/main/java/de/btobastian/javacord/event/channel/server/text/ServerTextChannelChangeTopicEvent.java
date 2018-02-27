package de.btobastian.javacord.event.channel.server.text;

import de.btobastian.javacord.entity.channel.ServerTextChannel;

/**
 * A server text channel change topic event.
 */
public class ServerTextChannelChangeTopicEvent extends ServerTextChannelEvent {

    /**
     * The new topic of the channel.
     */
    private final String newTopic;

    /**
     * The old topic of the channel.
     */
    private final String oldTopic;

    /**
     * Creates a new server text channel change topic event.
     *
     * @param channel The channel of the event.
     * @param newTopic The new topic of the channel.
     * @param oldTopic The old topic of the channel.
     */
    public ServerTextChannelChangeTopicEvent(ServerTextChannel channel, String newTopic, String oldTopic) {
        super(channel);
        this.newTopic = newTopic;
        this.oldTopic = oldTopic;
    }

    /**
     * Gets the new topic of the channel.
     *
     * @return The new topic of the channel.
     */
    public String getNewTopic() {
        return newTopic;
    }

    /**
     * Gets the old topic of the channel.
     *
     * @return The old topic of the channel.
     */
    public String getOldTopic() {
        return oldTopic;
    }
}

package org.javacord.event.channel.server.text.impl;

import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.event.channel.server.text.ServerTextChannelChangeTopicEvent;

/**
 * The implementation of {@link ServerTextChannelChangeTopicEvent}.
 */
public class ImplServerTextChannelChangeTopicEvent extends ImplServerTextChannelEvent
        implements ServerTextChannelChangeTopicEvent {

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
    public ImplServerTextChannelChangeTopicEvent(ServerTextChannel channel, String newTopic, String oldTopic) {
        super(channel);
        this.newTopic = newTopic;
        this.oldTopic = oldTopic;
    }

    @Override
    public String getNewTopic() {
        return newTopic;
    }

    @Override
    public String getOldTopic() {
        return oldTopic;
    }
}

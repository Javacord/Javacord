package org.javacord.core.event.channel.server.news;

import org.javacord.api.entity.channel.ServerNewsChannel;
import org.javacord.api.event.channel.server.news.ServerNewsChannelChangeTopicEvent;

public class ServerNewsChannelChangeTopicEventImpl extends ServerNewsChannelEventImpl
        implements ServerNewsChannelChangeTopicEvent {

    /**
     * The new topic of the channel.
     */
    private final String newTopic;

    /**
     * The old topic of the channel.
     */
    private final String oldTopic;

    /**
     * Creates a new server news channel change topic event.
     *
     * @param channel  The channel of the event.
     * @param newTopic The new topic of the channel.
     * @param oldTopic The old topic of the channel.
     */
    public ServerNewsChannelChangeTopicEventImpl(ServerNewsChannel channel, String newTopic, String oldTopic) {
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

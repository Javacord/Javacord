package org.javacord.core.event.channel.server.forum;

import org.javacord.api.entity.channel.ServerForumChannel;
import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeTopicEvent;

/**
 * The implementation of {@link ServerForumChannelChangeTopicEvent}.
 */
public class ServerForumChannelChangeTopicEventImpl extends ServerForumChannelEventImpl
        implements ServerForumChannelChangeTopicEvent {

    /**
     * The old topic of the channel.
     */
    private final String oldTopic;

    /**
     * The new topic of the channel.
     */
    private final String newTopic;

    /**
     * Creates a new server forum channel change topic event.
     *
     * @param channel The channel of the event.
     * @param oldTopic The old topic of the channel.
     * @param newTopic The new topic of the channel.
     */
    public ServerForumChannelChangeTopicEventImpl(final ServerForumChannel channel,
                                                  final String oldTopic, final String newTopic) {
        super(channel);
        this.oldTopic = oldTopic;
        this.newTopic = newTopic;
    }

    @Override
    public String getOldTopic() {
        return oldTopic;
    }

    @Override
    public String getNewTopic() {
        return newTopic;
    }
}

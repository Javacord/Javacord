package org.javacord.core.event.channel.server.voice;

import org.javacord.api.entity.channel.ServerStageVoiceChannel;
import org.javacord.api.event.channel.server.voice.ServerStageVoiceChannelChangeTopicEvent;

public class ServerStageVoiceChannelChangeTopicEventImpl extends ServerVoiceChannelEventImpl
        implements ServerStageVoiceChannelChangeTopicEvent {

    /**
     * The new topic of the channel.
     */
    private final String newTopic;

    /**
     * The old topic of the channel.
     */
    private final String oldTopic;

    /**
     * Creates a new server stage voice channel change topic event.
     *
     * @param channel The server stage voice channel of the event.
     * @param newTopic The new topic of the channel.
     * @param oldTopic The old topic of the channel.
     */

    public ServerStageVoiceChannelChangeTopicEventImpl(ServerStageVoiceChannel channel, String newTopic,
                                                       String oldTopic) {
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

    @Override
    public ServerStageVoiceChannel getChannel() {
        return (ServerStageVoiceChannel) channel;
    }

}

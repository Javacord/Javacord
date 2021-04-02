package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.channel.ServerStageVoiceChannel;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.listener.channel.server.voice.InternalServerStageVoiceChannelAttachableListenerManager;

import java.util.Optional;

public class ServerStageVoiceChannelImpl extends ServerVoiceChannelImpl
        implements ServerStageVoiceChannel, InternalServerStageVoiceChannelAttachableListenerManager {

    /**
     * The topic of the stage.
     */
    private String topic;

    /**
     * Creates a new server stage voice channel object.
     *
     * @param api The discord api instance.
     * @param server The server of the channel.
     * @param data The json data of the channel.
     */
    public ServerStageVoiceChannelImpl(DiscordApiImpl api, ServerImpl server, JsonNode data) {
        super(api, server, data);

        if (data.hasNonNull("topic")) {
            topic = data.get("topic").asText();
        } else {
            topic = null;
        }

    }

    @Override
    public Optional<String> getTopic() {
        return Optional.ofNullable(topic);
    }

    /**
     * Sets the topic of the channel.
     *
     * @param topic The new topic of the channel.
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        return String.format("ServerStageVoiceChannel (id: %s, name: %s)", getIdAsString(), getName());
    }

}

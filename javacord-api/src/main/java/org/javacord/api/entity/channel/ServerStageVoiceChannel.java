package org.javacord.api.entity.channel;

import org.javacord.api.listener.channel.server.voice.ServerStageVoiceChannelAttachableListenerManager;

import java.util.Optional;

public interface ServerStageVoiceChannel extends ServerVoiceChannel, ServerStageVoiceChannelAttachableListenerManager {

    /**
     * Gets the topic of this.
     *
     * @return The topic of this channel.
     */
    Optional<String> getTopic();
}

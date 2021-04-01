package org.javacord.api.entity.channel;

import org.javacord.api.listener.channel.server.voice.ServerStageVoiceChannelAttachableListenerManager;

import java.util.Optional;

public interface ServerStageVoiceChannel extends ServerVoiceChannel, ServerStageVoiceChannelAttachableListenerManager {

    @Override
    default ChannelType getType() {
        return ChannelType.SERVER_STAGE_VOICE_CHANNEL;
    }

    /**
     * Gets the topic of this.
     *
     * @return The topic of this channel.
     */
    Optional<String> getTopic();
}

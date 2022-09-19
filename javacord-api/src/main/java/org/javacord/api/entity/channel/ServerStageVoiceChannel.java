package org.javacord.api.entity.channel;

import org.javacord.api.listener.channel.server.voice.ServerStageVoiceChannelAttachableListenerManager;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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

    @Override
    default Optional<ServerStageVoiceChannel> getCurrentCachedInstance() {
        return getApi().getServerById(getServer().getId()).flatMap(server -> server.getChannelById(getId()))
                .flatMap(Channel::asServerStageVoiceChannel);
    }

    @Override
    default CompletableFuture<ServerStageVoiceChannel> getLatestInstance() {
        Optional<ServerStageVoiceChannel> currentCachedInstance = getCurrentCachedInstance();
        if (currentCachedInstance.isPresent()) {
            return CompletableFuture.completedFuture(currentCachedInstance.get());
        } else {
            CompletableFuture<ServerStageVoiceChannel> result = new CompletableFuture<>();
            result.completeExceptionally(new NoSuchElementException());
            return result;
        }
    }

}

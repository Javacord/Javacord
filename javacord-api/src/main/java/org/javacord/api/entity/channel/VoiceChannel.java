package org.javacord.api.entity.channel;

import org.javacord.api.listener.channel.VoiceChannelAttachableListenerManager;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a voice channel.
 */
public interface VoiceChannel extends Channel, VoiceChannelAttachableListenerManager {

    @Override
    default Optional<? extends VoiceChannel> getCurrentCachedInstance() {
        return getApi().getVoiceChannelById(getId());
    }

    @Override
    default CompletableFuture<? extends VoiceChannel> getLatestInstance() {
        Optional<? extends VoiceChannel> currentCachedInstance = getCurrentCachedInstance();
        if (currentCachedInstance.isPresent()) {
            return CompletableFuture.completedFuture(currentCachedInstance.get());
        } else {
            CompletableFuture<? extends VoiceChannel> result = new CompletableFuture<>();
            result.completeExceptionally(new NoSuchElementException());
            return result;
        }
    }

}

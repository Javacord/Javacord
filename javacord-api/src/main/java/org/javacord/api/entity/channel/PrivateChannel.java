package org.javacord.api.entity.channel;

import org.javacord.api.entity.user.User;
import org.javacord.api.listener.channel.user.PrivateChannelAttachableListenerManager;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a private channel.
 * Every conversation between two users takes place in a private channel.
 */
public interface PrivateChannel extends TextChannel, VoiceChannel, PrivateChannelAttachableListenerManager {

    @Override
    default ChannelType getType() {
        return ChannelType.PRIVATE_CHANNEL;
    }

    /**
     * Gets the recipient of the private channel.
     * A private channel always consists of yourself and one other user.
     *
     * @return The recipient of the private channel.
     */
    User getRecipient();

    @Override
    default Optional<PrivateChannel> getCurrentCachedInstance() {
        return getApi().getCachedUserById(getRecipient().getId())
                .flatMap(User::getPrivateChannel)
                .filter(privateChannel -> privateChannel.getId() == getId());
    }

    @Override
    default CompletableFuture<PrivateChannel> getLatestInstance() {
        Optional<PrivateChannel> currentCachedInstance = getCurrentCachedInstance();
        if (currentCachedInstance.isPresent()) {
            return CompletableFuture.completedFuture(currentCachedInstance.get());
        } else {
            CompletableFuture<PrivateChannel> result = new CompletableFuture<>();
            result.completeExceptionally(new NoSuchElementException());
            return result;
        }
    }

}

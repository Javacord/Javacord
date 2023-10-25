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
     * The user may be missing as discord doesn't always send the required information.
     *
     * @return The recipient of the private channel.
     */
    Optional<User> getRecipient();

    /**
     * Gets the id of the recipient of the private channel.
     * A private channel always consists of yourself and one other user.
     * The id may be missing as discord doesn't always send the required information.
     *
     * @return The id of the recipient of the private channel.
     */
    Optional<Long> getRecipientId();

    @Override
    default Optional<PrivateChannel> getCurrentCachedInstance() {
        return getApi().getPrivateChannelById(getId());
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

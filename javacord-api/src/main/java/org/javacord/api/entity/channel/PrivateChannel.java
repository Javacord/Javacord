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
    default boolean canWrite(User user) {
        return user.isYourself() || getRecipient()
                .map(recipient -> recipient.equals(user)).orElse(false);
    }

    @Override
    default boolean canUseExternalEmojis(User user) {
        return canWrite(user);
    }

    @Override
    default boolean canEmbedLinks(User user) {
        return canWrite(user);
    }

    @Override
    default boolean canReadMessageHistory(User user) {
        return canSee(user);
    }

    @Override
    default boolean canUseTts(User user) {
        return canWrite(user);
    }

    @Override
    default boolean canAttachFiles(User user) {
        return user.isYourself() || getRecipient()
                .map(recipient -> recipient.equals(user)).orElse(false);
    }

    @Override
    default boolean canAddNewReactions(User user) {
        return user.isYourself() || getRecipient()
                .map(recipient -> recipient.equals(user)).orElse(false);
    }

    @Override
    default boolean canManageMessages(User user) {
        return canSee(user);
    }

    @Override
    default boolean canMentionEveryone(User user) {
        return canSee(user);
    }

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

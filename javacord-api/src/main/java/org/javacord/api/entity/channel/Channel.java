package org.javacord.api.entity.channel;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.UpdatableFromCache;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.user.User;
import org.javacord.api.listener.channel.ChannelAttachableListenerManager;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * The class represents a channel.
 */
public interface Channel extends DiscordEntity, UpdatableFromCache, ChannelAttachableListenerManager {

    /**
     * Gets the type of the channel.
     *
     * @return The type of the channel.
     */
    ChannelType getType();

    /**
     * Gets the channel as group channel.
     *
     * @return The channel as group channel.
     */
    default Optional<GroupChannel> asGroupChannel() {
        if (this instanceof GroupChannel) {
            return Optional.of((GroupChannel) this);
        }
        return Optional.empty();
    }

    /**
     * Gets the channel as private channel.
     *
     * @return The channel as private channel.
     */
    default Optional<PrivateChannel> asPrivateChannel() {
        if (this instanceof PrivateChannel) {
            return Optional.of((PrivateChannel) this);
        }
        return Optional.empty();
    }

    /**
     * Gets the channel as server channel.
     *
     * @return The channel as server channel.
     */
    default Optional<ServerChannel> asServerChannel() {
        if (this instanceof ServerChannel) {
            return Optional.of((ServerChannel) this);
        }
        return Optional.empty();
    }

    /**
     * Gets the channel as channel category.
     *
     * @return The channel as channel category.
     */
    default Optional<ChannelCategory> asChannelCategory() {
        if (this instanceof ChannelCategory) {
            return Optional.of((ChannelCategory) this);
        }
        return Optional.empty();
    }

    /**
     * Gets the channel as categorizable.
     *
     * @return The channel as categorizable.
     */
    default Optional<Categorizable> asCategorizable() {
        if (this instanceof Categorizable) {
            return Optional.of((Categorizable) this);
        }
        return Optional.empty();
    }

    /**
     * Gets the channel as server text channel.
     *
     * @return The channel as server text channel.
     */
    default Optional<ServerTextChannel> asServerTextChannel() {
        if (this instanceof ServerTextChannel) {
            return Optional.of((ServerTextChannel) this);
        }
        return Optional.empty();
    }

    /**
     * Gets the channel as server voice channel.
     *
     * @return The channel as server voice channel.
     */
    default Optional<ServerVoiceChannel> asServerVoiceChannel() {
        if (this instanceof ServerVoiceChannel) {
            return Optional.of((ServerVoiceChannel) this);
        }
        return Optional.empty();
    }

    /**
     * Gets the channel as text channel.
     *
     * @return The channel as text channel.
     */
    default Optional<TextChannel> asTextChannel() {
        if (this instanceof TextChannel) {
            return Optional.of((TextChannel) this);
        }
        return Optional.empty();
    }

    /**
     * Gets the channel as voice channel.
     *
     * @return The channel as voice channel.
     */
    default Optional<VoiceChannel> asVoiceChannel() {
        if (this instanceof VoiceChannel) {
            return Optional.of((VoiceChannel) this);
        }
        return Optional.empty();
    }


    /**
     * Checks if the given user can see this channel.
     * In private chats (private channel or group channel) this always returns <code>true</code> if the user is
     * part of the chat.
     *
     * @param user The user to check.
     * @return Whether the given user can see this channel or not.
     */
    default boolean canSee(User user) {
        Optional<PrivateChannel> privateChannel = asPrivateChannel();
        if (privateChannel.isPresent()) {
            return user.isYourself() || privateChannel.get().getRecipient() == user;
        }
        Optional<GroupChannel> groupChannel = asGroupChannel();
        if (groupChannel.isPresent()) {
            return user.isYourself() || groupChannel.get().getMembers().contains(user);
        }
        Optional<ServerChannel> severChannel = asServerChannel();
        return !severChannel.isPresent()
                || severChannel.get().hasAnyPermission(user,
                                                       PermissionType.ADMINISTRATOR,
                                                       PermissionType.READ_MESSAGES);
    }

    /**
     * Checks if the user of the connected account can see this channel.
     * In private chats (private channel or group channel) this always returns {@code true} if the user is
     * part of the chat.
     *
     * @return Whether the user of the connected account can see this channel or not.
     */
    default boolean canYouSee() {
        return canSee(getApi().getYourself());
    }

    @Override
    default Optional<? extends Channel> getCurrentCachedInstance() {
        return getApi().getChannelById(getId());
    }

    @Override
    default CompletableFuture<? extends Channel> getLatestInstance() {
        Optional<? extends Channel> currentCachedInstance = getCurrentCachedInstance();
        if (currentCachedInstance.isPresent()) {
            return CompletableFuture.completedFuture(currentCachedInstance.get());
        } else {
            CompletableFuture<? extends Channel> result = new CompletableFuture<>();
            result.completeExceptionally(new NoSuchElementException());
            return result;
        }
    }

}

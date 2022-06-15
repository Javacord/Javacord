package org.javacord.api.entity.channel;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.UpdatableFromCache;
import org.javacord.api.entity.channel.internal.ChannelSpecialization;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.user.User;
import org.javacord.api.listener.channel.ChannelAttachableListenerManager;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * The class represents a channel.
 */
public interface Channel extends DiscordEntity, UpdatableFromCache, ChannelAttachableListenerManager,
        ChannelSpecialization {

    /**
     * Gets the type of the channel.
     *
     * @return The type of the channel.
     */
    ChannelType getType();

    /**
     * Gets the channel as private channel.
     *
     * @return The channel as private channel.
     */
    default Optional<PrivateChannel> asPrivateChannel() {
        return as(PrivateChannel.class);
    }

    /**
     * Gets the channel as server channel.
     *
     * @return The channel as server channel.
     */
    default Optional<ServerChannel> asServerChannel() {
        return as(ServerChannel.class);
    }

    /**
     * Gets the channel as regular server channel.
     *
     * @return The channel as server text channel.
     */
    default Optional<RegularServerChannel> asRegularServerChannel() {
        return as(RegularServerChannel.class);
    }

    /**
     * Gets the channel as channel category.
     *
     * @return The channel as channel category.
     */
    default Optional<ChannelCategory> asChannelCategory() {
        return as(ChannelCategory.class);
    }

    /**
     * Gets the channel as categorizable.
     *
     * @return The channel as categorizable.
     */
    default Optional<Categorizable> asCategorizable() {
        return as(Categorizable.class);
    }

    /**
     * Gets the channel as server text channel.
     *
     * @return The channel as server text channel.
     */
    default Optional<ServerTextChannel> asServerTextChannel() {
        return as(ServerTextChannel.class);
    }

    /**
     * Gets the channel as textable regular server channel.
     *
     * @return The channel as textable regular server channel.
     */
    default Optional<TextableRegularServerChannel> asTextableRegularServerChannel() {
        return as(TextableRegularServerChannel.class);
    }

    /**
     * Gets the channel as server forum channel.
     *
     * @return The channel as server forum channel.
     */
    default Optional<ServerForumChannel> asServerForumChannel() {
        return as(ServerForumChannel.class);
    }

    /**
     * Gets the channel as server voice channel.
     *
     * @return The channel as server voice channel.
     */
    default Optional<ServerVoiceChannel> asServerVoiceChannel() {
        return as(ServerVoiceChannel.class);
    }

    /**
     * Gets the channel as server stage voice channel.
     *
     * @return The channel as server stage voice channel.
     */
    default Optional<ServerStageVoiceChannel> asServerStageVoiceChannel() {
        return as(ServerStageVoiceChannel.class);
    }

    /**
     * Gets the channel as text channel.
     *
     * @return The channel as text channel.
     */
    default Optional<TextChannel> asTextChannel() {
        return as(TextChannel.class);
    }

    /**
     * Gets the channel as voice channel.
     *
     * @return The channel as voice channel.
     */
    default Optional<VoiceChannel> asVoiceChannel() {
        return as(VoiceChannel.class);
    }

    /**
     * Gets the channel as server thread channel.
     *
     * @return The channel as server thread channel.
     */
    default Optional<ServerThreadChannel> asServerThreadChannel() {
        return as(ServerThreadChannel.class);
    }

    /**
     * Checks if the given user can see this channel.
     * In private channels this always returns <code>true</code> if the user is  part of the chat.
     *
     * @param user The user to check.
     * @return Whether the given user can see this channel or not.
     */
    default boolean canSee(User user) {
        Optional<PrivateChannel> privateChannel = asPrivateChannel();
        if (privateChannel.isPresent()) {
            return user.isYourself() || privateChannel.get().getRecipient()
                    .map(recipient -> recipient.equals(user)).orElse(false);
        }
        Optional<RegularServerChannel> severChannel = asRegularServerChannel();
        return !severChannel.isPresent()
                || severChannel.get().hasAnyPermission(user,
                                                       PermissionType.ADMINISTRATOR,
                                                       PermissionType.VIEW_CHANNEL);
    }

    /**
     * Checks if the user of the connected account can see this channel.
     * In private channels this always returns {@code true} if the user is part of the chat.
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

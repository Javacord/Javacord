package de.btobastian.javacord.entity.channel;

import de.btobastian.javacord.entity.DiscordEntity;
import de.btobastian.javacord.entity.UpdatableFromCache;
import de.btobastian.javacord.entity.permission.PermissionType;
import de.btobastian.javacord.entity.user.User;
import de.btobastian.javacord.listener.ChannelAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.util.event.ListenerManager;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * The class represents a channel.
 */
public interface Channel extends DiscordEntity, UpdatableFromCache {

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

    /**
     * Adds a listener that implements one or more {@code ChannelAttachableListener}s.
     * Adding a listener multiple times will only add it once
     * and return the same listener managers on each invocation.
     * The order of invocation is according to first addition.
     *
     * @param listener The listener to add.
     * @param <T> The type of the listener.
     * @return The managers for the added listener.
     */
    <T extends ChannelAttachableListener & ObjectAttachableListener> Collection<ListenerManager<T>>
    addChannelAttachableListener(T listener);

    /**
     * Removes a listener that implements one or more {@code ChannelAttachableListener}s.
     *
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    <T extends ChannelAttachableListener & ObjectAttachableListener> void removeChannelAttachableListener(T listener);

    /**
     * Gets a map with all registered listeners that implement one or more {@code ChannelAttachableListener}s and
     * their assigned listener classes they listen to.
     *
     * @param <T> The type of the listeners.
     * @return A map with all registered listeners that implement one or more {@code ChannelAttachableListener}s
     * and their assigned listener classes they listen to.
     */
    <T extends ChannelAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
    getChannelAttachableListeners();

    /**
     * Removes a listener from this channel.
     *
     * @param listenerClass The listener class.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    <T extends ChannelAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener);

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

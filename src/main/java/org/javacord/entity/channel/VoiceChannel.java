package org.javacord.entity.channel;

import org.javacord.entity.permission.PermissionType;
import org.javacord.entity.user.User;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.VoiceChannelAttachableListener;
import org.javacord.util.event.ListenerManager;
import org.javacord.entity.user.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a voice channel.
 */
public interface VoiceChannel extends Channel {

    /**
     * Checks if the given user can connect to the voice channel.
     * In private chats (private channel or group channel) this always returns <code>true</code> if the user is
     * part of the chat.
     *
     * @param user The user to check.
     * @return Whether the given user can connect or not.
     */
    default boolean canConnect(User user) {
        if (!canSee(user)) {
            return false;
        }
        Optional<ServerTextChannel> severTextChannel = asServerTextChannel();
        return !severTextChannel.isPresent()
               || severTextChannel.get().hasAnyPermission(user,
                                                          PermissionType.ADMINISTRATOR,
                                                          PermissionType.VOICE_CONNECT);
    }

    /**
     * Checks if the user of the connected account can connect to the voice channel.
     * In private chats (private channel or group channel) this always returns {@code true} if the user is
     * part of the chat.
     *
     * @return Whether the user of the connected account can connect or not.
     */
    default boolean canYouConnect() {
        return canConnect(getApi().getYourself());
    }

    /**
     * Checks if the given user can mute other users in this voice channel.
     * In private chats (private channel or group channel) this always returns <code>false</code>.
     *
     * @param user The user to check.
     * @return Whether the given user can mute other users or not.
     */
    default boolean canMuteUsers(User user) {
        if (!canConnect(user) || getType() == ChannelType.PRIVATE_CHANNEL || getType() == ChannelType.GROUP_CHANNEL) {
            return false;
        }
        Optional<ServerVoiceChannel> serverVoiceChannel = asServerVoiceChannel();
        return !serverVoiceChannel.isPresent()
               || serverVoiceChannel.get().hasAnyPermission(user,
                                                            PermissionType.ADMINISTRATOR,
                                                            PermissionType.VOICE_MUTE_MEMBERS);
    }

    /**
     * Checks if the user of the connected account can mute other users in this voice channel.
     * In private chats (private channel or group channel) this always returns {@code false}.
     *
     * @return Whether the user of the connected account can mute other users or not.
     */
    default boolean canYouMuteUsers() {
        return canMuteUsers(getApi().getYourself());
    }

    /**
     * Adds a listener that implements one or more {@code VoiceChannelAttachableListener}s.
     * Adding a listener multiple times will only add it once
     * and return the same listener managers on each invocation.
     * The order of invocation is according to first addition.
     *
     * @param listener The listener to add.
     * @param <T> The type of the listener.
     * @return The managers for the added listener.
     */
    <T extends VoiceChannelAttachableListener & ObjectAttachableListener>
    Collection<ListenerManager<? extends VoiceChannelAttachableListener>> addVoiceChannelAttachableListener(T listener);

    /**
     * Removes a listener that implements one or more {@code VoiceChannelAttachableListener}s.
     *
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    <T extends VoiceChannelAttachableListener & ObjectAttachableListener> void removeVoiceChannelAttachableListener(
            T listener);

    /**
     * Gets a map with all registered listeners that implement one or more {@code VoiceChannelAttachableListener}s and
     * their assigned listener classes they listen to.
     *
     * @param <T> The type of the listeners.
     * @return A map with all registered listeners that implement one or more {@code VoiceChannelAttachableListener}s
     * and their assigned listener classes they listen to.
     */
    <T extends VoiceChannelAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
    getVoiceChannelAttachableListeners();

    /**
     * Removes a listener from this voice channel.
     *
     * @param listenerClass The listener class.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    <T extends VoiceChannelAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener);

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

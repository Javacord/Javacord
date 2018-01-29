package de.btobastian.javacord.entities.channels;

import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.permissions.PermissionType;
import de.btobastian.javacord.listeners.ChannelAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.VoiceChannelAttachableListener;
import de.btobastian.javacord.utils.ClassHelper;
import de.btobastian.javacord.utils.ListenerManager;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @SuppressWarnings("unchecked")
    default <T extends VoiceChannelAttachableListener & ObjectAttachableListener>
    Collection<ListenerManager<? extends VoiceChannelAttachableListener>>
    addVoiceChannelAttachableListener(T listener) {
        return ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(VoiceChannelAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .flatMap(listenerClass -> {
                    if (ChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        return addChannelAttachableListener(
                                (ChannelAttachableListener & ObjectAttachableListener) listener).stream();
                    } else {
                        return Stream.of(((ImplDiscordApi) getApi()).addObjectListener(VoiceChannel.class, getId(),
                                                                                       listenerClass, listener));
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Removes a listener that implements one or more {@code VoiceChannelAttachableListener}s.
     *
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    @SuppressWarnings("unchecked")
    default <T extends VoiceChannelAttachableListener & ObjectAttachableListener> void
    removeVoiceChannelAttachableListener(T listener) {
        ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(VoiceChannelAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .forEach(listenerClass -> {
                    if (ChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        removeChannelAttachableListener(
                                (ChannelAttachableListener & ObjectAttachableListener) listener);
                    } else {
                        ((ImplDiscordApi) getApi()).removeObjectListener(VoiceChannel.class, getId(),
                                                                         listenerClass, listener);
                    }
                });
    }

    /**
     * Gets a map with all registered listeners that implement one or more {@code VoiceChannelAttachableListener}s and
     * their assigned listener classes they listen to.
     *
     * @param <T> The type of the listeners.
     * @return A map with all registered listeners that implement one or more {@code VoiceChannelAttachableListener}s
     * and their assigned listener classes they listen to.
     */
    default <T extends VoiceChannelAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
    getVoiceChannelAttachableListeners() {
        Map<T, List<Class<T>>> voiceChannelListeners =
                ((ImplDiscordApi) getApi()).getObjectListeners(VoiceChannel.class, getId());
        getChannelAttachableListeners().forEach((listener, listenerClasses) -> voiceChannelListeners
                .merge((T) listener,
                       (List<Class<T>>) (Object) listenerClasses,
                       (listenerClasses1, listenerClasses2) -> {
                           listenerClasses1.addAll(listenerClasses2);
                           return listenerClasses1;
                       }));
        return voiceChannelListeners;
    }

    /**
     * Removes a listener from this voice channel.
     *
     * @param listenerClass The listener class.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    default <T extends VoiceChannelAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener) {
        ((ImplDiscordApi) getApi()).removeObjectListener(VoiceChannel.class, getId(), listenerClass, listener);
    }

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

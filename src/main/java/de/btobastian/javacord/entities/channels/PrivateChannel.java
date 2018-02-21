package de.btobastian.javacord.entities.channels;

import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.listeners.ChannelAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.TextChannelAttachableListener;
import de.btobastian.javacord.listeners.VoiceChannelAttachableListener;
import de.btobastian.javacord.listeners.user.channel.PrivateChannelAttachableListener;
import de.btobastian.javacord.listeners.user.channel.PrivateChannelDeleteListener;
import de.btobastian.javacord.utils.ClassHelper;
import de.btobastian.javacord.utils.ListenerManager;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class represents a private channel.
 * Every conversation between two users takes place in a private channel.
 */
public interface PrivateChannel extends TextChannel, VoiceChannel {

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

    /**
     * Adds a listener, which listens to this channel being deleted.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<PrivateChannelDeleteListener> addPrivateChannelDeleteListener(
            PrivateChannelDeleteListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                PrivateChannel.class, getId(), PrivateChannelDeleteListener.class, listener);
    }

    /**
     * Gets a list with all registered private channel delete listeners.
     *
     * @return A list with all registered private channel delete listeners.
     */
    default List<PrivateChannelDeleteListener> getPrivateChannelDeleteListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                PrivateChannel.class, getId(), PrivateChannelDeleteListener.class);
    }

    /**
     * Adds a listener that implements one or more {@code PrivateChannelAttachableListener}s.
     * Adding a listener multiple times will only add it once
     * and return the same listener managers on each invocation.
     * The order of invocation is according to first addition.
     *
     * @param listener The listener to add.
     * @param <T> The type of the listener.
     * @return The managers for the added listener.
     */
    @SuppressWarnings("unchecked")
    default <T extends PrivateChannelAttachableListener & ObjectAttachableListener>
    Collection<ListenerManager<? extends PrivateChannelAttachableListener>> addPrivateChannelAttachableListener(
            T listener) {
        return ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(PrivateChannelAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .flatMap(listenerClass -> {
                    if (ChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        return addChannelAttachableListener(
                                (ChannelAttachableListener & ObjectAttachableListener) listener).stream();
                    } else if (TextChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        return addTextChannelAttachableListener(
                                (TextChannelAttachableListener & ObjectAttachableListener) listener).stream();
                    } else if (VoiceChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        return addVoiceChannelAttachableListener(
                                (VoiceChannelAttachableListener & ObjectAttachableListener) listener).stream();
                    } else {
                        return Stream.of(((ImplDiscordApi) getApi()).addObjectListener(PrivateChannel.class, getId(),
                                                                                       listenerClass, listener));
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Removes a listener that implements one or more {@code PrivateChannelAttachableListener}s.
     *
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    @SuppressWarnings("unchecked")
    default <T extends PrivateChannelAttachableListener & ObjectAttachableListener> void
    removePrivateChannelAttachableListener(T listener) {
        ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(PrivateChannelAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .forEach(listenerClass -> {
                    if (ChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        removeChannelAttachableListener(
                                (ChannelAttachableListener & ObjectAttachableListener) listener);
                    } else if (TextChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        removeTextChannelAttachableListener(
                                (TextChannelAttachableListener & ObjectAttachableListener) listener);
                    } else if (VoiceChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        removeVoiceChannelAttachableListener(
                                (VoiceChannelAttachableListener & ObjectAttachableListener) listener);
                    } else {
                        ((ImplDiscordApi) getApi()).removeObjectListener(PrivateChannel.class, getId(),
                                                                         listenerClass, listener);
                    }
                });
    }

    /**
     * Gets a map with all registered listeners that implement one or more {@code PrivateChannelAttachableListener}s and
     * their assigned listener classes they listen to.
     *
     * @param <T> The type of the listeners.
     * @return A map with all registered listeners that implement one or more {@code PrivateChannelAttachableListener}s
     * and their assigned listener classes they listen to.
     */
    @SuppressWarnings("unchecked")
    default <T extends PrivateChannelAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
    getPrivateChannelAttachableListeners() {
        Map<T, List<Class<T>>> privateChannelListeners =
                ((ImplDiscordApi) getApi()).getObjectListeners(PrivateChannel.class, getId());
        getTextChannelAttachableListeners().forEach((listener, listenerClasses) -> privateChannelListeners
                .merge((T) listener,
                       (List<Class<T>>) (Object) listenerClasses,
                       (listenerClasses1, listenerClasses2) -> {
                           listenerClasses1.addAll(listenerClasses2);
                           return listenerClasses1;
                       }));
        getVoiceChannelAttachableListeners().forEach((listener, listenerClasses) -> privateChannelListeners
                .merge((T) listener,
                       (List<Class<T>>) (Object) listenerClasses,
                       (listenerClasses1, listenerClasses2) -> {
                           listenerClasses1.addAll(listenerClasses2);
                           return listenerClasses1;
                       }));
        getChannelAttachableListeners().forEach((listener, listenerClasses) -> privateChannelListeners
                .merge((T) listener,
                       (List<Class<T>>) (Object) listenerClasses,
                       (listenerClasses1, listenerClasses2) -> {
                           listenerClasses1.addAll(listenerClasses2);
                           return listenerClasses1;
                       }));
        return privateChannelListeners;
    }

    /**
     * Removes a listener from this private channel.
     *
     * @param listenerClass The listener class.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    default <T extends PrivateChannelAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener) {
        ((ImplDiscordApi) getApi()).removeObjectListener(PrivateChannel.class, getId(), listenerClass, listener);
    }

}

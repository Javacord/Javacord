package org.javacord.entity.channel;

import org.javacord.entity.user.User;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.channel.user.PrivateChannelAttachableListener;
import org.javacord.listener.channel.user.PrivateChannelDeleteListener;
import org.javacord.util.event.ListenerManager;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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
    ListenerManager<PrivateChannelDeleteListener> addPrivateChannelDeleteListener(
            PrivateChannelDeleteListener listener);

    /**
     * Gets a list with all registered private channel delete listeners.
     *
     * @return A list with all registered private channel delete listeners.
     */
    List<PrivateChannelDeleteListener> getPrivateChannelDeleteListeners();

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
    <T extends PrivateChannelAttachableListener & ObjectAttachableListener>
    Collection<ListenerManager<? extends PrivateChannelAttachableListener>> addPrivateChannelAttachableListener(
            T listener);

    /**
     * Removes a listener that implements one or more {@code PrivateChannelAttachableListener}s.
     *
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    <T extends PrivateChannelAttachableListener & ObjectAttachableListener> void removePrivateChannelAttachableListener(
            T listener);

    /**
     * Gets a map with all registered listeners that implement one or more {@code PrivateChannelAttachableListener}s and
     * their assigned listener classes they listen to.
     *
     * @param <T> The type of the listeners.
     * @return A map with all registered listeners that implement one or more {@code PrivateChannelAttachableListener}s
     * and their assigned listener classes they listen to.
     */
    <T extends PrivateChannelAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
    getPrivateChannelAttachableListeners();

    /**
     * Removes a listener from this private channel.
     *
     * @param listenerClass The listener class.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    <T extends PrivateChannelAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener);

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

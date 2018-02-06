package de.btobastian.javacord.entities.channels;

import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.listeners.user.channel.PrivateChannelDeleteListener;
import de.btobastian.javacord.utils.ListenerManager;

import java.util.List;

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
     * Removes a listener from this private channel.
     *
     * @param listenerClass The listener class.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    default <T> void removeListener(Class<T> listenerClass, T listener) {
        ((ImplDiscordApi) getApi()).removeObjectListener(PrivateChannel.class, getId(), listenerClass, listener);
    }

}

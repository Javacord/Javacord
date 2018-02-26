package de.btobastian.javacord.listeners.message;

import de.btobastian.javacord.events.message.ChannelPinsUpdateEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.TextChannelAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;

/**
 * This listener listens to channel pin updates.
 */
@FunctionalInterface
public interface ChannelPinsUpdateListener extends TextChannelAttachableListener, ServerAttachableListener,
                                                   GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a channel's pins update.
     *
     * @param event The event.
     */
    void onChannelPinsUpdate(ChannelPinsUpdateEvent event);
}

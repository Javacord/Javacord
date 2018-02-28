package de.btobastian.javacord.listener.message;

import de.btobastian.javacord.event.message.ChannelPinsUpdateEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.TextChannelAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;

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

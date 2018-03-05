package org.javacord.listener.message;

import org.javacord.event.message.ChannelPinsUpdateEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.TextChannelAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;

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

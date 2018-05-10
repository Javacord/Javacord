package org.javacord.api.listener.message;

import org.javacord.api.event.message.ChannelPinsUpdateEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.TextChannelAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

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

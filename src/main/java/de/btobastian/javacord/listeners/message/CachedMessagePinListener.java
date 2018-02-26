package de.btobastian.javacord.listeners.message;

import de.btobastian.javacord.events.message.CachedMessagePinEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.TextChannelAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;

/**
 * This listener listens to message pins of <b>cached</b> messages.
 */
@FunctionalInterface
public interface CachedMessagePinListener extends ServerAttachableListener, TextChannelAttachableListener,
        MessageAttachableListener, GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a cached message gets pinned.
     *
     * @param event The event.
     */
    void onCachedMessagePin(CachedMessagePinEvent event);

}

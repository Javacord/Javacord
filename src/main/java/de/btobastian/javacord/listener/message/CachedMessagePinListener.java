package de.btobastian.javacord.listener.message;

import de.btobastian.javacord.event.message.CachedMessagePinEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.TextChannelAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;

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

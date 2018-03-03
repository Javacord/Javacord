package org.javacord.listener.message;

import org.javacord.event.message.CachedMessagePinEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.TextChannelAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;
import org.javacord.event.message.CachedMessagePinEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.TextChannelAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;

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

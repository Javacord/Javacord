package org.javacord.listener.message;

import org.javacord.event.message.CachedMessageUnpinEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.TextChannelAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;
import org.javacord.event.message.CachedMessageUnpinEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.TextChannelAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;

/**
 * This listener listens to message unpins of <b>cached</b> messages.
 */
@FunctionalInterface
public interface CachedMessageUnpinListener extends ServerAttachableListener, TextChannelAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a cached message gets unpinned.
     *
     * @param event The event.
     */
    void onCachedMessageUnpin(CachedMessageUnpinEvent event);

}

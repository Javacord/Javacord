package de.btobastian.javacord.listener.message;

import de.btobastian.javacord.event.message.CachedMessageUnpinEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.TextChannelAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;

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

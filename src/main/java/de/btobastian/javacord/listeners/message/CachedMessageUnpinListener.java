package de.btobastian.javacord.listeners.message;

import de.btobastian.javacord.events.message.CachedMessageUnpinEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.TextChannelAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;

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

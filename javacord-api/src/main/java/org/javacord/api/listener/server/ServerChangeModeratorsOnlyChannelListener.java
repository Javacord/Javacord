package org.javacord.api.listener.server;

import org.javacord.api.event.server.ServerChangeModeratorsOnlyChannelEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;

/**
 * This listener listens to server moderators-only channel changes.
 */
@FunctionalInterface
public interface ServerChangeModeratorsOnlyChannelListener extends ServerAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a server's moderators-only channel changed.
     *
     * @param event The event.
     */
    void onServerChangeModeratorsOnlyChannel(ServerChangeModeratorsOnlyChannelEvent event);
}

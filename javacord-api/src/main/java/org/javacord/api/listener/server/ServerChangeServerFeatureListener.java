package org.javacord.api.listener.server;

import org.javacord.api.event.server.ServerChangeServerFeaturesEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;

/**
 * This listener listens to server feature changes.
 */
@FunctionalInterface
public interface ServerChangeServerFeatureListener extends ServerAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a server's feature changed.
     *
     * @param event The event.
     */
    void onServerChangeServerFeature(ServerChangeServerFeaturesEvent event);
}

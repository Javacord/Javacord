package org.javacord.api.listener.server;

import org.javacord.api.event.server.ApplicationCommandPermissionsUpdateEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;

/**
 * This listener listens to application command permissions updates.
 */
@FunctionalInterface
public interface ApplicationCommandPermissionsUpdateListener extends ServerAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time an application command has its permissions updated.
     *
     * @param event The event.
     */
    void onApplicationCommandPermissionsUpdate(ApplicationCommandPermissionsUpdateEvent event);
}

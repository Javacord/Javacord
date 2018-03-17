package org.javacord.api.listener.connection;

import org.javacord.api.event.connection.ResumeEvent;
import org.javacord.api.listener.GloballyAttachableListener;

/**
 * This listener listens to resumed sessions.
 * Resuming a session, means no events were missed and all objects are still valid.
 */
@FunctionalInterface
public interface ResumeListener extends GloballyAttachableListener {

    /**
     * This method is called every time a connection is resumed.
     *
     * @param event The event.
     */
    void onResume(ResumeEvent event);

}

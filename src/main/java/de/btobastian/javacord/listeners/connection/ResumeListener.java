package de.btobastian.javacord.listeners.connection;

import de.btobastian.javacord.events.connection.ResumeEvent;

/**
 * This listener listens to resumed sessions.
 * Resuming a session, means no events were missed and all objects are still valid.
 */
@FunctionalInterface
public interface ResumeListener {

    /**
     * This method is called every time a connection is resumed.
     *
     * @param event The event.
     */
    void onResume(ResumeEvent event);

}

package org.javacord.event.connection;

import org.javacord.event.Event;

/**
 * A resume event.
 * Resuming a session, means no events were missed and all objects are still valid.
 */
public interface ResumeEvent extends Event {
}

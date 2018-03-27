package org.javacord.api.event.connection;

import org.javacord.api.event.Event;

/**
 * A resume event.
 * Resuming a session, means no events were missed and all objects are still valid.
 */
public interface ResumeEvent extends Event {
}

package org.javacord.event.connection;

import org.javacord.event.Event;

/**
 * A reconnect event.
 * Reconnecting a session, means that it's likely that events were lost and therefore all objects were replaced.
 */
public interface ReconnectEvent extends Event {
}

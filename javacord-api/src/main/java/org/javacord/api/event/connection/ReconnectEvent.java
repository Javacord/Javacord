package org.javacord.api.event.connection;

import org.javacord.api.event.Event;

/**
 * A reconnect event.
 * Reconnecting a session, means that it's likely that events were lost and therefore all objects were replaced.
 */
public interface ReconnectEvent extends Event {
}

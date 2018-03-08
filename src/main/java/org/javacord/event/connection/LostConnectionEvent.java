package org.javacord.event.connection;

import org.javacord.event.Event;

/**
 * A lost connection event.
 * This event is thrown, when the websocket loses its connection.
 * Don't panic! It's totally normal for the websocket to occasionally lose the connection. In most cases it's possible
 * to resume the session without missing any events!
 */
public interface LostConnectionEvent extends Event {
}

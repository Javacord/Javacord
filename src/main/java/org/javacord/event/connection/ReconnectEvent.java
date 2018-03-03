package org.javacord.event.connection;

import org.javacord.DiscordApi;
import org.javacord.event.Event;

/**
 * A reconnect event.
 * Reconnecting a session, means that it's likely that events were lost and therefore all objects were replaced.
 */
public class ReconnectEvent extends Event {

    /**
     * Creates a new reconnect event.
     *
     * @param api The api instance of the event.
     */
    public ReconnectEvent(DiscordApi api) {
        super(api);
    }

}

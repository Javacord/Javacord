package de.btobastian.javacord.events.connection;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.events.Event;

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

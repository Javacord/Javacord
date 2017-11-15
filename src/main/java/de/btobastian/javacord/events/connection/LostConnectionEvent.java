package de.btobastian.javacord.events.connection;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.events.Event;

/**
 * A lost connection event.
 * This event is thrown, when the websocket loses it's connection.
 * Don't panic! It's totally normal for the websocket to occasionally lose the connection. In most cases it's possible
 * to resume the session without missing any events!
 */
public class LostConnectionEvent extends Event {

    /**
     * Creates a new lost connection event.
     *
     * @param api The api instance of the event.
     */
    public LostConnectionEvent(DiscordApi api) {
        super(api);
    }

}

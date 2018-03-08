package org.javacord.event.connection.impl;

import org.javacord.DiscordApi;
import org.javacord.event.connection.ReconnectEvent;
import org.javacord.event.impl.ImplEvent;

/**
 * The implementation of {@link ReconnectEvent}.
 */
public class ImplReconnectEvent extends ImplEvent implements ReconnectEvent {

    /**
     * Creates a new reconnect event.
     *
     * @param api The api instance of the event.
     */
    public ImplReconnectEvent(DiscordApi api) {
        super(api);
    }

}

package org.javacord.core.event.connection;

import org.javacord.api.DiscordApi;
import org.javacord.api.event.connection.ReconnectEvent;
import org.javacord.core.event.EventImpl;

/**
 * The implementation of {@link ReconnectEvent}.
 */
public class ReconnectEventImpl extends EventImpl implements ReconnectEvent {

    /**
     * Creates a new reconnect event.
     *
     * @param api The api instance of the event.
     */
    public ReconnectEventImpl(DiscordApi api) {
        super(api);
    }

}

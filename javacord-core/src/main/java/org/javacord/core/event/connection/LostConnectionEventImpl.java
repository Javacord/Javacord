package org.javacord.core.event.connection;

import org.javacord.api.DiscordApi;
import org.javacord.api.event.connection.LostConnectionEvent;
import org.javacord.core.event.EventImpl;

/**
 * The implementation of {@link LostConnectionEvent}.
 */
public class LostConnectionEventImpl extends EventImpl implements LostConnectionEvent {

    /**
     * Creates a new lost connection event.
     *
     * @param api The api instance of the event.
     */
    public LostConnectionEventImpl(DiscordApi api) {
        super(api);
    }

}

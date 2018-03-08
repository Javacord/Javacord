package org.javacord.event.connection.impl;

import org.javacord.DiscordApi;
import org.javacord.event.connection.LostConnectionEvent;
import org.javacord.event.impl.ImplEvent;

/**
 * The implementation of {@link LostConnectionEvent}.
 */
public class ImplLostConnectionEvent extends ImplEvent implements LostConnectionEvent {

    /**
     * Creates a new lost connection event.
     *
     * @param api The api instance of the event.
     */
    public ImplLostConnectionEvent(DiscordApi api) {
        super(api);
    }

}

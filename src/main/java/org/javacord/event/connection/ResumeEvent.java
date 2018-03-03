package org.javacord.event.connection;

import org.javacord.DiscordApi;
import org.javacord.event.Event;

/**
 * A resume event.
 * Resuming a session, means no events were missed and all objects are still valid.
 */
public class ResumeEvent extends Event {

    /**
     * Creates a new resume event.
     *
     * @param api The api instance of the event.
     */
    public ResumeEvent(DiscordApi api) {
        super(api);
    }

}

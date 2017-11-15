package de.btobastian.javacord.events.connection;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.events.Event;

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

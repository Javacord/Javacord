package org.javacord.core.event.connection;

import org.javacord.api.DiscordApi;
import org.javacord.api.event.connection.ResumeEvent;
import org.javacord.core.event.EventImpl;

/**
 * The implementation of {@link ResumeEvent}.
 */
public class ResumeEventImpl extends EventImpl implements ResumeEvent {

    /**
     * Creates a new resume event.
     *
     * @param api The api instance of the event.
     */
    public ResumeEventImpl(DiscordApi api) {
        super(api);
    }

}

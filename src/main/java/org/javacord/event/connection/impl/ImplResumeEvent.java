package org.javacord.event.connection.impl;

import org.javacord.DiscordApi;
import org.javacord.event.connection.ResumeEvent;
import org.javacord.event.impl.ImplEvent;

/**
 * The implementation of {@link ResumeEvent}.
 */
public class ImplResumeEvent extends ImplEvent implements ResumeEvent {

    /**
     * Creates a new resume event.
     *
     * @param api The api instance of the event.
     */
    public ImplResumeEvent(DiscordApi api) {
        super(api);
    }

}

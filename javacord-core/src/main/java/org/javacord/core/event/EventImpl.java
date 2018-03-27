package org.javacord.core.event;

import org.javacord.api.DiscordApi;
import org.javacord.api.event.Event;

/**
 * The implementation of {@link Event}.
 */
public abstract class EventImpl implements Event {

    /**
     * The api instance of the event.
     */
    protected final DiscordApi api;

    /**
     * Creates a new event.
     *
     * @param api The api instance of the event.
     */
    public EventImpl(DiscordApi api) {
        this.api = api;
    }

    @Override
    public DiscordApi getApi() {
        return api;
    }

}

package org.javacord.event.impl;

import org.javacord.DiscordApi;
import org.javacord.event.Event;

/**
 * The implementation of {@link Event}.
 */
public abstract class ImplEvent implements Event {

    /**
     * The api instance of the event.
     */
    protected final DiscordApi api;

    /**
     * Creates a new event.
     *
     * @param api The api instance of the event.
     */
    public ImplEvent(DiscordApi api) {
        this.api = api;
    }

    @Override
    public DiscordApi getApi() {
        return api;
    }

}

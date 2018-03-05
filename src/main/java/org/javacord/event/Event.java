package org.javacord.event;

import org.javacord.DiscordApi;

/**
 * The basic event.
 */
public abstract class Event {

    /**
     * The api instance of the event.
     */
    protected final DiscordApi api;

    /**
     * Creates a new event.
     *
     * @param api The api instance of the event.
     */
    public Event(DiscordApi api) {
        this.api = api;
    }

    /**
     * Gets the api instance of the event.
     *
     * @return The api instance of the event.
     */
    public DiscordApi getApi() {
        return api;
    }

}

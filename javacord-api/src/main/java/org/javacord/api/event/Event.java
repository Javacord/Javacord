package org.javacord.api.event;

import org.javacord.api.DiscordApi;

/**
 * The basic event.
 */
public interface Event {

    /**
     * Gets the api instance of the event.
     *
     * @return The api instance of the event.
     */
    DiscordApi getApi();

}

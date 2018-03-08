package org.javacord.event;

import org.javacord.DiscordApi;

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

package de.btobastian.javacord.impl;

import de.btobastian.javacord.api.DiscordAPI;

/**
 * The main class used to get an api instance.
 */
public class Javacord {

    private Javacord() { }
    
    /**
     * Creates a new {@link DiscordAPI} object.
     * 
     * @return The api.
     */
    public static DiscordAPI getApi() {
        return new ImplDiscordAPI();
    }
    
}

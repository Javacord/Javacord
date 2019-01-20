package org.javacord.api.entity;

/**
 * Represents the three different Discord clients.
 */
public enum DiscordClient {

    /**
     * The Discord desktop client.
     *
     * <p>Does not differentiate between operating systems like Windows or Linux.
     */
    DESKTOP("desktop"),

    /**
     * The Discord mobile client.
     *
     * <p>Does not differentiate between the iOS app and the Android app.
     */
    MOBILE("mobile"),

    /**
     * The Discord web client.
     */
    WEB("web");

    private final String name;

    /**
     * Creates a new Discord Client.
     *
     * @param name The name for the client, as it appears in the Discord REST-api.
     */
    DiscordClient(String name) {
        this.name = name;
    }

    /**
     * Gets the name of client, as it appears in the Discord REST-api.
     *
     * @return The name of the client.
     */
    public String getName() {
        return name;
    }
}

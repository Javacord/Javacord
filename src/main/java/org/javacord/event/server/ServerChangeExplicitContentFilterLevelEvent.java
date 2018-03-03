package org.javacord.event.server;

import org.javacord.DiscordApi;
import org.javacord.entity.server.ExplicitContentFilterLevel;
import org.javacord.entity.server.Server;

/**
 * A server change explicit content filter level event.
 */
public class ServerChangeExplicitContentFilterLevelEvent extends ServerEvent {

    /**
     * The new explicit content filter level of the server.
     */
    private final ExplicitContentFilterLevel newExplicitContentFilterLevel;

    /**
     * The old explicit content filter level of the server.
     */
    private final ExplicitContentFilterLevel oldExplicitContentFilterLevel;

    /**
     * Creates a new server change explicit content filter level event.
     *
     * @param api The api instance of the event.
     * @param server The server of the event.
     * @param newExplicitContentFilterLevel The new explicit content filter level of the server.
     * @param oldExplicitContentFilterLevel The old explicit content filter level of the server.
     */
    public ServerChangeExplicitContentFilterLevelEvent(DiscordApi api, Server server, ExplicitContentFilterLevel newExplicitContentFilterLevel, ExplicitContentFilterLevel oldExplicitContentFilterLevel) {
        super(api, server);
        this.newExplicitContentFilterLevel = newExplicitContentFilterLevel;
        this.oldExplicitContentFilterLevel = oldExplicitContentFilterLevel;
    }

    /**
     * Gets the old explicit content filter level of the server.
     *
     * @return The old explicit content filter level of the server.
     */
    public ExplicitContentFilterLevel getOldExplicitContentFilterLevel() {
        return oldExplicitContentFilterLevel;
    }

    /**
     * Gets the new explicit content filter level of the server.
     *
     * @return The new explicit content filter level of the server.
     */
    public ExplicitContentFilterLevel getNewExplicitContentFilterLevel() {
        return newExplicitContentFilterLevel;
    }

}

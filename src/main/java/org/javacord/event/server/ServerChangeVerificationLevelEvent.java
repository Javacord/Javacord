package org.javacord.event.server;

import org.javacord.DiscordApi;
import org.javacord.entity.server.Server;
import org.javacord.entity.server.VerificationLevel;

/**
 * A server change verification level event.
 */
public class ServerChangeVerificationLevelEvent extends ServerEvent {

    /**
     * The new verification level of the server.
     */
    private final VerificationLevel newVerificationLevel;

    /**
     * The old verification level of the server.
     */
    private final VerificationLevel oldVerificationLevel;

    /**
     * Creates a new server change name event.
     *
     * @param api The api instance of the event.
     * @param server The server of the event.
     * @param newVerificationLevel The new verification level of the server.
     * @param oldVerificationLevel The old verification level of the server.
     */
    public ServerChangeVerificationLevelEvent(DiscordApi api, Server server,
                                              VerificationLevel newVerificationLevel, VerificationLevel oldVerificationLevel) {
        super(api, server);
        this.newVerificationLevel = newVerificationLevel;
        this.oldVerificationLevel = oldVerificationLevel;
    }

    /**
     * Gets the old verification level of the server.
     *
     * @return The old verification level of the server.
     */
    public VerificationLevel getOldVerificationLevel() {
        return oldVerificationLevel;
    }

    /**
     * Gets the new verification level of the server.
     *
     * @return The new verification level of the server.
     */
    public VerificationLevel getNewVerificationLevel() {
        return newVerificationLevel;
    }

}

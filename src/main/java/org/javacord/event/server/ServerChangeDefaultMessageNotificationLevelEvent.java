package org.javacord.event.server;

import org.javacord.DiscordApi;
import org.javacord.entity.server.DefaultMessageNotificationLevel;
import org.javacord.entity.server.Server;

/**
 * A server change default message notification level event.
 */
public class ServerChangeDefaultMessageNotificationLevelEvent extends ServerEvent {

    /**
     * The new default message notification level of the server.
     */
    private final DefaultMessageNotificationLevel newDefaultMessageNotificationLevel;

    /**
     * The old default message notification level of the server.
     */
    private final DefaultMessageNotificationLevel oldDefaultMessageNotificationLevel;

    /**
     * Creates a new server change default message notification level event.
     *
     * @param api The api instance of the event.
     * @param server The server of the event.
     * @param newDefaultMessageNotificationLevel The new default message notification level of the server.
     * @param oldDefaultMessageNotificationLevel The old default message notification level of the server.
     */
    public ServerChangeDefaultMessageNotificationLevelEvent(DiscordApi api, Server server,
            DefaultMessageNotificationLevel newDefaultMessageNotificationLevel,
            DefaultMessageNotificationLevel oldDefaultMessageNotificationLevel) {
        super(api, server);
        this.newDefaultMessageNotificationLevel = newDefaultMessageNotificationLevel;
        this.oldDefaultMessageNotificationLevel = oldDefaultMessageNotificationLevel;
    }

    /**
     * Gets the old default message notification level of the server.
     *
     * @return The old default message notification level of the server.
     */
    public DefaultMessageNotificationLevel getOldDefaultMessageNotificationLevel() {
        return oldDefaultMessageNotificationLevel;
    }

    /**
     * Gets the new default message notification level of the server.
     *
     * @return The new default message notification level of the server.
     */
    public DefaultMessageNotificationLevel getNewDefaultMessageNotificationLevel() {
        return newDefaultMessageNotificationLevel;
    }

}

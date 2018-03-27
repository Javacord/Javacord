package org.javacord.core.event.server;

import org.javacord.api.entity.server.DefaultMessageNotificationLevel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.server.ServerChangeDefaultMessageNotificationLevelEvent;

/**
 * The implementation of {@link ServerChangeDefaultMessageNotificationLevelEvent}.
 */
public class ServerChangeDefaultMessageNotificationLevelEventImpl extends ServerEventImpl
        implements ServerChangeDefaultMessageNotificationLevelEvent {

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
     * @param server The server of the event.
     * @param newDefaultMessageNotificationLevel The new default message notification level of the server.
     * @param oldDefaultMessageNotificationLevel The old default message notification level of the server.
     */
    public ServerChangeDefaultMessageNotificationLevelEventImpl(
            Server server, DefaultMessageNotificationLevel newDefaultMessageNotificationLevel,
            DefaultMessageNotificationLevel oldDefaultMessageNotificationLevel) {
        super(server);
        this.newDefaultMessageNotificationLevel = newDefaultMessageNotificationLevel;
        this.oldDefaultMessageNotificationLevel = oldDefaultMessageNotificationLevel;
    }

    @Override
    public DefaultMessageNotificationLevel getOldDefaultMessageNotificationLevel() {
        return oldDefaultMessageNotificationLevel;
    }

    @Override
    public DefaultMessageNotificationLevel getNewDefaultMessageNotificationLevel() {
        return newDefaultMessageNotificationLevel;
    }

}

package org.javacord.event.server.impl;

import org.javacord.entity.server.DefaultMessageNotificationLevel;
import org.javacord.entity.server.Server;
import org.javacord.event.server.ServerChangeDefaultMessageNotificationLevelEvent;

/**
 * The implementation of {@link ServerChangeDefaultMessageNotificationLevelEvent}.
 */
public class ImplServerChangeDefaultMessageNotificationLevelEvent extends ImplServerEvent
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
    public ImplServerChangeDefaultMessageNotificationLevelEvent(
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

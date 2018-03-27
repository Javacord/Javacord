package org.javacord.api.event.server;

import org.javacord.api.entity.server.DefaultMessageNotificationLevel;

/**
 * A server change default message notification level event.
 */
public interface ServerChangeDefaultMessageNotificationLevelEvent extends ServerEvent {

    /**
     * Gets the old default message notification level of the server.
     *
     * @return The old default message notification level of the server.
     */
    DefaultMessageNotificationLevel getOldDefaultMessageNotificationLevel();

    /**
     * Gets the new default message notification level of the server.
     *
     * @return The new default message notification level of the server.
     */
    DefaultMessageNotificationLevel getNewDefaultMessageNotificationLevel();

}

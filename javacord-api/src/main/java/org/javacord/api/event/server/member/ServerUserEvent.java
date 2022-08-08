package org.javacord.api.event.server.member;

import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.ServerEvent;

/**
 * A server user event.
 */
public interface ServerUserEvent extends ServerEvent {
    /**
     * Gets the user of the event.
     *
     * @return The user of the event.
     */
    User getUser();
}

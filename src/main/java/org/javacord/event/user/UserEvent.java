package org.javacord.event.user;

import org.javacord.entity.user.User;
import org.javacord.event.Event;

/**
 * A user event.
 */
public interface UserEvent extends Event {

    /**
     * Gets the user of the event.
     *
     * @return The user of the event.
     */
    User getUser();

}

package org.javacord.api.event.server.member;

import org.javacord.api.entity.activity.Activity;
import org.javacord.api.event.server.ServerEvent;
import org.javacord.api.event.user.OptionalUserEvent;

import java.util.Set;

/**
 * A user change activity event.
 */
public interface ServerMemberChangeActivityEvent extends OptionalUserEvent, ServerEvent {

    /**
     * Gets the old activities of the user.
     *
     * @return The old activities of the user.
     */
    Set<Activity> getOldActivities();

    /**
     * Gets the new activities of the user.
     *
     * @return The new activities of the user.
     */
    Set<Activity> getNewActivities();

}

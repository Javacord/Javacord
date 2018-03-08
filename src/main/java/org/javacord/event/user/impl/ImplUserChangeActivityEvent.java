package org.javacord.event.user.impl;

import org.javacord.entity.activity.Activity;
import org.javacord.entity.user.User;
import org.javacord.event.user.UserChangeActivityEvent;

import java.util.Optional;

/**
 * The implementation of {@link UserChangeActivityEvent}.
 */
public class ImplUserChangeActivityEvent extends ImplUserEvent implements UserChangeActivityEvent {

    /**
     * The new activity of the user.
     */
    private final Activity newActivity;

    /**
     * The old activity of the user.
     */
    private final Activity oldActivity;

    /**
     * Creates a new user change activity event.
     *
     * @param user The user of the event.
     * @param newActivity The new activity of the user.
     * @param oldActivity The old activity of the user.
     */
    public ImplUserChangeActivityEvent(User user, Activity newActivity, Activity oldActivity) {
        super(user);
        this.newActivity = newActivity;
        this.oldActivity = oldActivity;
    }

    @Override
    public Optional<Activity> getOldActivity() {
        return Optional.ofNullable(oldActivity);
    }

    @Override
    public Optional<Activity> getNewActivity() {
        return Optional.ofNullable(newActivity);
    }

}

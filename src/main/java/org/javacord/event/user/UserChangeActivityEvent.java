package org.javacord.event.user;

import org.javacord.DiscordApi;
import org.javacord.entity.activity.Activity;
import org.javacord.entity.user.User;

import java.util.Optional;

/**
 * A user change activity event.
 */
public class UserChangeActivityEvent extends UserEvent {

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
     * @param api The api instance of the event.
     * @param user The user of the event.
     * @param newActivity The new activity of the user.
     * @param oldActivity The old activity of the user.
     */
    public UserChangeActivityEvent(DiscordApi api, User user, Activity newActivity, Activity oldActivity) {
        super(api, user);
        this.newActivity = newActivity;
        this.oldActivity = oldActivity;
    }

    /**
     * Gets the old activity of the user.
     *
     * @return The old activity of the user.
     */
    public Optional<Activity> getOldActivity() {
        return Optional.ofNullable(oldActivity);
    }

    /**
     * Gets the new activity of the user.
     *
     * @return The new activity of the user.
     */
    public Optional<Activity> getNewActivity() {
        return Optional.ofNullable(newActivity);
    }

}

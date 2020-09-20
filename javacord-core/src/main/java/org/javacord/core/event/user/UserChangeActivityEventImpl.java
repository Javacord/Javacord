package org.javacord.core.event.user;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.activity.Activity;
import org.javacord.api.event.user.UserChangeActivityEvent;

import java.util.Optional;

/**
 * The implementation of {@link UserChangeActivityEvent}.
 */
public class UserChangeActivityEventImpl extends OptionalUserEventImpl implements UserChangeActivityEvent {

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
     * @param api The discord api instance.
     * @param userId The id of the user of the event.
     * @param newActivity The new activity of the user.
     * @param oldActivity The old activity of the user.
     */
    public UserChangeActivityEventImpl(DiscordApi api, long userId, Activity newActivity, Activity oldActivity) {
        super(api, userId);
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

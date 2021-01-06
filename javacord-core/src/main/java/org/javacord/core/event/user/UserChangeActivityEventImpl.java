package org.javacord.core.event.user;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.activity.Activity;
import org.javacord.api.event.user.UserChangeActivityEvent;

import java.util.Collection;
import java.util.Collections;

/**
 * The implementation of {@link UserChangeActivityEvent}.
 */
public class UserChangeActivityEventImpl extends OptionalUserEventImpl implements UserChangeActivityEvent {

    /**
     * The new activities of the user.
     */
    private final Collection<Activity> newActivities;

    /**
     * The old activities of the user.
     */
    private final Collection<Activity> oldActivities;

    /**
     * Creates a new user change activity event.
     *
     * @param api The discord api instance.
     * @param userId The id of the user of the event.
     * @param newActivities The new activities of the user.
     * @param oldActivities The old activities of the user.
     */
    public UserChangeActivityEventImpl(DiscordApi api, long userId, Collection<Activity> newActivities,
                                       Collection<Activity> oldActivities) {
        super(api, userId);
        this.newActivities = newActivities;
        this.oldActivities = oldActivities;
    }

    @Override
    public Collection<Activity> getOldActivities() {
        return Collections.unmodifiableCollection(oldActivities);
    }

    @Override
    public Collection<Activity> getNewActivities() {
        return Collections.unmodifiableCollection(newActivities);
    }

}

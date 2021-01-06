package org.javacord.core.entity.user;

import io.vavr.collection.Map;
import org.javacord.api.entity.DiscordClient;
import org.javacord.api.entity.activity.Activity;
import org.javacord.api.entity.user.UserStatus;

import java.util.Collections;
import java.util.Set;

/**
 * Internal class for easy caching of user presences.
 */
public class UserPresence {

    private final long userId;
    private final Set<Activity> activities;
    private final UserStatus status;
    private final Map<DiscordClient, UserStatus> clientStatus;

    /**
     * Creates a new user presence instance.
     *
     * @param userId The id of the user.
     * @param activities The activities.
     * @param status The status.
     * @param clientStatus The client status.
     */
    public UserPresence(long userId, Set<Activity> activities, UserStatus status, Map<DiscordClient,
            UserStatus> clientStatus) {
        this.userId = userId;
        this.activities = activities;
        this.status = status;
        this.clientStatus = clientStatus;
    }

    /**
     * Gets the id of the user that this presence "belongs" to.
     *
     * @return The id of the user.
     */
    public long getUserId() {
        return userId;
    }

    /**
     * Sets the activity.
     *
     * @param activities The activities to set.
     * @return The new user presence with the updated activities.
     */
    public UserPresence setActivities(Set<Activity> activities) {
        return new UserPresence(userId, activities, status, clientStatus);
    }

    /**
     * Gets the presence's activities.
     *
     * @return The presence's activities.
     */
    public Set<Activity> getActivities() {
        return Collections.unmodifiableSet(activities);
    }

    /**
     * Sets the status.
     *
     * @param status The status to set.
     * @return The new user presence with the updated status.
     */
    public UserPresence setStatus(UserStatus status) {
        return new UserPresence(userId, activities, status, clientStatus);
    }

    /**
     * Gets the presence's status.
     *
     * @return The presence's status.
     */
    public UserStatus getStatus() {
        return status;
    }

    /**
     * Sets the client status.
     *
     * @param clientStatus The client status to set.
     * @return The new user presence with the updated client status.
     */
    public UserPresence setClientStatus(Map<DiscordClient, UserStatus> clientStatus) {
        return new UserPresence(userId, activities, status, clientStatus);
    }

    /**
     * Gets the presence's client status.
     *
     * @return The presence's client status.
     */
    public Map<DiscordClient, UserStatus> getClientStatus() {
        return clientStatus;
    }
}

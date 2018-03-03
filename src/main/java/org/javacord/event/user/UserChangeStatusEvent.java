package org.javacord.event.user;

import org.javacord.DiscordApi;
import org.javacord.entity.user.User;
import org.javacord.entity.user.UserStatus;
import org.javacord.DiscordApi;
import org.javacord.entity.user.User;
import org.javacord.entity.user.UserStatus;

/**
 * A user change status event.
 */
public class UserChangeStatusEvent extends UserEvent {

    /**
     * The new status of the user.
     */
    private final UserStatus newStatus;

    /**
     * The old status of the user.
     */
    private final UserStatus oldStatus;

    /**
     * Creates a new user change status event.
     *
     * @param api The api instance of the event.
     * @param user The user of the event.
     * @param newStatus The new status of the user.
     * @param oldStatus The old status of the user.
     */
    public UserChangeStatusEvent(DiscordApi api, User user, UserStatus newStatus, UserStatus oldStatus) {
        super(api, user);
        this.newStatus = newStatus;
        this.oldStatus = oldStatus;
    }

    /**
     * Gets the old status of the user.
     *
     * @return The old status of the user.
     */
    public UserStatus getOldStatus() {
        return oldStatus;
    }

    /**
     * Gets the new status of the user.
     *
     * @return The new status of the user.
     */
    public UserStatus getNewStatus() {
        return newStatus;
    }

}

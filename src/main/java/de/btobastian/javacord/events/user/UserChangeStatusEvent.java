package de.btobastian.javacord.events.user;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.UserStatus;

/**
 * A user change status event.
 */
public class UserChangeStatusEvent extends UserEvent {

    /**
     * The old status of the user.
     */
    private final UserStatus oldStatus;

    /**
     * Creates a new user change status event.
     *
     * @param api The api instance of the event.
     * @param user The user of the event.
     * @param oldStatus The old status of the user.
     */
    public UserChangeStatusEvent(DiscordApi api, User user, UserStatus oldStatus) {
        super(api, user);
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
        // TODO return getUser().getStatus();
        return UserStatus.INVISIBLE;
    }

}

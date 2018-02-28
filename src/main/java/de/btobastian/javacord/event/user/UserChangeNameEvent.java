package de.btobastian.javacord.event.user;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entity.user.User;

/**
 * A user change name event.
 */
public class UserChangeNameEvent extends UserEvent {

    /**
     * The new name of the user.
     */
    private final String newName;

    /**
     * The old name of the user.
     */
    private final String oldName;

    /**
     * Creates a new user change name event.
     *
     * @param api The api instance of the event.
     * @param user The user of the event.
     * @param newName The new name of the user.
     * @param oldName The old name of the user.
     */
    public UserChangeNameEvent(DiscordApi api, User user, String newName, String oldName) {
        super(api, user);
        this.newName = newName;
        this.oldName = oldName;
    }

    /**
     * Gets the new name of the user.
     *
     * @return The new name of the user.
     */
    public String getNewName() {
        return newName;
    }

    /**
     * Gets the old name of the user.
     *
     * @return The old name of the user.
     */
    public String getOldName() {
        return oldName;
    }

}

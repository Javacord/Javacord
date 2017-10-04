package de.btobastian.javacord.events.user;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.User;

/**
 * A user change name event.
 */
public class UserChangeNameEvent extends UserEvent {

    /**
     * The old name of the user.
     */
    private final String oldName;

    /**
     * Creates a new user change name event.
     *
     * @param api The api instance of the event.
     * @param user The user of the event.
     * @param oldName The old name of the user.
     */
    public UserChangeNameEvent(DiscordApi api, User user, String oldName) {
        super(api, user);
        this.oldName = oldName;
    }

    /**
     * Gets the old name of the user.
     *
     * @return The old name of the user.
     */
    public String getOldName() {
        return oldName;
    }

    /**
     * Gets the new name of the user.
     *
     * @return The new name of the user.
     */
    public String getNewName() {
        // TODO return getUser().getName();
        return "";
    }

}

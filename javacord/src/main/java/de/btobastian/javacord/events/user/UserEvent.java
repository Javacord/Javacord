package de.btobastian.javacord.events.user;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.events.Event;

/**
 * A user event.
 */
public abstract class UserEvent extends Event {

    /**
     * The user of the event.
     */
    private final User user;

    /**
     * Creates a new user event.
     *
     * @param api The api instance of the event.
     * @param user The user of the event.
     */
    public UserEvent(DiscordApi api, User user) {
        super(api);
        this.user = user;
    }

    /**
     * Gets the user of the event.
     *
     * @return The user of the event.
     */
    public User getUser() {
        return user;
    }

}

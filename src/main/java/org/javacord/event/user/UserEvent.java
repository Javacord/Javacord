package org.javacord.event.user;

import org.javacord.DiscordApi;
import org.javacord.entity.user.User;
import org.javacord.event.Event;
import org.javacord.DiscordApi;
import org.javacord.entity.user.User;
import org.javacord.event.Event;

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

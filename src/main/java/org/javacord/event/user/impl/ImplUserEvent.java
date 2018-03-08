package org.javacord.event.user.impl;

import org.javacord.entity.user.User;
import org.javacord.event.impl.ImplEvent;
import org.javacord.event.user.UserEvent;

/**
 * The implementation of {@link UserEvent}.
 */
public abstract class ImplUserEvent extends ImplEvent implements UserEvent {

    /**
     * The user of the event.
     */
    private final User user;

    /**
     * Creates a new user event.
     *
     * @param user The user of the event.
     */
    public ImplUserEvent(User user) {
        super(user.getApi());
        this.user = user;
    }

    @Override
    public User getUser() {
        return user;
    }

}

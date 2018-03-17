package org.javacord.core.event.user;

import org.javacord.api.entity.user.User;
import org.javacord.api.event.user.UserEvent;
import org.javacord.core.event.EventImpl;

/**
 * The implementation of {@link UserEvent}.
 */
public abstract class UserEventImpl extends EventImpl implements UserEvent {

    /**
     * The user of the event.
     */
    private final User user;

    /**
     * Creates a new user event.
     *
     * @param user The user of the event.
     */
    public UserEventImpl(User user) {
        super(user.getApi());
        this.user = user;
    }

    @Override
    public User getUser() {
        return user;
    }

}

package org.javacord.event.user.impl;

import org.javacord.entity.user.User;
import org.javacord.event.user.UserChangeNameEvent;

/**
 * The implementation of {@link UserChangeNameEvent}.
 */
public class ImplUserChangeNameEvent extends ImplUserEvent implements UserChangeNameEvent {

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
     * @param user The user of the event.
     * @param newName The new name of the user.
     * @param oldName The old name of the user.
     */
    public ImplUserChangeNameEvent(User user, String newName, String oldName) {
        super(user);
        this.newName = newName;
        this.oldName = oldName;
    }

    @Override
    public String getNewName() {
        return newName;
    }

    @Override
    public String getOldName() {
        return oldName;
    }

}

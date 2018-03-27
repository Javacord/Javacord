package org.javacord.core.event.user;

import org.javacord.api.entity.user.User;
import org.javacord.api.event.user.UserChangeNameEvent;

/**
 * The implementation of {@link UserChangeNameEvent}.
 */
public class UserChangeNameEventImpl extends UserEventImpl implements UserChangeNameEvent {

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
    public UserChangeNameEventImpl(User user, String newName, String oldName) {
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

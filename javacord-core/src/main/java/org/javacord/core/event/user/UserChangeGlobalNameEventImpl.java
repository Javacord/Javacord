package org.javacord.core.event.user;

import org.javacord.api.entity.user.User;
import org.javacord.api.event.user.UserChangeGlobalNameEvent;

import java.util.Optional;

/**
 * The implementation of {@link UserChangeGlobalNameEvent}.
 */
public class UserChangeGlobalNameEventImpl extends UserEventImpl implements UserChangeGlobalNameEvent {

    /**
     * The new global name of the user.
     */
    private final String newGlobalName;

    /**
     * The old global name of the user.
     */
    private final String oldGlobalName;

    /**
     * Creates a new user change global name event.
     *
     * @param user The user of the event.
     * @param newGlobalName The new global name of the user.
     * @param oldGlobalName The old global name of the user.
     */
    public UserChangeGlobalNameEventImpl(User user, String newGlobalName, String oldGlobalName) {
        super(user);
        this.newGlobalName = newGlobalName;
        this.oldGlobalName = oldGlobalName;
    }

    @Override
    public Optional<String> getNewGlobalName() {
        return Optional.ofNullable(newGlobalName);
    }

    @Override
    public Optional<String> getOldGlobalName() {
        return Optional.ofNullable(oldGlobalName);
    }
}

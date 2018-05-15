package org.javacord.core.event.user;

import org.javacord.api.entity.user.User;
import org.javacord.api.event.user.UserChangeDiscriminatorEvent;

/**
 * The implementation of {@link UserChangeDiscriminatorEvent}.
 */
public class UserChangeDiscriminatorEventImpl extends UserEventImpl implements UserChangeDiscriminatorEvent {

    /**
     * The new discriminator of the user.
     */
    private final String newDiscriminator;

    /**
     * The old discriminator of the user.
     */
    private final String oldDiscriminator;

    /**
     * Creates a new user change discriminator event.
     *
     * @param user The user of the event.
     * @param newDiscriminator The new discriminator of the user.
     * @param oldDiscriminator The old discriminator of the user.
     */
    public UserChangeDiscriminatorEventImpl(User user, String newDiscriminator, String oldDiscriminator) {
        super(user);
        this.newDiscriminator = newDiscriminator;
        this.oldDiscriminator = oldDiscriminator;
    }

    @Override
    public String getNewDiscriminator() {
        return newDiscriminator;
    }

    @Override
    public String getOldDiscriminator() {
        return oldDiscriminator;
    }

}

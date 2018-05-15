package org.javacord.core.event.user;

import org.javacord.api.entity.Icon;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.user.UserChangeAvatarEvent;
import org.javacord.core.entity.user.UserImpl;

/**
 * The implementation of {@link UserChangeAvatarEvent}.
 */
public class UserChangeAvatarEventImpl extends UserEventImpl implements UserChangeAvatarEvent {

    /**
     * The new avatar hash of the user.
     */
    private final String newAvatarHash;

    /**
     * The old avatar hash of the user.
     */
    private final String oldAvatarHash;

    /**
     * Creates a new user change avatar event.
     *
     * @param user The user of the event.
     * @param newAvatarHash The new avatar hash of the user.
     * @param oldAvatarHash The old avatar hash of the user.
     */
    public UserChangeAvatarEventImpl(User user, String newAvatarHash, String oldAvatarHash) {
        super(user);
        this.newAvatarHash = newAvatarHash;
        this.oldAvatarHash = oldAvatarHash;
    }

    @Override
    public Icon getNewAvatar() {
        return UserImpl.getAvatar(api, newAvatarHash, getUser().getDiscriminator(), getUser().getId());
    }

    @Override
    public boolean newAvatarIsDefaultAvatar() {
        return newAvatarHash == null;
    }

    @Override
    public Icon getOldAvatar() {
        return UserImpl.getAvatar(api, oldAvatarHash, getUser().getDiscriminator(), getUser().getId());
    }

    @Override
    public boolean oldAvatarIsDefaultAvatar() {
        return oldAvatarHash == null;
    }

}

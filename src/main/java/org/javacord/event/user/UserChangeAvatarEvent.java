package org.javacord.event.user;

import org.javacord.entity.Icon;

/**
 * A user change avatar event.
 */
public interface UserChangeAvatarEvent extends UserEvent {

    /**
     * Gets the new avatar of the user.
     *
     * @return The new avatar of the user.
     */
    Icon getNewAvatar();

    /**
     * Checks if the new avatar is a default avatar.
     *
     * @return Whether the new avatar is a default avatar or not.
     */
    boolean newAvatarIsDefaultAvatar();

    /**
     * Gets the old avatar of the user.
     *
     * @return The old avatar of the user.
     */
    Icon getOldAvatar();

    /**
     * Checks if the old avatar is a default avatar.
     *
     * @return Whether the old avatar is a default avatar or not.
     */
    boolean oldAvatarIsDefaultAvatar();

}

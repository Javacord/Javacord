package org.javacord.api.event.user;

import org.javacord.api.entity.Icon;
import org.javacord.api.event.server.member.ServerMemberEvent;

/**
 * A user change avatar event.
 */
public interface UserChangeAvatarEvent extends ServerMemberEvent {

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

package org.javacord.api.event.server.member;

import org.javacord.api.entity.Icon;
import org.javacord.api.event.server.member.ServerMemberEvent;

import java.util.Optional;

public interface ServerMemberChangeServerAvatarEvent extends ServerMemberEvent {

    /**
     * Gets the old server avatar of the user.
     *
     * @return The old server avatar of the user.
     */
    Optional<Icon> getOldServerAvatar();

    /**
     * Gets the old server avatar of the user in the given size.
     *
     * @param size The size of the image. Must be a power of 2 between 16 and 4096.
     * @return The old server avatar of the user.
     */
    Optional<Icon> getOldServerAvatar(int size);

    /**
     * Gets the new server avatar of the user.
     *
     * @return The new server avatar of the user.
     */
    Optional<Icon> getNewServerAvatar();

    /**
     * Gets the new server avatar of the user in the given size.
     *
     * @param size The size of the image. Must be a power of 2 between 16 and 4096.
     * @return The new server avatar of the user.
     */
    Optional<Icon> getNewServerAvatar(int size);
}

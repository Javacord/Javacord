package org.javacord.api.event.server.emoji;

import org.javacord.api.entity.permission.Role;

import java.util.Optional;
import java.util.Set;

/**
 * A custom emoji change whitelisted roles event.
 */
public interface KnownCustomEmojiChangeWhitelistedRolesEvent extends KnownCustomEmojiEvent {

    /**
     * Gets the old whitelisted roles.
     *
     * @return The old whitelisted roles.
     */
    Optional<Set<Role>> getOldWhitelistedRoles();

    /**
     * Gets the new whitelisted roles.
     *
     * @return The new whitelisted roles.
     */
    Optional<Set<Role>> getNewWhitelistedRoles();

}

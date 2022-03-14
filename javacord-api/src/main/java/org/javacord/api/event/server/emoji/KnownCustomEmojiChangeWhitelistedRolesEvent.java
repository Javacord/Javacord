package org.javacord.api.event.server.emoji;

import org.javacord.api.entity.permission.Role;

import java.util.Optional;
import java.util.Set;

/**
 * A custom emoji change whitelisted roles event.
 */
public interface KnownCustomEmojiChangeWhitelistedRolesEvent extends KnownCustomEmojiEvent {

    /**
     * Gets a set with the old whitelisted roles.
     *
     * @return A set with the old whitelisted roles.
     */
    Optional<Set<Role>> getOldWhitelistedRoles();

    /**
     * Gets a set with the new whitelisted roles.
     *
     * @return A set with the new whitelisted roles.
     */
    Optional<Set<Role>> getNewWhitelistedRoles();

}

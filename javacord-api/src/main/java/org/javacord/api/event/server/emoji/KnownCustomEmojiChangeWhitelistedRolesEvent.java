package org.javacord.api.event.server.emoji;

import org.javacord.api.entity.permission.Role;

import java.util.Collection;
import java.util.Optional;

/**
 * A custom emoji change whitelisted roles event.
 */
public interface KnownCustomEmojiChangeWhitelistedRolesEvent extends KnownCustomEmojiEvent {

    /**
     * Gets a list with the old whitelisted roles.
     *
     * @return A list with the old whitelisted roles.
     */
    Optional<Collection<Role>> getOldWhitelistedRoles();

    /**
     * Gets a list with the new whitelisted roles.
     *
     * @return A list with the new whitelisted roles.
     */
    Optional<Collection<Role>> getNewWhitelistedRoles();

}

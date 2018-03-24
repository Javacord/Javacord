package org.javacord.api.entity.emoji.internal;

import org.javacord.api.entity.emoji.CustomEmojiUpdater;
import org.javacord.api.entity.permission.Role;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * This class is internally used by the {@link CustomEmojiUpdater} to update custom emojis.
 * You usually don't want to interact with this object.
 */
public interface CustomEmojiUpdaterDelegate {

    /**
     * Sets the reason for this update. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     */
    void setAuditLogReason(String reason);

    /**
     * Queues the name to be updated.
     *
     * @param name The new name of the emoji.
     */
    void setName(String name);

    /**
     * Queues a role to be added to the whitelist.
     *
     * @param role The role to add.
     */
    void addRoleToWhitelist(Role role);

    /**
     * Queues a role to be removed from the whitelist.
     * To be active, the whitelist must at least contain one role, otherwise everyone can use the emoji!
     *
     * @param role The role to remove.
     */
    void removeRoleFromWhitelist(Role role);

    /**
     * Queues the whitelist to be removed.
     */
    void removeWhitelist();

    /**
     * Sets the roles which should be whitelisted.
     * To be active, the whitelist must at least contain one role, otherwise everyone can use the emoji!
     *
     * @param roles The roles which should be whitelisted.
     */
    void setWhitelist(Collection<Role> roles);

    /**
     * Sets the roles which should be whitelisted.
     * To be active, the whitelist must at least contain one role, otherwise everyone can use the emoji!
     *
     * @param roles The roles which should be whitelisted.
     */
    void setWhitelist(Role... roles);

    /**
     * Performs the queued updates.
     *
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> update();

}

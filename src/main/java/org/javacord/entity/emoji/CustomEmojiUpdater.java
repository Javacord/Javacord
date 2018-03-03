package org.javacord.entity.emoji;

import org.javacord.entity.permission.Role;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update known custom emojis.
 */
public interface CustomEmojiUpdater {

    /**
     * Sets the reason for this update. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     * @return The current instance in order to chain call methods.
     */
    CustomEmojiUpdater setAuditLogReason(String reason);

    /**
     * Queues the name to be updated.
     *
     * @param name The new name of the emoji.
     * @return The current instance in order to chain call methods.
     */
    CustomEmojiUpdater setName(String name);

    /**
     * Queues a role to be added to the whitelist.
     *
     * @param role The role to add.
     * @return The current instance in order to chain call methods.
     */
    CustomEmojiUpdater addRoleToWhitelist(Role role);

    /**
     * Queues a role to be removed from the whitelist.
     * To be active, the whitelist must at least contain one role, otherwise everyone can use the emoji!
     *
     * @param role The role to remove.
     * @return The current instance in order to chain call methods.
     */
    CustomEmojiUpdater removeRoleFromWhitelist(Role role);

    /**
     * Queues the whitelist to be removed.
     *
     * @return The current instance in order to chain call methods.
     */
    CustomEmojiUpdater removeWhitelist();

    /**
     * Sets the roles which should be whitelisted.
     * To be active, the whitelist must at least contain one role, otherwise everyone can use the emoji!
     *
     * @param roles The roles which should be whitelisted.
     * @return The current instance in order to chain call methods.
     */
    CustomEmojiUpdater setWhitelist(Collection<Role> roles);

    /**
     * Sets the roles which should be whitelisted.
     * To be active, the whitelist must at least contain one role, otherwise everyone can use the emoji!
     *
     * @param roles The roles which should be whitelisted.
     * @return The current instance in order to chain call methods.
     */
    CustomEmojiUpdater setWhitelist(Role... roles);

    /**
     * Performs the queued updates.
     *
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> update();

}

package org.javacord.entity.permission;

import java.awt.Color;
import java.util.concurrent.CompletableFuture;

/**
 * This interface can be used to update the settings of a role.
 */
public interface RoleUpdater {

    /**
     * Sets the reason for this update. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     * @return The current instance in order to chain call methods.
     */
    RoleUpdater setAuditLogReason(String reason);

    /**
     * Queues the name to be updated.
     *
     * @param name The new name of the role.
     * @return The current instance in order to chain call methods.
     */
    RoleUpdater setName(String name);

    /**
     * Queues the permissions to be updated.
     *
     * @param permissions The new permissions of the role.
     * @return The current instance in order to chain call methods.
     */
    RoleUpdater setPermissions(Permissions permissions);

    /**
     * Queues the color to be updated.
     *
     * @param color The new color of the role.
     * @return The current instance in order to chain call methods.
     */
    RoleUpdater setColor(Color color);

    /**
     * Queues the display separately flag (sometimes called "hoist") to be updated.
     *
     * @param displaySeparately The new display separately flag of the role.
     * @return The current instance in order to chain call methods.
     */
    RoleUpdater setDisplaySeparatelyFlag(boolean displaySeparately);

    /**
     * Queues the mentionable flag to be updated.
     *
     * @param mentionable The new mentionable flag of the role.
     * @return The current instance in order to chain call methods.
     */
    RoleUpdater setMentionableFlag(boolean mentionable);

    /**
     * Performs the queued updates.
     *
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> update();

}

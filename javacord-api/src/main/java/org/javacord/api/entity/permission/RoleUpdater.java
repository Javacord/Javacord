package org.javacord.api.entity.permission;

import org.javacord.api.entity.permission.internal.RoleUpdaterDelegate;
import org.javacord.api.util.internal.DelegateFactory;

import java.awt.Color;
import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update the settings of a role.
 */
public class RoleUpdater {

    /**
     * The role delegate used by this instance.
     */
    private final RoleUpdaterDelegate delegate;

    /**
     * Creates a new role updater.
     *
     * @param role The role to update.
     */
    public RoleUpdater(Role role) {
        delegate = DelegateFactory.createRoleUpdaterDelegate(role);
    }

    /**
     * Sets the reason for this update. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     * @return The current instance in order to chain call methods.
     */
    public RoleUpdater setAuditLogReason(String reason) {
        delegate.setAuditLogReason(reason);
        return this;
    }

    /**
     * Queues the name to be updated.
     *
     * @param name The new name of the role.
     * @return The current instance in order to chain call methods.
     */
    public RoleUpdater setName(String name) {
        delegate.setName(name);
        return this;
    }

    /**
     * Queues the permissions to be updated.
     *
     * @param permissions The new permissions of the role.
     * @return The current instance in order to chain call methods.
     */
    public RoleUpdater setPermissions(Permissions permissions) {
        delegate.setPermissions(permissions);
        return this;
    }

    /**
     * Queues the color to be updated.
     *
     * @param color The new color of the role.
     * @return The current instance in order to chain call methods.
     */
    public RoleUpdater setColor(Color color) {
        delegate.setColor(color);
        return this;
    }

    /**
     * Queues the display separately flag (sometimes called "hoist") to be updated.
     *
     * @param displaySeparately The new display separately flag of the role.
     * @return The current instance in order to chain call methods.
     */
    public RoleUpdater setDisplaySeparatelyFlag(boolean displaySeparately) {
        delegate.setDisplaySeparatelyFlag(displaySeparately);
        return this;
    }

    /**
     * Queues the mentionable flag to be updated.
     *
     * @param mentionable The new mentionable flag of the role.
     * @return The current instance in order to chain call methods.
     */
    public RoleUpdater setMentionableFlag(boolean mentionable) {
        delegate.setMentionableFlag(mentionable);
        return this;
    }

    /**
     * Performs the queued updates.
     *
     * @return A future to check if the update was successful.
     */
    public CompletableFuture<Void> update() {
        return delegate.update();
    }

}

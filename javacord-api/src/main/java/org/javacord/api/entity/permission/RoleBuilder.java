package org.javacord.api.entity.permission;

import org.javacord.api.entity.permission.internal.RoleBuilderDelegate;
import org.javacord.api.entity.server.Server;
import org.javacord.api.util.internal.DelegateFactory;

import java.awt.Color;
import java.util.concurrent.CompletableFuture;

/**
 * This class is used to create roles.
 */
public class RoleBuilder {

    /**
     * The role delegate used by this instance.
     */
    private final RoleBuilderDelegate delegate;

    /**
     * Creates a new role role builder for the given server.
     *
     * @param server The server for which the role should be created.
     */
    public RoleBuilder(Server server) {
        delegate = DelegateFactory.createRoleBuilderDelegate(server);
    }

    /**
     * Sets the reason for the creation. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     * @return The current instance in order to chain call methods.
     */
    public RoleBuilder setAuditLogReason(String reason) {
        delegate.setAuditLogReason(reason);
        return this;
    }

    /**
     * Sets the name of the role.
     * By default it's <code>"new role"</code>.
     *
     * @param name The name of the role.
     * @return The current instance in order to chain call methods.
     */
    public RoleBuilder setName(String name) {
        delegate.setName(name);
        return this;
    }

    /**
     * Sets the permissions of the role.
     * By default it uses the permissions of the @everyone role.
     *
     * @param permissions The permissions to set.
     * @return The current instance in order to chain call methods.
     */
    public RoleBuilder setPermissions(Permissions permissions) {
        delegate.setPermissions(permissions);
        return this;
    }

    /**
     * Sets the color of the role.
     *
     * @param color The color of the role.
     * @return The current instance in order to chain call methods.
     */
    public RoleBuilder setColor(Color color) {
        delegate.setColor(color);
        return this;
    }

    /**
     * Sets if the role is mentionable or not.
     * By default it's set to <code>false</code>.
     *
     * @param mentionable Whether the role should be mentionable or not.
     * @return The current instance in order to chain call methods.
     */
    public RoleBuilder setMentionable(boolean mentionable) {
        delegate.setMentionable(mentionable);
        return this;
    }

    /**
     * Sets if the role should be pinned in the user listing (sometimes called "hoist").
     *
     * @param displaySeparately Whether the role should be pinned in the user listing or not.
     * @return The current instance in order to chain call methods.
     */
    public RoleBuilder setDisplaySeparately(boolean displaySeparately) {
        delegate.setDisplaySeparately(displaySeparately);
        return this;
    }

    /**
     * Creates the server text channel.
     *
     * @return The created text channel.
     */
    public CompletableFuture<Role> create() {
        return delegate.create();
    }

}

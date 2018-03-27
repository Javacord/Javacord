package org.javacord.api.entity.permission.internal;

import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.permission.RoleBuilder;

import java.awt.Color;
import java.util.concurrent.CompletableFuture;

/**
 * This class is internally used by the {@link RoleBuilder} to create roles.
 * You usually don't want to interact with this object.
 */
public interface RoleBuilderDelegate {

    /**
     * Sets the reason for the creation. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     */
    void setAuditLogReason(String reason);

    /**
     * Sets the name of the role.
     * By default it's <code>"new role"</code>.
     *
     * @param name The name of the role.
     */
    void setName(String name);

    /**
     * Sets the permissions of the role.
     * By default it uses the permissions of the @everyone role.
     *
     * @param permissions The permissions to set.
     */
    void setPermissions(Permissions permissions);

    /**
     * Sets the color of the role.
     *
     * @param color The color of the role.
     */
    void setColor(Color color);

    /**
     * Sets if the role is mentionable or not.
     * By default it's set to <code>false</code>.
     *
     * @param mentionable Whether the role should be mentionable or not.
     */
    void setMentionable(boolean mentionable);

    /**
     * Sets if the role should be pinned in the user listing (sometimes called "hoist").
     *
     * @param displaySeparately Whether the role should be pinned in the user listing or not.
     */
    void setDisplaySeparately(boolean displaySeparately);

    /**
     * Creates the role.
     *
     * @return The created role.
     */
    CompletableFuture<Role> create();

}

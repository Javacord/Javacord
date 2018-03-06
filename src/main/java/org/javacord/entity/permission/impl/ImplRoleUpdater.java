package org.javacord.entity.permission.impl;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.entity.permission.Permissions;
import org.javacord.entity.permission.Role;
import org.javacord.entity.permission.RoleUpdater;
import org.javacord.util.rest.RestEndpoint;
import org.javacord.util.rest.RestMethod;
import org.javacord.util.rest.RestRequest;

import java.awt.Color;
import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update the settings of a role.
 */
public class ImplRoleUpdater implements RoleUpdater {

    /**
     * The role to update.
     */
    private final Role role;

    /**
     * The reason for the update.
     */
    private String reason = null;

    /**
     * The name to update.
     */
    private String name = null;

    /**
     * The permissions to update.
     */
    private Permissions permissions = null;

    /**
     * The color to update.
     */
    private Color color = null;

    /**
     * The display separately flag to update.
     */
    private Boolean displaySeparately = null;

    /**
     * The mentionable flag to update.
     */
    private Boolean mentionable = null;

    /**
     * Creates a new role updater.
     *
     * @param role The role to update.
     */
    public ImplRoleUpdater(Role role) {
        this.role = role;
    }

    @Override
    public RoleUpdater setAuditLogReason(String reason) {
        this.reason = reason;
        return this;
    }

    @Override
    public RoleUpdater setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public RoleUpdater setPermissions(Permissions permissions) {
        this.permissions = permissions;
        return this;
    }

    @Override
    public RoleUpdater setColor(Color color) {
        this.color = color;
        return this;
    }

    @Override
    public RoleUpdater setDisplaySeparatelyFlag(boolean displaySeparately) {
        this.displaySeparately = displaySeparately;
        return this;
    }

    @Override
    public RoleUpdater setMentionableFlag(boolean mentionable) {
        this.mentionable = mentionable;
        return this;
    }

    @Override
    public CompletableFuture<Void> update() {
        boolean patchRole = false;
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        if (name != null) {
            body.put("name", name);
            patchRole = true;
        }
        if (permissions != null) {
            body.put("permissions", permissions.getAllowedBitmask());
            patchRole = true;
        }
        if (color != null) {
            body.put("color", color.getRGB() & 0xFFFFFF);
            patchRole = true;
        }
        if (displaySeparately != null) {
            body.put("hoist", displaySeparately.booleanValue());
            patchRole = true;
        }
        if (mentionable != null) {
            body.put("mentionable", mentionable.booleanValue());
            patchRole = true;
        }
        if (patchRole) {
            return new RestRequest<Void>(role.getApi(), RestMethod.PATCH, RestEndpoint.ROLE)
                    .setUrlParameters(role.getServer().getIdAsString(), role.getIdAsString())
                    .setBody(body)
                    .setAuditLogReason(reason)
                    .execute(result -> null);
        } else {
            return CompletableFuture.completedFuture(null);
        }
    }

}

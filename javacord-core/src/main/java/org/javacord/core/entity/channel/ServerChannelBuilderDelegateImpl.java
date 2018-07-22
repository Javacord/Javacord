package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Permissionable;
import org.javacord.api.entity.channel.internal.ServerChannelBuilderDelegate;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;
import org.javacord.core.entity.server.ServerImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * The implementation of {@link ServerChannelBuilderDelegate}.
 */
public class ServerChannelBuilderDelegateImpl implements ServerChannelBuilderDelegate {

    /**
     * The server of the channel.
     */
    protected final ServerImpl server;

    /**
     * The reason for the creation.
     */
    protected String reason = null;

    /**
     * The name of the channel.
     */
    private String name = null;

    /**
     * The overwritten user permissions.
     */
    private final Map<Long, Permissions> overwrittenUserPermissions = new HashMap<>();

    /**
     * The overwritten role permissions.
     */
    private final Map<Long, Permissions> overwrittenRolePermissions = new HashMap<>();

    protected ServerChannelBuilderDelegateImpl(ServerImpl server) {
        this.server = server;
    }

    @Override
    public void setAuditLogReason(String reason) {
        this.reason = reason;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public <T extends Permissionable & DiscordEntity> void addPermissionOverwrite(T permissionable,
                                                                                  Permissions permissions) {
        if (permissionable instanceof Role) {
            overwrittenRolePermissions.put(permissionable.getId(), permissions);
        } else if (permissionable instanceof User) {
            overwrittenUserPermissions.put(permissionable.getId(), permissions);
        }
    }

    @Override
    public <T extends Permissionable & DiscordEntity> void removePermissionOverwrite(T permissionable) {
        if (permissionable instanceof Role) {
            overwrittenRolePermissions.remove(permissionable.getId());
        } else if (permissionable instanceof User) {
            overwrittenUserPermissions.remove(permissionable.getId());
        }
    }

    protected void prepareBody(ObjectNode body) {
        if (name == null) {
            throw new IllegalStateException("Name is no optional parameter!");
        }
        body.put("name", name);
        ArrayNode permissionOverwrites = null;
        if (overwrittenUserPermissions.size() + overwrittenRolePermissions.size() > 0) {
            permissionOverwrites = body.putArray("permission_overwrites");
        }
        for (Map.Entry<Long, Permissions> entry : overwrittenUserPermissions.entrySet()) {
            permissionOverwrites.addObject()
                    .put("id", Long.toUnsignedString(entry.getKey()))
                    .put("type", "member")
                    .put("allow", entry.getValue().getAllowedBitmask())
                    .put("deny", entry.getValue().getDeniedBitmask());
        }
        for (Map.Entry<Long, Permissions> entry : overwrittenRolePermissions.entrySet()) {
            permissionOverwrites.addObject()
                    .put("id", Long.toUnsignedString(entry.getKey()))
                    .put("type", "role")
                    .put("allow", entry.getValue().getAllowedBitmask())
                    .put("deny", entry.getValue().getDeniedBitmask());
        }
    }
}

package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Permissionable;
import org.javacord.api.entity.channel.internal.RegularServerChannelBuilderDelegate;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;
import org.javacord.core.entity.server.ServerImpl;
import java.util.HashMap;
import java.util.Map;

/**
 * The implementation of {@link RegularServerChannelBuilderDelegate}.
 */
public class RegularServerChannelBuilderDelegateImpl extends ServerChannelBuilderDelegateImpl implements
        RegularServerChannelBuilderDelegate {

    /**
     * The overwritten user permissions.
     */
    private final Map<Long, Permissions> overwrittenUserPermissions = new HashMap<>();

    /**
     * The overwritten role permissions.
     */
    private final Map<Long, Permissions> overwrittenRolePermissions = new HashMap<>();

    /**
     * The position to update.
     */
    protected Integer position = null;

    protected RegularServerChannelBuilderDelegateImpl(ServerImpl server) {
        super(server);
    }

    @Override
    public void setRawPosition(int rawPosition) {
        this.position = rawPosition;
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

    @Override
    protected void prepareBody(ObjectNode body) {
        super.prepareBody(body);
        if (overwrittenUserPermissions.size() + overwrittenRolePermissions.size() > 0) {
            ArrayNode permissionOverwrites = body.putArray("permission_overwrites");

            for (Map.Entry<Long, Permissions> entry : overwrittenUserPermissions.entrySet()) {
                permissionOverwrites.addObject()
                        .put("id", Long.toUnsignedString(entry.getKey()))
                        .put("type", 1)
                        .put("allow", entry.getValue().getAllowedBitmask())
                        .put("deny", entry.getValue().getDeniedBitmask());
            }
            for (Map.Entry<Long, Permissions> entry : overwrittenRolePermissions.entrySet()) {
                permissionOverwrites.addObject()
                        .put("id", Long.toUnsignedString(entry.getKey()))
                        .put("type", 0)
                        .put("allow", entry.getValue().getAllowedBitmask())
                        .put("deny", entry.getValue().getDeniedBitmask());
            }
        }
    }
}

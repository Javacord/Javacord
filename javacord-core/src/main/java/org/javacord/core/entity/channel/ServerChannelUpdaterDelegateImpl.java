package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Permissionable;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.internal.ServerChannelUpdaterDelegate;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link ServerChannelUpdaterDelegate}.
 */
public class ServerChannelUpdaterDelegateImpl implements ServerChannelUpdaterDelegate {

    /**
     * The channel to update.
     */
    protected final ServerChannel channel;

    /**
     * The reason for the update.
     */
    protected String reason = null;

    /**
     * The name to update.
     */
    protected String name = null;

    /**
     * The position to update.
     */
    protected Integer position = null;

    /**
     * A map with all overwritten user permissions.
     */
    protected Map<Long, Permissions> overwrittenUserPermissions = null;

    /**
     * A map with all overwritten role permissions.
     */
    protected Map<Long, Permissions> overwrittenRolePermissions = null;

    /**
     * Creates a new server channel updater delegate.
     *
     * @param channel The channel to update.
     */
    public ServerChannelUpdaterDelegateImpl(ServerChannel channel) {
        this.channel = channel;
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
    public void setRawPosition(int rawPosition) {
        this.position = rawPosition;
    }

    @Override
    public <T extends Permissionable & DiscordEntity> void addPermissionOverwrite(T permissionable,
                                                                                  Permissions permissions) {
        populatePermissionOverwrites();
        if (permissionable instanceof Role) {
            overwrittenRolePermissions.put(permissionable.getId(), permissions);
        } else if (permissionable instanceof User) {
            overwrittenUserPermissions.put(permissionable.getId(), permissions);
        }
    }

    @Override
    public <T extends Permissionable & DiscordEntity> void removePermissionOverwrite(T permissionable) {
        populatePermissionOverwrites();
        if (permissionable instanceof Role) {
            overwrittenRolePermissions.remove(permissionable.getId());
        } else if (permissionable instanceof User) {
            overwrittenUserPermissions.remove(permissionable.getId());
        }
    }

    @Override
    public CompletableFuture<Void> update() {
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        if (prepareUpdateBody(body)) {
            return new RestRequest<Void>(channel.getApi(), RestMethod.PATCH, RestEndpoint.CHANNEL)
                    .setUrlParameters(channel.getIdAsString())
                    .setBody(body)
                    .setAuditLogReason(reason)
                    .execute(result -> null);
        } else {
            return CompletableFuture.completedFuture(null);
        }
    }

    protected boolean prepareUpdateBody(ObjectNode body) {
        boolean patchChannel = false;
        if (name != null) {
            body.put("name", name);
            patchChannel = true;
        }
        if (position != null) {
            body.put("position", position.intValue());
            patchChannel = true;
        }
        ArrayNode permissionOverwrites = null;
        if (overwrittenUserPermissions != null || overwrittenRolePermissions != null) {
            permissionOverwrites = body.putArray("permission_overwrites");
            patchChannel = true;
        }
        if (overwrittenUserPermissions != null) {
            for (Map.Entry<Long, Permissions> entry : overwrittenUserPermissions.entrySet()) {
                permissionOverwrites.addObject()
                        .put("id", Long.toUnsignedString(entry.getKey()))
                        .put("type", "member")
                        .put("allow", entry.getValue().getAllowedBitmask())
                        .put("deny", entry.getValue().getDeniedBitmask());
            }
        }
        if (overwrittenRolePermissions != null) {
            for (Map.Entry<Long, Permissions> entry : overwrittenRolePermissions.entrySet()) {
                permissionOverwrites.addObject()
                        .put("id", Long.toUnsignedString(entry.getKey()))
                        .put("type", "role")
                        .put("allow", entry.getValue().getAllowedBitmask())
                        .put("deny", entry.getValue().getDeniedBitmask());
            }
        }
        return patchChannel;
    }

    /**
     * Populates the maps which contain the permission overwrites.
     */
    private void populatePermissionOverwrites() {
        if (overwrittenUserPermissions == null) {
            overwrittenUserPermissions = new HashMap<>();
            overwrittenUserPermissions.putAll(((ServerChannelImpl) channel).getInternalOverwrittenUserPermissions());
        }
        if (overwrittenRolePermissions == null) {
            overwrittenRolePermissions = new HashMap<>();
            overwrittenRolePermissions.putAll(((ServerChannelImpl) channel).getInternalOverwrittenRolePermissions());
        }
    }

}

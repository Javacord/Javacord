package org.javacord.entity.channel.impl;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.entity.channel.ServerChannel;
import org.javacord.entity.channel.ServerChannelUpdaterDelegate;
import org.javacord.entity.permission.Permissions;
import org.javacord.entity.permission.Role;
import org.javacord.entity.user.User;
import org.javacord.util.rest.RestEndpoint;
import org.javacord.util.rest.RestMethod;
import org.javacord.util.rest.RestRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link ServerChannelUpdaterDelegate}.
 */
public class ImplServerChannelUpdaterDelegate implements ServerChannelUpdaterDelegate {

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
    public ImplServerChannelUpdaterDelegate(ServerChannel channel) {
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
    public void addPermissionOverwrite(User user, Permissions permissions) {
        populatePermissionOverwrites();
        overwrittenUserPermissions.put(user.getId(), permissions);
    }

    @Override
    public void addPermissionOverwrite(Role role, Permissions permissions) {
        populatePermissionOverwrites();
        overwrittenRolePermissions.put(role.getId(), permissions);
    }

    @Override
    public void removePermissionOverwrite(User user) {
        populatePermissionOverwrites();
        overwrittenUserPermissions.remove(user.getId());
    }

    @Override
    public void removePermissionOverwrite(Role role) {
        populatePermissionOverwrites();
        overwrittenRolePermissions.remove(role.getId());
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
            channel.asServerTextChannel()
                    .map(c -> (ImplServerTextChannel) c)
                    .map(ImplServerTextChannel::getOverwrittenUserPermissions)
                    .ifPresent(overwrittenUserPermissions::putAll);
            channel.asServerVoiceChannel()
                    .map(c -> (ImplServerVoiceChannel) c)
                    .map(ImplServerVoiceChannel::getOverwrittenUserPermissions)
                    .ifPresent(overwrittenUserPermissions::putAll);
            channel.asChannelCategory()
                    .map(c -> (ImplChannelCategory) c)
                    .map(ImplChannelCategory::getOverwrittenUserPermissions)
                    .ifPresent(overwrittenUserPermissions::putAll);
        }
        if (overwrittenRolePermissions == null) {
            overwrittenRolePermissions = new HashMap<>();
            channel.asServerTextChannel()
                    .map(c -> (ImplServerTextChannel) c)
                    .map(ImplServerTextChannel::getOverwrittenRolePermissions)
                    .ifPresent(overwrittenRolePermissions::putAll);
            channel.asServerVoiceChannel()
                    .map(c -> (ImplServerVoiceChannel) c)
                    .map(ImplServerVoiceChannel::getOverwrittenRolePermissions)
                    .ifPresent(overwrittenRolePermissions::putAll);
            channel.asChannelCategory()
                    .map(c -> (ImplChannelCategory) c)
                    .map(ImplChannelCategory::getOverwrittenRolePermissions)
                    .ifPresent(overwrittenRolePermissions::putAll);
        }
    }

}

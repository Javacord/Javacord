package org.javacord.entity.channel.impl;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.entity.channel.ChannelCategory;
import org.javacord.entity.channel.ServerVoiceChannel;
import org.javacord.entity.channel.ServerVoiceChannelUpdater;
import org.javacord.entity.permission.Permissions;
import org.javacord.entity.permission.Role;
import org.javacord.entity.permission.impl.ImplPermissions;
import org.javacord.entity.user.User;
import org.javacord.util.rest.RestEndpoint;
import org.javacord.util.rest.RestMethod;
import org.javacord.util.rest.RestRequest;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link ServerVoiceChannelUpdater}.
 */
public class ImplServerVoiceChannelUpdater extends ImplServerChannelUpdater implements ServerVoiceChannelUpdater {

    /**
     * The bitrate to update.
     */
    protected Integer bitrate = null;

    /**
     * The user limit to update.
     */
    protected Integer userLimit = null;

    /**
     * The category to update.
     */
    protected ChannelCategory category = null;

    /**
     * Whether the category should be modified or not.
     */
    protected boolean modifyCategory = false;

    /**
     * Creates a new server voice channel updater.
     *
     * @param channel The channel to update.
     */
    public ImplServerVoiceChannelUpdater(ServerVoiceChannel channel) {
        super(channel);
    }

    @Override
    public ServerVoiceChannelUpdater setBitrate(int bitrate) {
        this.bitrate = bitrate;
        return this;
    }

    @Override
    public ServerVoiceChannelUpdater setUserLimit(int userLimit) {
        this.userLimit = userLimit;
        return this;
    }

    @Override
    public ServerVoiceChannelUpdater removeUserLimit() {
        this.userLimit = 0;
        return this;
    }

    @Override
    public ServerVoiceChannelUpdater setCategory(ChannelCategory category) {
        this.category = category;
        this.modifyCategory = true;
        return this;
    }

    @Override
    public ServerVoiceChannelUpdater removeCategory() {
        return setCategory(null);
    }

    @Override
    public ServerVoiceChannelUpdater setAuditLogReason(String reason) {
        super.setAuditLogReason(reason);
        return this;
    }

    @Override
    public ServerVoiceChannelUpdater setName(String name) {
        super.setName(name);
        return this;
    }

    @Override
    public ServerVoiceChannelUpdater setRawPosition(int rawPosition) {
        super.setRawPosition(rawPosition);
        return this;
    }

    @Override
    public ServerVoiceChannelUpdater addPermissionOverwrite(User user, Permissions permissions) {
        super.addPermissionOverwrite(user, permissions);
        return this;
    }

    @Override
    public ServerVoiceChannelUpdater addPermissionOverwrite(Role role, Permissions permissions) {
        super.addPermissionOverwrite(role, permissions);
        return this;
    }

    @Override
    public ServerVoiceChannelUpdater removePermissionOverwrite(User user) {
        super.removePermissionOverwrite(user);
        return this;
    }

    @Override
    public ServerVoiceChannelUpdater removePermissionOverwrite(Role role) {
        super.removePermissionOverwrite(role);
        return this;
    }

    @Override
    public CompletableFuture<Void> update() {
        boolean patchChannel = false;
        ObjectNode body = JsonNodeFactory.instance.objectNode();
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
        if (bitrate != null) {
            body.put("bitrate", bitrate.intValue());
            patchChannel = true;
        }
        if (userLimit != null) {
            body.put("user_limit", userLimit.intValue());
            patchChannel = true;
        }
        if (modifyCategory) {
            body.put("parent_id", category == null ? null : category.getIdAsString());
            patchChannel = true;
        }
        if (patchChannel) {
            return new RestRequest<Void>(channel.getApi(), RestMethod.PATCH, RestEndpoint.CHANNEL)
                    .setUrlParameters(channel.getIdAsString())
                    .setBody(body)
                    .setAuditLogReason(reason)
                    .execute(result -> null);
        } else {
            return CompletableFuture.completedFuture(null);
        }
    }

}

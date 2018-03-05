package org.javacord.entity.channel.impl;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.entity.channel.ChannelCategory;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.channel.ServerTextChannelUpdater;
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
 * The implementation of {@link ServerTextChannelUpdater}.
 */
public class ImplServerTextChannelUpdater extends ImplServerChannelUpdater implements ServerTextChannelUpdater{

    /**
     * The topic to update.
     */
    protected String topic = null;

    /**
     * The nsfw flag to update.
     */
    protected Boolean nsfw = null;

    /**
     * The category to update.
     */
    protected ChannelCategory category = null;

    /**
     * Whether the category should be modified or not.
     */
    protected boolean modifyCategory = false;

    /**
     * Creates a new server text channel updater.
     *
     * @param channel The channel to update.
     */
    public ImplServerTextChannelUpdater(ServerTextChannel channel) {
        super(channel);
    }

    @Override
    public ServerTextChannelUpdater setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    @Override
    public ServerTextChannelUpdater setNsfwFlag(boolean nsfw) {
        this.nsfw = nsfw;
        return this;
    }

    @Override
    public ServerTextChannelUpdater setCategory(ChannelCategory category) {
        this.category = category;
        this.modifyCategory = true;
        return this;
    }

    @Override
    public ServerTextChannelUpdater removeCategory() {
        return setCategory(null);
    }

    @Override
    public ServerTextChannelUpdater setAuditLogReason(String reason) {
        super.setAuditLogReason(reason);
        return this;
    }

    @Override
    public ServerTextChannelUpdater setName(String name) {
        super.setName(name);
        return this;
    }

    @Override
    public ServerTextChannelUpdater setRawPosition(int rawPosition) {
        super.setRawPosition(rawPosition);
        return this;
    }

    @Override
    public ServerTextChannelUpdater addPermissionOverwrite(User user, Permissions permissions) {
        super.addPermissionOverwrite(user, permissions);
        return this;
    }

    @Override
    public ServerTextChannelUpdater addPermissionOverwrite(Role role, Permissions permissions) {
        super.addPermissionOverwrite(role, permissions);
        return this;
    }

    @Override
    public ServerTextChannelUpdater removePermissionOverwrite(User user) {
        super.removePermissionOverwrite(user);
        return this;
    }

    @Override
    public ServerTextChannelUpdater removePermissionOverwrite(Role role) {
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
                        .put("allow", ((ImplPermissions) entry.getValue()).getAllowed())
                        .put("deny", ((ImplPermissions) entry.getValue()).getDenied());
            }
        }
        if (overwrittenRolePermissions != null) {
            for (Map.Entry<Long, Permissions> entry : overwrittenRolePermissions.entrySet()) {
                permissionOverwrites.addObject()
                        .put("id", Long.toUnsignedString(entry.getKey()))
                        .put("type", "role")
                        .put("allow", ((ImplPermissions) entry.getValue()).getAllowed())
                        .put("deny", ((ImplPermissions) entry.getValue()).getDenied());
            }
        }
        if (topic != null) {
            body.put("topic", topic);
            patchChannel = true;
        }
        if (nsfw != null) {
            body.put("nsfw", nsfw.booleanValue());
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

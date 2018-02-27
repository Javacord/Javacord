package de.btobastian.javacord.entity.channel;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.btobastian.javacord.entity.permission.Permissions;
import de.btobastian.javacord.entity.permission.Role;
import de.btobastian.javacord.entity.permission.impl.ImplPermissions;
import de.btobastian.javacord.entity.user.User;
import de.btobastian.javacord.util.rest.RestEndpoint;
import de.btobastian.javacord.util.rest.RestMethod;
import de.btobastian.javacord.util.rest.RestRequest;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update server voice channels.
 */
public class ServerVoiceChannelUpdater extends ServerChannelUpdater {

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
    public ServerVoiceChannelUpdater(ServerVoiceChannel channel) {
        super(channel);
    }

    /**
     * Queues the bitrate to be updated.
     *
     * @param bitrate The new bitrate of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerVoiceChannelUpdater setBitrate(int bitrate) {
        this.bitrate = bitrate;
        return this;
    }

    /**
     * Queues the user limit to be updated.
     *
     * @param userLimit The new user limit of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerVoiceChannelUpdater setUserLimit(int userLimit) {
        this.userLimit = userLimit;
        return this;
    }

    /**
     * Queues the user limit to be removed.
     *
     * @return The current instance in order to chain call methods.
     */
    public ServerVoiceChannelUpdater removeUserLimit() {
        this.userLimit = 0;
        return this;
    }

    /**
     * Queues the category to be updated.
     *
     * @param category The new category of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerVoiceChannelUpdater setCategory(ChannelCategory category) {
        this.category = category;
        this.modifyCategory = true;
        return this;
    }

    /**
     * Queues the category to be removed.
     *
     * @return The current instance in order to chain call methods.
     */
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

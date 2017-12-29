package de.btobastian.javacord.entities.channels;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.permissions.Permissions;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.entities.permissions.impl.ImplPermissions;
import de.btobastian.javacord.utils.rest.RestEndpoint;
import de.btobastian.javacord.utils.rest.RestMethod;
import de.btobastian.javacord.utils.rest.RestRequest;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update server text channels.
 */
public class ServerTextChannelUpdater extends ServerChannelUpdater {

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
    public ServerTextChannelUpdater(ServerTextChannel channel) {
        super(channel);
    }

    /**
     * Queues the topic to be updated.
     *
     * @param topic The new topic of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerTextChannelUpdater setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    /**
     * Queues the nsfw to be updated.
     *
     * @param nsfw The new nsfw flag of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerTextChannelUpdater setNsfwFlag(boolean nsfw) {
        this.nsfw = nsfw;
        return this;
    }

    /**
     * Queues the category to be updated.
     *
     * @param category The new category of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerTextChannelUpdater setCategory(ChannelCategory category) {
        this.category = category;
        this.modifyCategory = true;
        return this;
    }

    /**
     * Queues the category to be removed.
     *
     * @return The current instance in order to chain call methods.
     */
    public ServerTextChannelUpdater removeCategory() {
        return setCategory(null);
    }

    @Override
    public ServerTextChannelUpdater setName(String name) {
        super.setName(name);
        return this;
    }

    @Override
    public ServerTextChannelUpdater setPosition(int position) {
        super.setPosition(position);
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
                        .put("id", String.valueOf(entry.getKey().longValue()))
                        .put("type", "member")
                        .put("allow", ((ImplPermissions) entry.getValue()).getAllowed())
                        .put("deny", ((ImplPermissions) entry.getValue()).getDenied());
            }
        }
        if (overwrittenRolePermissions != null) {
            for (Map.Entry<Long, Permissions> entry : overwrittenRolePermissions.entrySet()) {
                permissionOverwrites.addObject()
                        .put("id", String.valueOf(entry.getKey().longValue()))
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
                    .execute(result -> null);
        } else {
            return CompletableFuture.completedFuture(null);
        }
    }

}

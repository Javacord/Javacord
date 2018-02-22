package de.btobastian.javacord.entities.channels.impl;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.ChannelCategory;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.entities.permissions.Permissions;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.entities.permissions.impl.ImplPermissions;
import de.btobastian.javacord.utils.Cleanupable;
import de.btobastian.javacord.utils.cache.ImplMessageCache;
import de.btobastian.javacord.utils.cache.MessageCache;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The implementation of {@link ServerTextChannel}.
 */
public class ImplServerTextChannel implements ServerTextChannel, Cleanupable {

    /**
     * The discord api instance.
     */
    private final ImplDiscordApi api;

    /**
     * The id of the channel.
     */
    private final long id;

    /**
     * The name of the channel.
     */
    private String name;

    /**
     * The server of the channel.
     */
    private final ImplServer server;

    /**
     * The position of the channel.
     */
    private int position;

    /**
     * The message cache of the server text channel.
     */
    private final ImplMessageCache messageCache;

    /**
     * Whether the channel is "not safe for work" or not.
     */
    private boolean nsfw;

    /**
     * The parent id of the channel.
     */
    private long parentId;

    /**
     * The topic of the channel.
     */
    private String topic;

    /**
     * A map with all overwritten user permissions.
     */
    private final ConcurrentHashMap<Long, Permissions> overwrittenUserPermissions = new ConcurrentHashMap<>();

    /**
     * A map with all overwritten role permissions.
     */
    private final ConcurrentHashMap<Long, Permissions> overwrittenRolePermissions = new ConcurrentHashMap<>();

    /**
     * Creates a new server text channel object.
     *
     * @param api The discord api instance.
     * @param server The server of the channel.
     * @param data The json data of the channel.
     */
    public ImplServerTextChannel(ImplDiscordApi api, ImplServer server, JsonNode data) {
        this.api = api;
        this.server = server;
        this.messageCache = new ImplMessageCache(
                api, api.getDefaultMessageCacheCapacity(), api.getDefaultMessageCacheStorageTimeInSeconds());

        id = Long.parseLong(data.get("id").asText());
        name = data.get("name").asText();
        position = data.get("position").asInt();
        nsfw = data.has("nsfw") && data.get("nsfw").asBoolean();
        parentId = Long.valueOf(data.has("parent_id") ? data.get("parent_id").asText("-1") : "-1");
        topic = data.has("topic") && !data.get("topic").isNull() ? data.get("topic").asText() : "";

        if (data.has("permission_overwrites")) {
            for (JsonNode permissionOverwrite : data.get("permission_overwrites")) {
                long id = Long.parseLong(permissionOverwrite.has("id") ? permissionOverwrite.get("id").asText() : "-1");
                int allow = permissionOverwrite.has("allow") ? permissionOverwrite.get("allow").asInt() : 0;
                int deny = permissionOverwrite.has("deny") ? permissionOverwrite.get("deny").asInt() : 0;
                Permissions permissions = new ImplPermissions(allow, deny);
                switch (permissionOverwrite.get("type").asText()) {
                    case "role":
                        overwrittenRolePermissions.put(id, permissions);
                        break;
                    case "member":
                        overwrittenUserPermissions.put(id, permissions);
                        break;
                }
            }
        }

        server.addChannelToCache(this);
    }

    /**
     * Sets the name of the channel.
     *
     * @param name The new name of the channel.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the position of the channel.
     *
     * @param position The new position of the channel.
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Sets the topic of the channel.
     *
     * @param topic The new topic of the channel.
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
     * Sets the nsfw flag.
     *
     * @param nsfw The nsfw flag.
     */
    public void setNsfwFlag(boolean nsfw) {
        this.nsfw = nsfw;
    }

    /**
     * Sets the parent id of the channel.
     *
     * @param parentId The parent id to set.
     */
    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    /**
     * Gets the overwritten role permissions.
     *
     * @return The overwritten role permissions.
     */
    public ConcurrentHashMap<Long, Permissions> getOverwrittenRolePermissions() {
        return overwrittenRolePermissions;
    }

    /**
     * Gets the overwritten user permissions.
     *
     * @return The overwritten user permissions.
     */
    public ConcurrentHashMap<Long, Permissions> getOverwrittenUserPermissions() {
        return overwrittenUserPermissions;
    }

    @Override
    public DiscordApi getApi() {
        return api;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public boolean isNsfw() {
        return nsfw;
    }

    @Override
    public Optional<ChannelCategory> getCategory() {
        return getServer().getChannelCategoryById(parentId);
    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public int getRawPosition() {
        return position;
    }

    @Override
    public Permissions getOverwrittenPermissions(User user) {
        return overwrittenUserPermissions.getOrDefault(user.getId(), ImplPermissions.EMPTY_PERMISSIONS);
    }

    @Override
    public Permissions getOverwrittenPermissions(Role role) {
        return overwrittenRolePermissions.getOrDefault(role.getId(), ImplPermissions.EMPTY_PERMISSIONS);
    }

    @Override
    public MessageCache getMessageCache() {
        return messageCache;
    }

    @Override
    public String getMentionTag() {
        return "<#" + getId() + ">";
    }

    @Override
    public void cleanup() {
        messageCache.cleanup();
    }

    @Override
    public String toString() {
        return String.format("ServerTextChannel (id: %s, name: %s)", getId(), getName());
    }

}

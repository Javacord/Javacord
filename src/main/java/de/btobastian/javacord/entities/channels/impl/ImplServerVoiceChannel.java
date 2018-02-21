package de.btobastian.javacord.entities.channels.impl;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.ChannelCategory;
import de.btobastian.javacord.entities.channels.ServerVoiceChannel;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.entities.permissions.Permissions;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.entities.permissions.impl.ImplPermissions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The implementation of {@link ServerVoiceChannel}.
 */
public class ImplServerVoiceChannel implements ServerVoiceChannel {

    /**
     * The discord api instance.
     */
    private final ImplDiscordApi api;

    /**
     * The id of the channel.
     */
    private final long id;

    /**
     * The bitrate of the channel.
     */
    private int bitrate;

    /**
     * The userLimit of the channel.
     */
    private int userLimit;

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
     * The parent id of the channel.
     */
    private long parentId;

    /**
     * A map with all overwritten user permissions.
     */
    private final ConcurrentHashMap<Long, Permissions> overwrittenUserPermissions = new ConcurrentHashMap<>();

    /**
     * A map with all overwritten role permissions.
     */
    private final ConcurrentHashMap<Long, Permissions> overwrittenRolePermissions = new ConcurrentHashMap<>();

    /**
     * The connected users of this server voice channel.
     */
    private final Collection<User> connectedUsers = new ArrayList<>();

    /**
     * Creates a new server voice channel object.
     *
     * @param api The discord api instance.
     * @param server The server of the channel.
     * @param data The json data of the channel.
     */
    public ImplServerVoiceChannel(ImplDiscordApi api, ImplServer server, JsonNode data) {
        this.api = api;
        this.server = server;
        position = data.get("position").asInt();

        id = Long.parseLong(data.get("id").asText());
        bitrate = data.get("bitrate").asInt();
        userLimit = data.get("user_limit").asInt();
        name = data.get("name").asText();
        parentId = Long.valueOf(data.has("parent_id") ? data.get("parent_id").asText("-1") : "-1");

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
     * Sets the bitrate of the channel.
     *
     * @param bitrate The new bitrate of the channel.
     */
    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    /**
     * Sets the user limit of the channel.
     *
     * @param userLimit The user limit to set.
     */
    public void setUserLimit(int userLimit) {
        this.userLimit = userLimit;
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

    /**
     * Adds the given user to the list of connected users.
     *
     * @param user The user to add.
     */
    public void addConnectedUser(User user) {
        connectedUsers.add(user);
    }

    /**
     * Removes the given user from the list of connected users.
     *
     * @param user The user to remove.
     */
    public void removeConnectedUser(User user) {
        connectedUsers.remove(user);
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
    public Optional<ChannelCategory> getCategory() {
        return getServer().getChannelCategoryById(parentId);
    }

    @Override
    public int getBitrate() {
        return bitrate;
    }

    @Override
    public Optional<Integer> getUserLimit() {
        return userLimit == 0 ? Optional.empty() : Optional.of(userLimit);
    }

    @Override
    public Collection<User> getConnectedUsers() {
        return Collections.unmodifiableCollection(connectedUsers);
    }

    @Override
    public String toString() {
        return String.format("ServerVoiceChannel (id: %s, name: %s)", getId(), getName());
    }

}

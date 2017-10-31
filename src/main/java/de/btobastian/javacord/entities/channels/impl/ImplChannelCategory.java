package de.btobastian.javacord.entities.channels.impl;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.ChannelCategory;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.entities.permissions.Permissions;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.entities.permissions.impl.ImplPermissions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ConcurrentHashMap;

/**
 * The implementation of {@link ChannelCategory}.
 */
public class ImplChannelCategory implements ChannelCategory {

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
     * Whether the category is "not safe for work" or not.
     */
    private boolean nsfw = false;

    /**
     * A map with all overwritten user permissions.
     */
    private final ConcurrentHashMap<Long, Permissions> overwrittenUserPermissions = new ConcurrentHashMap<>();

    /**
     * A map with all overwritten role permissions.
     */
    private final ConcurrentHashMap<Long, Permissions> overwrittenRolePermissions = new ConcurrentHashMap<>();

    /**
     * Creates a new server channel category object.
     *
     * @param api The discord api instance.
     * @param server The server of the channel.
     * @param data The json data of the channel.
     */
    public ImplChannelCategory(ImplDiscordApi api, ImplServer server, JSONObject data) {
        this.api = api;
        this.server = server;

        id = Long.parseLong(data.getString("id"));
        name = data.getString("name");
        position = data.getInt("position");
        nsfw = data.has("nsfw") && data.getBoolean("nsfw");

        JSONArray permissionOverwritesJson = data.optJSONArray("permission_overwrites");
        permissionOverwritesJson = permissionOverwritesJson == null ? new JSONArray() : permissionOverwritesJson;
        for (int i = 0; i < permissionOverwritesJson.length(); i++) {
            JSONObject permissionOverwrite = permissionOverwritesJson.getJSONObject(i);
            long id = Long.parseLong(permissionOverwrite.optString("id", "-1"));
            int allow = permissionOverwrite.optInt("allow", 0);
            int deny = permissionOverwrite.optInt("deny", 0);
            Permissions permissions = new ImplPermissions(allow, deny);
            switch (permissionOverwrite.getString("type")) {
                case "role":
                    overwrittenRolePermissions.put(id, permissions);
                    break;
                case "member":
                    overwrittenUserPermissions.put(id, permissions);
                    break;
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
    public String toString() {
        return String.format("ChannelCategory (id: %s, name: %s)", getId(), getName());
    }

}

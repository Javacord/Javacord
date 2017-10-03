package de.btobastian.javacord.entities.permissions.impl;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.impl.ImplServer;
import de.btobastian.javacord.entities.permissions.Permissions;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.javacord.listeners.server.role.RoleChangePermissionsListener;
import de.btobastian.javacord.listeners.server.role.RoleChangePositionListener;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * The implementation of {@link Role}.
 */
public class ImplRole implements Role {

    /**
     * The discord api instance.
     */
    private final ImplDiscordApi api;

    /**
     * The server of the role.
     */
    private final ImplServer server;

    /**
     * The id of the role.
     */
    private final long id;

    /**
     * The name of the role.
     */
    private String name;

    /**
     * The position of the role.
     */
    private int position;

    /**
     * The color of the role.
     */
    private int color;

    /**
     * Whether this role is pinned in the user listing or not.
     */
    private boolean hoist;

    /**
     * The permissions of the role.
     */
    private ImplPermissions permissions;

    /**
     * A collection with all users with this role.
     */
    private final Collection<User> users = new HashSet<>();

    /**
     * A map which contains all listeners.
     * The key is the class of the listener.
     */
    private final ConcurrentHashMap<Class<?>, List<Object>> listeners = new ConcurrentHashMap<>();

    /**
     * Creates a new role object.
     *
     * @param api The discord api instance.
     * @param server The server of the role.
     * @param data The json data of the role.
     */
    public ImplRole(ImplDiscordApi api, ImplServer server, JSONObject data) {
        this.api = api;
        this.server = server;
        this.id = Long.parseLong(data.getString("id"));
        this.name = data.getString("name");
        this.position = data.getInt("position");
        this.color = data.optInt("color", 0);
        this.hoist = data.optBoolean("hoist", false);
        this.permissions = new ImplPermissions(data.getInt("permissions"), 0);
    }

    /**
     * Adds a user to the role.
     *
     * @param user The user to add.
     */
    public void addUserToCache(User user) {
        users.add(user);
    }

    /**
     * Removes a user from the role.
     *
     * @param user The user to remove.
     */
    public void removeUserFromCache(User user) {
        users.remove(user);
    }

    /**
     * Sets the permissions of the role.
     *
     * @param permissions The permissions to set.
     */
    public void setPermissions(ImplPermissions permissions) {
        this.permissions = permissions;
    }

    /**
     * Sets the position of the role.
     *
     * @param position The position to set.
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Adds a listener.
     *
     * @param clazz The listener class.
     * @param listener The listener to add.
     */
    private void addListener(Class<?> clazz, Object listener) {
        List<Object> classListeners = listeners.computeIfAbsent(clazz, c -> new ArrayList<>());
        classListeners.add(listener);
    }

    /**
     * Gets all listeners of the given class.
     *
     * @param clazz The class of the listener.
     * @param <T> The class of the listener.
     * @return A list with all listeners of the given type.
     */
    @SuppressWarnings("unchecked") // We make sure it's the right type when adding elements
    private <T> List<T> getListeners(Class<?> clazz) {
        List<Object> classListeners = listeners.getOrDefault(clazz, new ArrayList<>());
        return classListeners.stream().map(o -> (T) o).collect(Collectors.toCollection(ArrayList::new));
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
    public Server getServer() {
        return server;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public Optional<Color> getColor() {
        return Optional.ofNullable(color == 0 ? null : new Color(color));
    }

    @Override
    public boolean isDisplayedSeparately() {
        return hoist;
    }

    @Override
    public Collection<User> getUsers() {
        if (getName().equals("@everyone")) {
            return getServer().getMembers();
        }
        return Collections.unmodifiableCollection(users);
    }

    @Override
    public Permissions getPermissions() {
        return permissions;
    }

    @Override
    public String toString() {
        return String.format("Role (id: %s, name: %s, server: %s)", getId(), getName(), getServer());
    }

    @Override
    public void addRoleChangePermissionsListener(RoleChangePermissionsListener listener) {
        addListener(RoleChangePermissionsListener.class, listener);
    }

    @Override
    public java.util.List<RoleChangePermissionsListener> getRoleChangePermissionsListeners() {
        return getListeners(RoleChangePermissionsListener.class);
    }

    @Override
    public void addRoleChangePositionListener(RoleChangePositionListener listener) {
        addListener(RoleChangePositionListener.class, listener);
    }

    @Override
    public List<RoleChangePositionListener> getRoleChangePositionListeners() {
        return getListeners(RoleChangePositionListener.class);
    }
}

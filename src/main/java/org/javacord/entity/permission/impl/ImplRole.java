package org.javacord.entity.permission.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.DiscordApi;
import org.javacord.ImplDiscordApi;
import org.javacord.entity.DiscordEntity;
import org.javacord.entity.permission.Permissions;
import org.javacord.entity.permission.Role;
import org.javacord.entity.server.Server;
import org.javacord.entity.server.impl.ImplServer;
import org.javacord.entity.user.User;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.channel.server.ServerChannelChangeOverwrittenPermissionsListener;
import org.javacord.listener.server.role.RoleAttachableListener;
import org.javacord.listener.server.role.RoleChangeColorListener;
import org.javacord.listener.server.role.RoleChangeHoistListener;
import org.javacord.listener.server.role.RoleChangeMentionableListener;
import org.javacord.listener.server.role.RoleChangeNameListener;
import org.javacord.listener.server.role.RoleChangePermissionsListener;
import org.javacord.listener.server.role.RoleChangePositionListener;
import org.javacord.listener.server.role.RoleDeleteListener;
import org.javacord.listener.server.role.UserRoleAddListener;
import org.javacord.listener.server.role.UserRoleRemoveListener;
import org.javacord.util.ClassHelper;
import org.javacord.util.event.ListenerManager;
import org.javacord.util.rest.RestEndpoint;
import org.javacord.util.rest.RestMethod;
import org.javacord.util.rest.RestRequest;

import java.awt.Color;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
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
     * Whether this role can be mentioned or not.
     */
    private boolean mentionable;

    /**
     * The permissions of the role.
     */
    private ImplPermissions permissions;

    /**
     * Whether this role is managed by an integration or not.
     */
    private boolean managed;

    /**
     * A collection with all users with this role.
     */
    private final Collection<User> users = new HashSet<>();

    /**
     * Creates a new role object.
     *
     * @param api The discord api instance.
     * @param server The server of the role.
     * @param data The json data of the role.
     */
    public ImplRole(ImplDiscordApi api, ImplServer server, JsonNode data) {
        this.api = api;
        this.server = server;
        this.id = data.get("id").asLong();
        this.name = data.get("name").asText();
        this.position = data.get("position").asInt();
        this.color = data.get("color").asInt(0);
        this.hoist = data.get("hoist").asBoolean(false);
        this.mentionable = data.get("mentionable").asBoolean(false);
        this.permissions = new ImplPermissions(data.get("permissions").asInt(), 0);
        this.managed = data.get("managed").asBoolean(false);
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
     * Gets the color of the role as {@code int}.
     *
     * @return The color of the role as {@code int}.
     */
    public int getColorAsInt() {
        return color;
    }

    /**
     * Sets the color of the role.
     *
     * @param color The color to set.
     */
    public void setColor(int color) {
        this.color = color;
    }

    /**
     * Sets the hoist flag of the role.
     *
     * @param hoist The hoist flag to set.
     */
    public void setHoist(boolean hoist) {
        this.hoist = hoist;
    }

    /**
     * Sets the managed flag of the role.
     *
     * @param managed The managed flag to set.
     */
    public void setManaged(boolean managed) {
        this.managed = managed;
    }

    /**
     * Sets the mentionable flag of the role.
     *
     * @param mentionable The mentionable flag to set.
     */
    public void setMentionable(boolean mentionable) {
        this.mentionable = mentionable;
    }

    /**
     * Sets the name of the role.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
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
    public boolean isMentionable() {
        return mentionable;
    }

    @Override
    public boolean isDisplayedSeparately() {
        return hoist;
    }

    @Override
    public Collection<User> getUsers() {
        if (isEveryoneRole()) {
            return getServer().getMembers();
        }
        return Collections.unmodifiableCollection(users);
    }

    @Override
    public Permissions getPermissions() {
        return permissions;
    }

    @Override
    public boolean isManaged() {
        return managed;
    }

    @Override
    public CompletableFuture<Void> delete() {
        return new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.ROLE)
                .setUrlParameters(getServer().getIdAsString(), getIdAsString())
                .execute(result -> null);
    }

    @Override
    public ListenerManager<RoleChangeColorListener> addRoleChangeColorListener(RoleChangeColorListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Role.class, getId(), RoleChangeColorListener.class, listener);
    }

    @Override
    public List<RoleChangeColorListener> getRoleChangeColorListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Role.class, getId(), RoleChangeColorListener.class);
    }

    @Override
    public ListenerManager<RoleChangeHoistListener> addRoleChangeHoistListener(RoleChangeHoistListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Role.class, getId(), RoleChangeHoistListener.class, listener);
    }

    @Override
    public List<RoleChangeHoistListener> getRoleChangeHoistListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Role.class, getId(), RoleChangeHoistListener.class);
    }

    @Override
    public ListenerManager<RoleChangeMentionableListener> addRoleChangeMentionableListener(
            RoleChangeMentionableListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Role.class, getId(), RoleChangeMentionableListener.class, listener);
    }

    @Override
    public List<RoleChangeMentionableListener> getRoleChangeMentionableListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Role.class, getId(), RoleChangeMentionableListener.class);
    }

    @Override
    public ListenerManager<RoleChangeNameListener> addRoleChangeNameListener(RoleChangeNameListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Role.class, getId(), RoleChangeNameListener.class, listener);
    }

    @Override
    public List<RoleChangeNameListener> getRoleChangeNameListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Role.class, getId(), RoleChangeNameListener.class);
    }

    @Override
    public ListenerManager<RoleChangePermissionsListener> addRoleChangePermissionsListener(
            RoleChangePermissionsListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Role.class, getId(), RoleChangePermissionsListener.class, listener);
    }

    @Override
    public List<RoleChangePermissionsListener> getRoleChangePermissionsListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Role.class, getId(), RoleChangePermissionsListener.class);
    }

    @Override
    public ListenerManager<RoleChangePositionListener> addRoleChangePositionListener(
            RoleChangePositionListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Role.class, getId(), RoleChangePositionListener.class, listener);
    }

    @Override
    public java.util.List<RoleChangePositionListener> getRoleChangePositionListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Role.class, getId(), RoleChangePositionListener.class);
    }

    @Override
    public ListenerManager<ServerChannelChangeOverwrittenPermissionsListener>
    addServerChannelChangeOverwrittenPermissionsListener(ServerChannelChangeOverwrittenPermissionsListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                Role.class, getId(), ServerChannelChangeOverwrittenPermissionsListener.class, listener);
    }

    @Override
    public java.util.List<ServerChannelChangeOverwrittenPermissionsListener>
            getServerChannelChangeOverwrittenPermissionsListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                Role.class, getId(), ServerChannelChangeOverwrittenPermissionsListener.class);
    }

    @Override
    public ListenerManager<RoleDeleteListener> addRoleDeleteListener(RoleDeleteListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Role.class, getId(), RoleDeleteListener.class, listener);
    }

    @Override
    public java.util.List<RoleDeleteListener> getRoleDeleteListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Role.class, getId(), RoleDeleteListener.class);
    }

    @Override
    public ListenerManager<UserRoleAddListener> addUserRoleAddListener(UserRoleAddListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(Role.class, getId(), UserRoleAddListener.class, listener);
    }

    @Override
    public java.util.List<UserRoleAddListener> getUserRoleAddListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Role.class, getId(), UserRoleAddListener.class);
    }

    @Override
    public ListenerManager<UserRoleRemoveListener> addUserRoleRemoveListener(UserRoleRemoveListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(Role.class, getId(), UserRoleRemoveListener.class, listener);
    }

    @Override
    public java.util.List<UserRoleRemoveListener> getUserRoleRemoveListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Role.class, getId(), UserRoleRemoveListener.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends RoleAttachableListener & ObjectAttachableListener> Collection<ListenerManager<T>>
    addRoleAttachableListener(T listener) {
        return ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(RoleAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .map(listenerClass -> ((ImplDiscordApi) getApi()).addObjectListener(Role.class, getId(),
                                                                                    listenerClass, listener))
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends RoleAttachableListener & ObjectAttachableListener> void removeRoleAttachableListener(T listener) {
        ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(RoleAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .forEach(listenerClass -> ((ImplDiscordApi) getApi()).removeObjectListener(Role.class, getId(),
                                                                                           listenerClass, listener));
    }

    @Override
    public <T extends RoleAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
    getRoleAttachableListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Role.class, getId());
    }

    @Override
    public <T extends RoleAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener) {
        ((ImplDiscordApi) getApi()).removeObjectListener(Role.class, getId(), listenerClass, listener);
    }

    @Override
    public boolean equals(Object o) {
        return (this == o)
               || !((o == null)
                    || (getClass() != o.getClass())
                    || (getId() != ((DiscordEntity) o).getId()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return String.format("Role (id: %s, name: %s, server: %s)", getIdAsString(), getName(), getServer());
    }

}

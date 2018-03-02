package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.permission.PermissionState;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.server.invite.RichInvite;
import org.javacord.api.entity.user.User;
import org.javacord.api.listener.ChannelAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.server.ServerChannelAttachableListener;
import org.javacord.api.listener.channel.server.ServerChannelChangeNameListener;
import org.javacord.api.listener.channel.server.ServerChannelChangeOverwrittenPermissionsListener;
import org.javacord.api.listener.channel.server.ServerChannelChangePositionListener;
import org.javacord.api.listener.channel.server.ServerChannelDeleteListener;
import org.javacord.api.util.event.ListenerManager;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.permission.PermissionsImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.entity.server.invite.InviteImpl;
import org.javacord.core.util.ClassHelper;
import org.javacord.core.util.logging.LoggerUtil;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The implementation of {@link ServerChannel}.
 */
public abstract class ServerChannelImpl implements ServerChannel, InternalChannel {

    /**
     * The discord api instance.
     */
    private final DiscordApiImpl api;

    /**
     * The id of the channel.
     */
    private final long id;

    /**
     * The name of the channel.
     */
    private volatile String name;

    /**
     * The server of the channel.
     */
    private final ServerImpl server;

    /**
     * The position of the channel.
     */
    private volatile int position;

    /**
     * A map with all overwritten user permissions.
     */
    private final ConcurrentHashMap<Long, Permissions> overwrittenUserPermissions = new ConcurrentHashMap<>();

    /**
     * A map with all overwritten role permissions.
     */
    private final ConcurrentHashMap<Long, Permissions> overwrittenRolePermissions = new ConcurrentHashMap<>();

    /**
     * Creates a new server channel object.
     *
     * @param api The discord api instance.
     * @param server The server of the channel.
     * @param data The json data of the channel.
     */
    public ServerChannelImpl(DiscordApiImpl api, ServerImpl server, JsonNode data) {
        this.api = api;
        this.server = server;

        id = Long.parseLong(data.get("id").asText());
        name = data.get("name").asText();
        position = data.get("position").asInt();

        if (data.has("permission_overwrites")) {
            for (JsonNode permissionOverwrite : data.get("permission_overwrites")) {
                long id = Long.parseLong(permissionOverwrite.has("id") ? permissionOverwrite.get("id").asText() : "-1");
                int allow = permissionOverwrite.has("allow") ? permissionOverwrite.get("allow").asInt() : 0;
                int deny = permissionOverwrite.has("deny") ? permissionOverwrite.get("deny").asInt() : 0;
                Permissions permissions = new PermissionsImpl(allow, deny);
                switch (permissionOverwrite.get("type").asText()) {
                    case "role":
                        overwrittenRolePermissions.put(id, permissions);
                        break;
                    case "member":
                        overwrittenUserPermissions.put(id, permissions);
                        break;
                    default:
                        LoggerUtil.getLogger(ServerChannelImpl.class).warn("Unknown type for permission_overwrites. "
                                + "Your Javacord version might be outdated.");
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
     * Gets the overwritten role permissions.
     *
     * @return The overwritten role permissions.
     */
    public ConcurrentHashMap<Long, Permissions> getInternalOverwrittenRolePermissions() {
        return overwrittenRolePermissions;
    }

    /**
     * Gets the overwritten user permissions.
     *
     * @return The overwritten user permissions.
     */
    public ConcurrentHashMap<Long, Permissions> getInternalOverwrittenUserPermissions() {
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
    public ListenerManager<ServerChannelDeleteListener> addServerChannelDeleteListener(
            ServerChannelDeleteListener listener) {
        return ((DiscordApiImpl) getApi()).addObjectListener(
                ServerChannel.class, getId(), ServerChannelDeleteListener.class, listener);
    }

    @Override
    public List<ServerChannelDeleteListener> getServerChannelDeleteListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(
                ServerChannel.class, getId(), ServerChannelDeleteListener.class);
    }

    @Override
    public ListenerManager<ServerChannelChangeNameListener> addServerChannelChangeNameListener(
            ServerChannelChangeNameListener listener) {
        return ((DiscordApiImpl) getApi()).addObjectListener(
                ServerChannel.class, getId(), ServerChannelChangeNameListener.class, listener);
    }

    @Override
    public List<ServerChannelChangeNameListener> getServerChannelChangeNameListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(
                ServerChannel.class, getId(), ServerChannelChangeNameListener.class);
    }

    @Override
    public ListenerManager<ServerChannelChangePositionListener> addServerChannelChangePositionListener(
            ServerChannelChangePositionListener listener) {
        return ((DiscordApiImpl) getApi()).addObjectListener(
                ServerChannel.class, getId(), ServerChannelChangePositionListener.class, listener);
    }

    @Override
    public List<ServerChannelChangePositionListener> getServerChannelChangePositionListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(
                ServerChannel.class, getId(), ServerChannelChangePositionListener.class);
    }

    @Override
    public ListenerManager<ServerChannelChangeOverwrittenPermissionsListener>
            addServerChannelChangeOverwrittenPermissionsListener(
                    ServerChannelChangeOverwrittenPermissionsListener listener) {
        return ((DiscordApiImpl) getApi()).addObjectListener(
                ServerChannel.class, getId(), ServerChannelChangeOverwrittenPermissionsListener.class, listener);
    }

    @Override
    public List<ServerChannelChangeOverwrittenPermissionsListener>
            getServerChannelChangeOverwrittenPermissionsListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(
                ServerChannel.class, getId(), ServerChannelChangeOverwrittenPermissionsListener.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ServerChannelAttachableListener & ObjectAttachableListener>
            Collection<ListenerManager<? extends ServerChannelAttachableListener>>
                    addServerChannelAttachableListener(T listener) {
        return ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(ServerChannelAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .flatMap(listenerClass -> {
                    if (ChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        return addChannelAttachableListener(
                                (ChannelAttachableListener & ObjectAttachableListener) listener).stream();
                    } else {
                        return Stream.of(((DiscordApiImpl) getApi()).addObjectListener(ServerChannel.class, getId(),
                                                                                       listenerClass, listener));
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ServerChannelAttachableListener & ObjectAttachableListener> void
            removeServerChannelAttachableListener(T listener) {
        ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(ServerChannelAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .forEach(listenerClass -> {
                    if (ChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        removeChannelAttachableListener(
                                (ChannelAttachableListener & ObjectAttachableListener) listener);
                    } else {
                        ((DiscordApiImpl) getApi()).removeObjectListener(ServerChannel.class, getId(),
                                                                         listenerClass, listener);
                    }
                });
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ServerChannelAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
            getServerChannelAttachableListeners() {
        Map<T, List<Class<T>>> serverChannelListeners =
                ((DiscordApiImpl) getApi()).getObjectListeners(ServerChannel.class, getId());
        getChannelAttachableListeners().forEach((listener, listenerClasses) -> serverChannelListeners
                .merge((T) listener,
                        (List<Class<T>>) (Object) listenerClasses,
                        (listenerClasses1, listenerClasses2) -> {
                            listenerClasses1.addAll(listenerClasses2);
                            return listenerClasses1;
                        }));
        return serverChannelListeners;
    }

    @Override
    public <T extends ServerChannelAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener) {
        ((DiscordApiImpl) getApi()).removeObjectListener(ServerChannel.class, getId(), listenerClass, listener);
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
    public CompletableFuture<Collection<RichInvite>> getInvites() {
        return new RestRequest<Collection<RichInvite>>(getApi(), RestMethod.GET, RestEndpoint.CHANNEL_INVITE)
                .setUrlParameters(getIdAsString())
                .execute(result -> {
                    Collection<RichInvite> invites = new HashSet<>();
                    for (JsonNode inviteJson : result.getJsonBody()) {
                        invites.add(new InviteImpl(getApi(), inviteJson));
                    }
                    return Collections.unmodifiableCollection(invites);
                });
    }

    @Override
    public Permissions getOverwrittenPermissions(User user) {
        return overwrittenUserPermissions.getOrDefault(user.getId(), PermissionsImpl.EMPTY_PERMISSIONS);
    }

    @Override
    public Permissions getOverwrittenPermissions(Role role) {
        return overwrittenRolePermissions.getOrDefault(role.getId(), PermissionsImpl.EMPTY_PERMISSIONS);
    }

    @Override
    public Map<User, Permissions> getOverwrittenUserPermissions() {
        Server server = getServer();
        return Collections.unmodifiableMap(
                overwrittenUserPermissions.entrySet().stream()
                        .collect(Collectors.toMap(
                                entry -> server.getMemberById(entry.getKey()).orElseThrow(AssertionError::new),
                                Map.Entry::getValue))
        );
    }

    @Override
    public Permissions getEffectiveOverwrittenPermissions(User user) {
        PermissionsBuilder builder = new PermissionsBuilder(PermissionsImpl.EMPTY_PERMISSIONS);
        Server server = getServer();
        Role everyoneRole = server.getEveryoneRole();
        Permissions everyoneRolePermissionOverwrites = getOverwrittenPermissions(everyoneRole);
        for (PermissionType type : PermissionType.values()) {
            if (everyoneRolePermissionOverwrites.getState(type) == PermissionState.DENIED) {
                builder.setState(type, PermissionState.DENIED);
            }
            if (everyoneRolePermissionOverwrites.getState(type) == PermissionState.ALLOWED) {
                builder.setState(type, PermissionState.ALLOWED);
            }
        }
        List<Role> rolesOfUser = new ArrayList<>(server.getRolesOf(user));
        rolesOfUser.remove(everyoneRole);
        List<Permissions> permissionOverwrites = rolesOfUser.stream()
                .map(this::getOverwrittenPermissions)
                .collect(Collectors.toList());
        for (Permissions permissions : permissionOverwrites) {
            for (PermissionType type : PermissionType.values()) {
                if (permissions.getState(type) == PermissionState.DENIED) {
                    builder.setState(type, PermissionState.DENIED);
                }
            }
        }
        for (Permissions permissions : permissionOverwrites) {
            for (PermissionType type : PermissionType.values()) {
                if (permissions.getState(type) == PermissionState.ALLOWED) {
                    builder.setState(type, PermissionState.ALLOWED);
                }
            }
        }
        for (PermissionType type : PermissionType.values()) {
            Permissions permissions = getOverwrittenPermissions(user);
            if (permissions.getState(type) == PermissionState.DENIED) {
                builder.setState(type, PermissionState.DENIED);
            }
            if (permissions.getState(type) == PermissionState.ALLOWED) {
                builder.setState(type, PermissionState.ALLOWED);
            }
        }
        return builder.build();
    }

    @Override
    public CompletableFuture<Void> delete(String reason) {
        return new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.CHANNEL)
                .setUrlParameters(getIdAsString())
                .setAuditLogReason(reason)
                .execute(result -> null);
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
        return String.format("ServerChannel (id: %s, name: %s)", getIdAsString(), getName());
    }

}

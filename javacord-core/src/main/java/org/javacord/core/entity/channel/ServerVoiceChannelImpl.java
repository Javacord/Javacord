package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.listener.ChannelAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.VoiceChannelAttachableListener;
import org.javacord.api.listener.channel.server.ServerChannelAttachableListener;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelAttachableListener;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelChangeBitrateListener;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelChangeUserLimitListener;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelMemberJoinListener;
import org.javacord.api.listener.channel.server.voice.ServerVoiceChannelMemberLeaveListener;
import org.javacord.api.util.event.ListenerManager;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.permission.PermissionsImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.util.ClassHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The implementation of {@link ServerVoiceChannel}.
 */
public class ServerVoiceChannelImpl
        implements ServerVoiceChannel, InternalChannel, InternalServerChannel, InternalVoiceChannel {

    /**
     * The discord api instance.
     */
    private final DiscordApiImpl api;

    /**
     * The id of the channel.
     */
    private final long id;

    /**
     * The bitrate of the channel.
     */
    private volatile int bitrate;

    /**
     * The userLimit of the channel.
     */
    private volatile int userLimit;

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
     * The parent id of the channel.
     */
    private volatile long parentId;

    /**
     * A map with all overwritten user permissions.
     */
    private final ConcurrentHashMap<Long, Permissions> overwrittenUserPermissions = new ConcurrentHashMap<>();

    /**
     * A map with all overwritten role permissions.
     */
    private final ConcurrentHashMap<Long, Permissions> overwrittenRolePermissions = new ConcurrentHashMap<>();

    /**
     * The ids of the connected users of this server voice channel.
     */
    private final Collection<Long> connectedUsers = new ArrayList<>();

    /**
     * Creates a new server voice channel object.
     *
     * @param api The discord api instance.
     * @param server The server of the channel.
     * @param data The json data of the channel.
     */
    public ServerVoiceChannelImpl(DiscordApiImpl api, ServerImpl server, JsonNode data) {
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
                Permissions permissions = new PermissionsImpl(allow, deny);
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

    /**
     * Adds the user with the given id to the list of connected users.
     *
     * @param userId The id of the user to add.
     */
    public void addConnectedUser(long userId) {
        connectedUsers.add(userId);
    }

    /**
     * Removes the user with the given id from the list of connected users.
     *
     * @param userId The id of the user to remove.
     */
    public void removeConnectedUser(long userId) {
        connectedUsers.remove(userId);
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
        return overwrittenUserPermissions.getOrDefault(user.getId(), PermissionsImpl.EMPTY_PERMISSIONS);
    }

    @Override
    public Permissions getOverwrittenPermissions(Role role) {
        return overwrittenRolePermissions.getOrDefault(role.getId(), PermissionsImpl.EMPTY_PERMISSIONS);
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
    public Collection<Long> getConnectedUserIds() {
        return Collections.unmodifiableCollection(connectedUsers);
    }

    @Override
    public boolean isConnected(long userId) {
        return connectedUsers.contains(userId);
    }

    @Override
    public ListenerManager<ServerVoiceChannelMemberJoinListener> addServerVoiceChannelMemberJoinListener(
            ServerVoiceChannelMemberJoinListener listener) {
        return ((DiscordApiImpl) getApi()).addObjectListener(
                ServerVoiceChannel.class, getId(), ServerVoiceChannelMemberJoinListener.class, listener);
    }

    @Override
    public List<ServerVoiceChannelMemberJoinListener> getServerVoiceChannelMemberJoinListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(
                ServerVoiceChannel.class, getId(), ServerVoiceChannelMemberJoinListener.class);
    }

    @Override
    public ListenerManager<ServerVoiceChannelMemberLeaveListener> addServerVoiceChannelMemberLeaveListener(
            ServerVoiceChannelMemberLeaveListener listener) {
        return ((DiscordApiImpl) getApi()).addObjectListener(
                ServerVoiceChannel.class, getId(), ServerVoiceChannelMemberLeaveListener.class, listener);
    }

    @Override
    public List<ServerVoiceChannelMemberLeaveListener> getServerVoiceChannelMemberLeaveListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(
                ServerVoiceChannel.class, getId(), ServerVoiceChannelMemberLeaveListener.class);
    }

    @Override
    public ListenerManager<ServerVoiceChannelChangeBitrateListener> addServerVoiceChannelChangeBitrateListener(
            ServerVoiceChannelChangeBitrateListener listener) {
        return ((DiscordApiImpl) getApi()).addObjectListener(
                ServerVoiceChannel.class, getId(), ServerVoiceChannelChangeBitrateListener.class, listener);
    }

    @Override
    public List<ServerVoiceChannelChangeBitrateListener> getServerVoiceChannelChangeBitrateListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(
                ServerVoiceChannel.class, getId(), ServerVoiceChannelChangeBitrateListener.class);
    }

    @Override
    public ListenerManager<ServerVoiceChannelChangeUserLimitListener> addServerVoiceChannelChangeUserLimitListener(
            ServerVoiceChannelChangeUserLimitListener listener) {
        return ((DiscordApiImpl) getApi()).addObjectListener(
                ServerVoiceChannel.class, getId(), ServerVoiceChannelChangeUserLimitListener.class, listener);
    }

    @Override
    public List<ServerVoiceChannelChangeUserLimitListener> getServerVoiceChannelChangeUserLimitListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(
                ServerVoiceChannel.class, getId(), ServerVoiceChannelChangeUserLimitListener.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ServerVoiceChannelAttachableListener & ObjectAttachableListener>
    Collection<ListenerManager<? extends ServerVoiceChannelAttachableListener>> addServerVoiceChannelAttachableListener(
            T listener) {
        return ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(ServerVoiceChannelAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .flatMap(listenerClass -> {
                    if (ChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        return addChannelAttachableListener(
                                (ChannelAttachableListener & ObjectAttachableListener) listener).stream();
                    } else if (ServerChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        return addServerChannelAttachableListener(
                                (ServerChannelAttachableListener & ObjectAttachableListener) listener).stream();
                    } else if (VoiceChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        return addVoiceChannelAttachableListener(
                                (VoiceChannelAttachableListener & ObjectAttachableListener) listener).stream();
                    } else {
                        return Stream.of(((DiscordApiImpl) getApi()).addObjectListener(
                                ServerVoiceChannel.class, getId(), listenerClass, listener));
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ServerVoiceChannelAttachableListener & ObjectAttachableListener> void
    removeServerVoiceChannelAttachableListener(T listener) {
        ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(ServerVoiceChannelAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .forEach(listenerClass -> {
                    if (ChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        removeChannelAttachableListener(
                                (ChannelAttachableListener & ObjectAttachableListener) listener);
                    } else if (ServerChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        removeServerChannelAttachableListener(
                                (ServerChannelAttachableListener & ObjectAttachableListener) listener);
                    } else if (VoiceChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        removeVoiceChannelAttachableListener(
                                (VoiceChannelAttachableListener & ObjectAttachableListener) listener);
                    } else {
                        ((DiscordApiImpl) getApi()).removeObjectListener(ServerVoiceChannel.class, getId(),
                                                                         listenerClass, listener);
                    }
                });
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ServerVoiceChannelAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
    getServerVoiceChannelAttachableListeners() {
        Map<T, List<Class<T>>> serverVoiceChannelListeners =
                ((DiscordApiImpl) getApi()).getObjectListeners(ServerVoiceChannel.class, getId());
        getVoiceChannelAttachableListeners().forEach((listener, listenerClasses) -> serverVoiceChannelListeners
                .merge((T) listener,
                       (List<Class<T>>) (Object) listenerClasses,
                       (listenerClasses1, listenerClasses2) -> {
                           listenerClasses1.addAll(listenerClasses2);
                           return listenerClasses1;
                       }));
        getServerChannelAttachableListeners().forEach((listener, listenerClasses) -> serverVoiceChannelListeners
                .merge((T) listener,
                       (List<Class<T>>) (Object) listenerClasses,
                       (listenerClasses1, listenerClasses2) -> {
                           listenerClasses1.addAll(listenerClasses2);
                           return listenerClasses1;
                       }));
        getChannelAttachableListeners().forEach((listener, listenerClasses) -> serverVoiceChannelListeners
                .merge((T) listener,
                       (List<Class<T>>) (Object) listenerClasses,
                       (listenerClasses1, listenerClasses2) -> {
                           listenerClasses1.addAll(listenerClasses2);
                           return listenerClasses1;
                       }));
        return serverVoiceChannelListeners;
    }

    @Override
    public <T extends ServerVoiceChannelAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener) {
        ((DiscordApiImpl) getApi()).removeObjectListener(ServerVoiceChannel.class, getId(), listenerClass, listener);
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
        return String.format("ServerVoiceChannel (id: %s, name: %s)", getIdAsString(), getName());
    }

}

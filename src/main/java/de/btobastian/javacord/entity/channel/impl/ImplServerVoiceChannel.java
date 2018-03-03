package de.btobastian.javacord.entity.channel.impl;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entity.DiscordEntity;
import de.btobastian.javacord.entity.channel.ChannelCategory;
import de.btobastian.javacord.entity.channel.InternalChannel;
import de.btobastian.javacord.entity.channel.InternalServerChannel;
import de.btobastian.javacord.entity.channel.InternalVoiceChannel;
import de.btobastian.javacord.entity.channel.ServerVoiceChannel;
import de.btobastian.javacord.entity.channel.ServerVoiceChannelUpdater;
import de.btobastian.javacord.entity.permission.Permissions;
import de.btobastian.javacord.entity.permission.Role;
import de.btobastian.javacord.entity.permission.impl.ImplPermissions;
import de.btobastian.javacord.entity.server.Server;
import de.btobastian.javacord.entity.server.impl.ImplServer;
import de.btobastian.javacord.entity.user.User;
import de.btobastian.javacord.listener.ChannelAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.VoiceChannelAttachableListener;
import de.btobastian.javacord.listener.channel.server.ServerChannelAttachableListener;
import de.btobastian.javacord.listener.channel.server.voice.ServerVoiceChannelAttachableListener;
import de.btobastian.javacord.listener.channel.server.voice.ServerVoiceChannelChangeBitrateListener;
import de.btobastian.javacord.listener.channel.server.voice.ServerVoiceChannelChangeUserLimitListener;
import de.btobastian.javacord.listener.channel.server.voice.ServerVoiceChannelMemberJoinListener;
import de.btobastian.javacord.listener.channel.server.voice.ServerVoiceChannelMemberLeaveListener;
import de.btobastian.javacord.util.ClassHelper;
import de.btobastian.javacord.util.event.ListenerManager;

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
public class ImplServerVoiceChannel
        implements ServerVoiceChannel, InternalChannel, InternalServerChannel, InternalVoiceChannel {

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
    public ServerVoiceChannelUpdater createUpdater() {
        return new ImplServerVoiceChannelUpdater(this);
    }

    @Override
    public ListenerManager<ServerVoiceChannelMemberJoinListener> addServerVoiceChannelMemberJoinListener(
            ServerVoiceChannelMemberJoinListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                ServerVoiceChannel.class, getId(), ServerVoiceChannelMemberJoinListener.class, listener);
    }

    @Override
    public List<ServerVoiceChannelMemberJoinListener> getServerVoiceChannelMemberJoinListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                ServerVoiceChannel.class, getId(), ServerVoiceChannelMemberJoinListener.class);
    }

    @Override
    public ListenerManager<ServerVoiceChannelMemberLeaveListener> addServerVoiceChannelMemberLeaveListener(
            ServerVoiceChannelMemberLeaveListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                ServerVoiceChannel.class, getId(), ServerVoiceChannelMemberLeaveListener.class, listener);
    }

    @Override
    public List<ServerVoiceChannelMemberLeaveListener> getServerVoiceChannelMemberLeaveListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                ServerVoiceChannel.class, getId(), ServerVoiceChannelMemberLeaveListener.class);
    }

    @Override
    public ListenerManager<ServerVoiceChannelChangeBitrateListener> addServerVoiceChannelChangeBitrateListener(
            ServerVoiceChannelChangeBitrateListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                ServerVoiceChannel.class, getId(), ServerVoiceChannelChangeBitrateListener.class, listener);
    }

    @Override
    public List<ServerVoiceChannelChangeBitrateListener> getServerVoiceChannelChangeBitrateListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                ServerVoiceChannel.class, getId(), ServerVoiceChannelChangeBitrateListener.class);
    }

    @Override
    public ListenerManager<ServerVoiceChannelChangeUserLimitListener> addServerVoiceChannelChangeUserLimitListener(
            ServerVoiceChannelChangeUserLimitListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                ServerVoiceChannel.class, getId(), ServerVoiceChannelChangeUserLimitListener.class, listener);
    }

    @Override
    public List<ServerVoiceChannelChangeUserLimitListener> getServerVoiceChannelChangeUserLimitListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
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
                        return Stream.of(((ImplDiscordApi) getApi()).addObjectListener(
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
                        ((ImplDiscordApi) getApi()).removeObjectListener(ServerVoiceChannel.class, getId(),
                                                                         listenerClass, listener);
                    }
                });
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ServerVoiceChannelAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
    getServerVoiceChannelAttachableListeners() {
        Map<T, List<Class<T>>> serverVoiceChannelListeners =
                ((ImplDiscordApi) getApi()).getObjectListeners(ServerVoiceChannel.class, getId());
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
        ((ImplDiscordApi) getApi()).removeObjectListener(ServerVoiceChannel.class, getId(), listenerClass, listener);
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

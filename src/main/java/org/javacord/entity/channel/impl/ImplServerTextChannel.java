package org.javacord.entity.channel.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.DiscordApi;
import org.javacord.ImplDiscordApi;
import org.javacord.entity.DiscordEntity;
import org.javacord.entity.channel.ChannelCategory;
import org.javacord.entity.channel.InternalChannel;
import org.javacord.entity.channel.InternalServerChannel;
import org.javacord.entity.channel.InternalTextChannel;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.channel.ServerTextChannelUpdater;
import org.javacord.entity.permission.Permissions;
import org.javacord.entity.permission.Role;
import org.javacord.entity.permission.impl.ImplPermissions;
import org.javacord.entity.server.Server;
import org.javacord.entity.server.impl.ImplServer;
import org.javacord.entity.user.User;
import org.javacord.entity.webhook.WebhookBuilder;
import org.javacord.entity.webhook.impl.ImplWebhookBuilder;
import org.javacord.listener.ChannelAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.TextChannelAttachableListener;
import org.javacord.listener.channel.server.ServerChannelAttachableListener;
import org.javacord.listener.channel.server.ServerChannelChangeNsfwFlagListener;
import org.javacord.listener.channel.server.text.ServerTextChannelAttachableListener;
import org.javacord.listener.channel.server.text.ServerTextChannelChangeTopicListener;
import org.javacord.listener.channel.text.WebhooksUpdateListener;
import org.javacord.util.ClassHelper;
import org.javacord.util.Cleanupable;
import org.javacord.util.cache.ImplMessageCache;
import org.javacord.util.cache.MessageCache;
import org.javacord.util.event.ListenerManager;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The implementation of {@link ServerTextChannel}.
 */
public class ImplServerTextChannel
        implements ServerTextChannel, Cleanupable, InternalChannel, InternalServerChannel, InternalTextChannel {

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
    public WebhookBuilder createWebhookBuilder() {
        return new ImplWebhookBuilder(this);
    }

    @Override
    public ServerTextChannelUpdater createUpdater() {
        return new ImplServerTextChannelUpdater(this);
    }

    @Override
    public ListenerManager<ServerTextChannelChangeTopicListener> addServerTextChannelChangeTopicListener(
            ServerTextChannelChangeTopicListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                ServerTextChannel.class, getId(), ServerTextChannelChangeTopicListener.class, listener);
    }

    @Override
    public List<ServerTextChannelChangeTopicListener> getServerTextChannelChangeTopicListeners() {
        return ((ImplDiscordApi) getApi())
                .getObjectListeners(ServerTextChannel.class, getId(), ServerTextChannelChangeTopicListener.class);
    }

    @Override
    public ListenerManager<ServerChannelChangeNsfwFlagListener> addServerChannelChangeNsfwFlagListener(
            ServerChannelChangeNsfwFlagListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                ServerTextChannel.class, getId(), ServerChannelChangeNsfwFlagListener.class, listener);
    }

    @Override
    public List<ServerChannelChangeNsfwFlagListener> getServerChannelChangeNsfwFlagListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                ServerTextChannel.class, getId(), ServerChannelChangeNsfwFlagListener.class);
    }

    @Override
    public ListenerManager<WebhooksUpdateListener> addWebhooksUpdateListener(WebhooksUpdateListener listener) {
        return ((ImplDiscordApi) getApi())
                .addObjectListener(ServerTextChannel.class, getId(), WebhooksUpdateListener.class, listener);
    }

    @Override
    public List<WebhooksUpdateListener> getWebhooksUpdateListeners() {
        return ((ImplDiscordApi) getApi())
                .getObjectListeners(ServerTextChannel.class, getId(), WebhooksUpdateListener.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ServerTextChannelAttachableListener & ObjectAttachableListener>
    Collection<ListenerManager<? extends ServerTextChannelAttachableListener>> addServerTextChannelAttachableListener(
            T listener) {
        return ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(ServerTextChannelAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .flatMap(listenerClass -> {
                    if (ChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        return addChannelAttachableListener(
                                (ChannelAttachableListener & ObjectAttachableListener) listener).stream();
                    } else if (ServerChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        return addServerChannelAttachableListener(
                                (ServerChannelAttachableListener & ObjectAttachableListener) listener).stream();
                    } else if (TextChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        return addTextChannelAttachableListener(
                                (TextChannelAttachableListener & ObjectAttachableListener) listener).stream();
                    } else {
                        return Stream.of(((ImplDiscordApi) getApi()).addObjectListener(ServerTextChannel.class, getId(),
                                                                                       listenerClass, listener));
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ServerTextChannelAttachableListener & ObjectAttachableListener> void
    removeServerTextChannelAttachableListener(T listener) {
        ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(ServerTextChannelAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .forEach(listenerClass -> {
                    if (ChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        removeChannelAttachableListener(
                                (ChannelAttachableListener & ObjectAttachableListener) listener);
                    } else if (ServerChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        removeServerChannelAttachableListener(
                                (ServerChannelAttachableListener & ObjectAttachableListener) listener);
                    } else if (TextChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        removeTextChannelAttachableListener(
                                (TextChannelAttachableListener & ObjectAttachableListener) listener);
                    } else {
                        ((ImplDiscordApi) getApi()).removeObjectListener(ServerTextChannel.class, getId(),
                                                                         listenerClass, listener);
                    }
                });
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ServerTextChannelAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
    getServerTextChannelAttachableListeners() {
        Map<T, List<Class<T>>> serverTextChannelListeners =
                ((ImplDiscordApi) getApi()).getObjectListeners(ServerTextChannel.class, getId());
        getTextChannelAttachableListeners().forEach((listener, listenerClasses) -> serverTextChannelListeners
                .merge((T) listener,
                       (List<Class<T>>) (Object) listenerClasses,
                       (listenerClasses1, listenerClasses2) -> {
                           listenerClasses1.addAll(listenerClasses2);
                           return listenerClasses1;
                       }));
        getServerChannelAttachableListeners().forEach((listener, listenerClasses) -> serverTextChannelListeners
                .merge((T) listener,
                       (List<Class<T>>) (Object) listenerClasses,
                       (listenerClasses1, listenerClasses2) -> {
                           listenerClasses1.addAll(listenerClasses2);
                           return listenerClasses1;
                       }));
        getChannelAttachableListeners().forEach((listener, listenerClasses) -> serverTextChannelListeners
                .merge((T) listener,
                       (List<Class<T>>) (Object) listenerClasses,
                       (listenerClasses1, listenerClasses2) -> {
                           listenerClasses1.addAll(listenerClasses2);
                           return listenerClasses1;
                       }));
        return serverTextChannelListeners;
    }

    @Override
    public <T extends ServerTextChannelAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener) {
        ((ImplDiscordApi) getApi()).removeObjectListener(ServerTextChannel.class, getId(), listenerClass, listener);
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
        return "<#" + getIdAsString() + ">";
    }

    @Override
    public void cleanup() {
        messageCache.cleanup();
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
        return String.format("ServerTextChannel (id: %s, name: %s)", getIdAsString(), getName());
    }

}

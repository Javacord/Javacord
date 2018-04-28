package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.listener.ChannelAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.TextChannelAttachableListener;
import org.javacord.api.listener.channel.server.ServerChannelAttachableListener;
import org.javacord.api.listener.channel.server.ServerChannelChangeNsfwFlagListener;
import org.javacord.api.listener.channel.server.text.ServerTextChannelAttachableListener;
import org.javacord.api.listener.channel.server.text.ServerTextChannelChangeTopicListener;
import org.javacord.api.listener.channel.server.text.WebhooksUpdateListener;
import org.javacord.api.util.cache.MessageCache;
import org.javacord.api.util.event.ListenerManager;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.util.ClassHelper;
import org.javacord.core.util.Cleanupable;
import org.javacord.core.util.cache.MessageCacheImpl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The implementation of {@link ServerTextChannel}.
 */
public class ServerTextChannelImpl extends ServerChannelImpl
        implements ServerTextChannel, Cleanupable, InternalTextChannel {

    /**
     * The message cache of the server text channel.
     */
    private final MessageCacheImpl messageCache;

    /**
     * Whether the channel is "not safe for work" or not.
     */
    private volatile boolean nsfw;

    /**
     * The parent id of the channel.
     */
    private volatile long parentId;

    /**
     * The topic of the channel.
     */
    private volatile String topic;

    /**
     * Creates a new server text channel object.
     *
     * @param api The discord api instance.
     * @param server The server of the channel.
     * @param data The json data of the channel.
     */
    public ServerTextChannelImpl(DiscordApiImpl api, ServerImpl server, JsonNode data) {
        super(api, server, data);
        nsfw = data.has("nsfw") && data.get("nsfw").asBoolean();
        parentId = Long.valueOf(data.has("parent_id") ? data.get("parent_id").asText("-1") : "-1");
        topic = data.has("topic") && !data.get("topic").isNull() ? data.get("topic").asText() : "";
        messageCache = new MessageCacheImpl(
                api, api.getDefaultMessageCacheCapacity(), api.getDefaultMessageCacheStorageTimeInSeconds());
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
    public ListenerManager<ServerTextChannelChangeTopicListener> addServerTextChannelChangeTopicListener(
            ServerTextChannelChangeTopicListener listener) {
        return ((DiscordApiImpl) getApi()).addObjectListener(
                ServerTextChannel.class, getId(), ServerTextChannelChangeTopicListener.class, listener);
    }

    @Override
    public List<ServerTextChannelChangeTopicListener> getServerTextChannelChangeTopicListeners() {
        return ((DiscordApiImpl) getApi())
                .getObjectListeners(ServerTextChannel.class, getId(), ServerTextChannelChangeTopicListener.class);
    }

    @Override
    public ListenerManager<ServerChannelChangeNsfwFlagListener> addServerChannelChangeNsfwFlagListener(
            ServerChannelChangeNsfwFlagListener listener) {
        return ((DiscordApiImpl) getApi()).addObjectListener(
                ServerTextChannel.class, getId(), ServerChannelChangeNsfwFlagListener.class, listener);
    }

    @Override
    public List<ServerChannelChangeNsfwFlagListener> getServerChannelChangeNsfwFlagListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(
                ServerTextChannel.class, getId(), ServerChannelChangeNsfwFlagListener.class);
    }

    @Override
    public ListenerManager<WebhooksUpdateListener> addWebhooksUpdateListener(WebhooksUpdateListener listener) {
        return ((DiscordApiImpl) getApi())
                .addObjectListener(ServerTextChannel.class, getId(), WebhooksUpdateListener.class, listener);
    }

    @Override
    public List<WebhooksUpdateListener> getWebhooksUpdateListeners() {
        return ((DiscordApiImpl) getApi())
                .getObjectListeners(ServerTextChannel.class, getId(), WebhooksUpdateListener.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ServerTextChannelAttachableListener & ObjectAttachableListener>
            Collection<ListenerManager<? extends ServerTextChannelAttachableListener>>
                    addServerTextChannelAttachableListener(T listener) {
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
                        return Stream.of(((DiscordApiImpl) getApi()).addObjectListener(ServerTextChannel.class, getId(),
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
                        ((DiscordApiImpl) getApi()).removeObjectListener(ServerTextChannel.class, getId(),
                                                                         listenerClass, listener);
                    }
                });
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ServerTextChannelAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
            getServerTextChannelAttachableListeners() {
        Map<T, List<Class<T>>> serverTextChannelListeners =
                ((DiscordApiImpl) getApi()).getObjectListeners(ServerTextChannel.class, getId());
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
        ((DiscordApiImpl) getApi()).removeObjectListener(ServerTextChannel.class, getId(), listenerClass, listener);
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

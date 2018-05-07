package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.channel.Categorizable;
import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.listener.ChannelAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.server.ChannelCategoryAttachableListener;
import org.javacord.api.listener.channel.server.ServerChannelAttachableListener;
import org.javacord.api.listener.channel.server.ServerChannelChangeNsfwFlagListener;
import org.javacord.api.util.event.ListenerManager;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.util.ClassHelper;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The implementation of {@link ChannelCategory}.
 */
public class ChannelCategoryImpl extends ServerChannelImpl implements ChannelCategory {

    /**
     * Whether the category is "not safe for work" or not.
     */
    private volatile boolean nsfw;

    /**
     * Creates a new server channel category object.
     *
     * @param api The discord api instance.
     * @param server The server of the channel.
     * @param data The json data of the channel.
     */
    public ChannelCategoryImpl(DiscordApiImpl api, ServerImpl server, JsonNode data) {
        super(api, server, data);
        nsfw = data.has("nsfw") && data.get("nsfw").asBoolean();
    }

    /**
     * Sets the nsfw flag.
     *
     * @param nsfw The nsfw flag.
     */
    public void setNsfwFlag(boolean nsfw) {
        this.nsfw = nsfw;
    }

    @Override
    public List<ServerChannel> getChannels() {
        return Collections.unmodifiableList(
                ((ServerImpl) getServer()).getUnorderedChannels().stream()
                        .filter(channel -> channel.asCategorizable()
                                .flatMap(Categorizable::getCategory)
                                .map(this::equals)
                                .orElse(false))
                        .sorted(Comparator
                                        .<ServerChannel>comparingInt(channel -> channel.getType().getId())
                                        .thenComparingInt(ServerChannel::getRawPosition)
                                        .thenComparingLong(ServerChannel::getId))
                        .collect(Collectors.toList()));
    }

    @Override
    public boolean isNsfw() {
        return nsfw;
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
    @SuppressWarnings("unchecked")
    public <T extends ChannelCategoryAttachableListener & ObjectAttachableListener>
            Collection<ListenerManager<? extends ChannelCategoryAttachableListener>>
            addChannelCategoryAttachableListener(T listener) {
        return ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(ChannelCategoryAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .flatMap(listenerClass -> {
                    if (ChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        return addChannelAttachableListener(
                                (ChannelAttachableListener & ObjectAttachableListener) listener).stream();
                    } else if (ServerChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        return addServerChannelAttachableListener(
                                (ServerChannelAttachableListener & ObjectAttachableListener) listener).stream();
                    } else {
                        return Stream.of(((DiscordApiImpl) getApi()).addObjectListener(ChannelCategory.class, getId(),
                                                                                       listenerClass, listener));
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ChannelCategoryAttachableListener & ObjectAttachableListener>
            void removeChannelCategoryAttachableListener(T listener) {
        ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(ChannelCategoryAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .forEach(listenerClass -> {
                    if (ChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        removeChannelAttachableListener(
                                (ChannelAttachableListener & ObjectAttachableListener) listener);
                    } else if (ServerChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        removeServerChannelAttachableListener(
                                (ServerChannelAttachableListener & ObjectAttachableListener) listener);
                    } else {
                        ((DiscordApiImpl) getApi()).removeObjectListener(ChannelCategory.class, getId(),
                                listenerClass, listener);
                    }
                });
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ChannelCategoryAttachableListener & ObjectAttachableListener>
            Map<T, List<Class<T>>> getChannelCategoryAttachableListeners() {
        Map<T, List<Class<T>>> channelCategoryListeners =
                ((DiscordApiImpl) getApi()).getObjectListeners(ChannelCategory.class, getId());
        getServerChannelAttachableListeners().forEach((listener, listenerClasses) -> channelCategoryListeners
                .merge((T) listener,
                        (List<Class<T>>) (Object) listenerClasses,
                        (listenerClasses1, listenerClasses2) -> {
                            listenerClasses1.addAll(listenerClasses2);
                            return listenerClasses1;
                        }));
        getChannelAttachableListeners().forEach((listener, listenerClasses) -> channelCategoryListeners
                .merge((T) listener,
                        (List<Class<T>>) (Object) listenerClasses,
                        (listenerClasses1, listenerClasses2) -> {
                            listenerClasses1.addAll(listenerClasses2);
                            return listenerClasses1;
                        }));
        return channelCategoryListeners;
    }

    @Override
    public <T extends ChannelCategoryAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener) {
        ((DiscordApiImpl) getApi()).removeObjectListener(ChannelCategory.class, getId(), listenerClass, listener);
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
        return String.format("ChannelCategory (id: %s, name: %s)", getIdAsString(), getName());
    }

}

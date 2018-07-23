package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.Icon;
import org.javacord.api.entity.channel.GroupChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.listener.ChannelAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.TextChannelAttachableListener;
import org.javacord.api.listener.VoiceChannelAttachableListener;
import org.javacord.api.listener.channel.group.GroupChannelAttachableListener;
import org.javacord.api.listener.channel.group.GroupChannelChangeNameListener;
import org.javacord.api.listener.channel.group.GroupChannelDeleteListener;
import org.javacord.api.util.cache.MessageCache;
import org.javacord.api.util.event.ListenerManager;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.IconImpl;
import org.javacord.core.util.ClassHelper;
import org.javacord.core.util.Cleanupable;
import org.javacord.core.util.cache.MessageCacheImpl;
import org.javacord.core.util.logging.LoggerUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The implementation of {@link GroupChannel}.
 */
public class GroupChannelImpl
        implements GroupChannel, Cleanupable, InternalChannel, InternalTextChannel, InternalVoiceChannel {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(GroupChannelImpl.class);

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
     * The icon id of the channel.
     */
    private volatile String iconId;

    /**
     * The recipients of the group channel.
     */
    private final List<User> recipients = new ArrayList<>();

    /**
     * The message cache of the private channel.
     */
    private final MessageCacheImpl messageCache;

    /**
     * Creates a new private channel.
     *
     * @param api The discord api instance.
     * @param data The json data of the channel.
     */
    public GroupChannelImpl(DiscordApiImpl api, JsonNode data) {
        this.api = api;

        for (JsonNode recipientJson : data.get("recipients")) {
            recipients.add(api.getOrCreateUser(recipientJson));
        }

        this.messageCache = new MessageCacheImpl(
                api, api.getDefaultMessageCacheCapacity(), api.getDefaultMessageCacheStorageTimeInSeconds());

        id = Long.parseLong(data.get("id").asText());
        name = data.has("name") && !data.get("name").isNull() ? data.get("name").asText() : null;
        iconId = data.has("icon") && !data.get("icon").isNull() ? data.get("icon").asText() : null;

        api.addGroupChannelToCache(this);
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
    public MessageCache getMessageCache() {
        return messageCache;
    }

    @Override
    public Collection<User> getMembers() {
        return Collections.unmodifiableCollection(recipients);
    }

    @Override
    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    /**
     * Sets the name of the channel.
     *
     * @param name The new name of the channel.
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Optional<Icon> getIcon() {
        if (iconId == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(new IconImpl(
                    getApi(),
                    new URL("https://cdn.discordapp.com/channel-icons/" + getIdAsString() + "/" + iconId + ".png")));
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the icon is malformed! Please contact the developer!", e);
            return Optional.empty();
        }
    }

    @Override
    public ListenerManager<GroupChannelChangeNameListener> addGroupChannelChangeNameListener(
            GroupChannelChangeNameListener listener) {
        return ((DiscordApiImpl) getApi()).addObjectListener(
                GroupChannel.class, getId(), GroupChannelChangeNameListener.class, listener);
    }

    @Override
    public List<GroupChannelChangeNameListener> getGroupChannelChangeNameListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(
                GroupChannel.class, getId(), GroupChannelChangeNameListener.class);
    }

    @Override
    public ListenerManager<GroupChannelDeleteListener> addGroupChannelDeleteListener(
            GroupChannelDeleteListener listener) {
        return ((DiscordApiImpl) getApi()).addObjectListener(
                GroupChannel.class, getId(), GroupChannelDeleteListener.class, listener);
    }

    @Override
    public List<GroupChannelDeleteListener> getGroupChannelDeleteListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(
                GroupChannel.class, getId(), GroupChannelDeleteListener.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends GroupChannelAttachableListener & ObjectAttachableListener>
            Collection<ListenerManager<? extends GroupChannelAttachableListener>> addGroupChannelAttachableListener(
            T listener) {
        return ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(GroupChannelAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .flatMap(listenerClass -> {
                    if (ChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        return addChannelAttachableListener(
                                (ChannelAttachableListener & ObjectAttachableListener) listener).stream();
                    } else if (TextChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        return addTextChannelAttachableListener(
                                (TextChannelAttachableListener & ObjectAttachableListener) listener).stream();
                    } else if (VoiceChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        return addVoiceChannelAttachableListener(
                                (VoiceChannelAttachableListener & ObjectAttachableListener) listener).stream();
                    } else {
                        return Stream.of(((DiscordApiImpl) getApi()).addObjectListener(GroupChannel.class, getId(),
                                                                                       listenerClass, listener));
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends GroupChannelAttachableListener & ObjectAttachableListener> void
            removeGroupChannelAttachableListener(T listener) {
        ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(GroupChannelAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .forEach(listenerClass -> {
                    if (ChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        removeChannelAttachableListener(
                                (ChannelAttachableListener & ObjectAttachableListener) listener);
                    } else if (TextChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        removeTextChannelAttachableListener(
                                (TextChannelAttachableListener & ObjectAttachableListener) listener);
                    } else if (VoiceChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        removeVoiceChannelAttachableListener(
                                (VoiceChannelAttachableListener & ObjectAttachableListener) listener);
                    } else {
                        ((DiscordApiImpl) getApi()).removeObjectListener(GroupChannel.class, getId(),
                                                                         listenerClass, listener);
                    }
                });
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends GroupChannelAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
            getGroupChannelAttachableListeners() {
        Map<T, List<Class<T>>> groupChannelListeners =
                ((DiscordApiImpl) getApi()).getObjectListeners(GroupChannel.class, getId());
        getTextChannelAttachableListeners().forEach((listener, listenerClasses) -> groupChannelListeners
                .merge((T) listener,
                        (List<Class<T>>) (Object) listenerClasses,
                        (listenerClasses1, listenerClasses2) -> {
                            listenerClasses1.addAll(listenerClasses2);
                            return listenerClasses1;
                        }));
        getVoiceChannelAttachableListeners().forEach((listener, listenerClasses) -> groupChannelListeners
                .merge((T) listener,
                        (List<Class<T>>) (Object) listenerClasses,
                        (listenerClasses1, listenerClasses2) -> {
                            listenerClasses1.addAll(listenerClasses2);
                            return listenerClasses1;
                        }));
        getChannelAttachableListeners().forEach((listener, listenerClasses) -> groupChannelListeners
                .merge((T) listener,
                        (List<Class<T>>) (Object) listenerClasses,
                        (listenerClasses1, listenerClasses2) -> {
                            listenerClasses1.addAll(listenerClasses2);
                            return listenerClasses1;
                        }));
        return groupChannelListeners;
    }

    @Override
    public <T extends GroupChannelAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener) {
        ((DiscordApiImpl) getApi()).removeObjectListener(GroupChannel.class, getId(), listenerClass, listener);
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
        return String.format("GroupChannel (id: %s, name: %s)", getIdAsString(), getName());
    }

}

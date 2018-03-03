package de.btobastian.javacord.entity.channel.impl;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entity.DiscordEntity;
import de.btobastian.javacord.entity.Icon;
import de.btobastian.javacord.entity.channel.GroupChannel;
import de.btobastian.javacord.entity.channel.GroupChannelUpdater;
import de.btobastian.javacord.entity.channel.InternalChannel;
import de.btobastian.javacord.entity.channel.InternalTextChannel;
import de.btobastian.javacord.entity.channel.InternalVoiceChannel;
import de.btobastian.javacord.entity.impl.ImplIcon;
import de.btobastian.javacord.entity.user.User;
import de.btobastian.javacord.listener.ChannelAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.TextChannelAttachableListener;
import de.btobastian.javacord.listener.VoiceChannelAttachableListener;
import de.btobastian.javacord.listener.channel.group.GroupChannelAttachableListener;
import de.btobastian.javacord.listener.channel.group.GroupChannelChangeNameListener;
import de.btobastian.javacord.listener.channel.group.GroupChannelDeleteListener;
import de.btobastian.javacord.util.ClassHelper;
import de.btobastian.javacord.util.Cleanupable;
import de.btobastian.javacord.util.cache.ImplMessageCache;
import de.btobastian.javacord.util.cache.MessageCache;
import de.btobastian.javacord.util.event.ListenerManager;
import de.btobastian.javacord.util.logging.LoggerUtil;
import org.slf4j.Logger;

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
public class ImplGroupChannel
        implements GroupChannel, Cleanupable, InternalChannel, InternalTextChannel, InternalVoiceChannel {

    /**
     * The logger of this class.
     */
    Logger logger = LoggerUtil.getLogger(ImplGroupChannel.class);

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
     * The icon id of the channel.
     */
    private String iconId;

    /**
     * The recipients of the group channel.
     */
    private final List<User> recipients = new ArrayList<>();

    /**
     * The message cache of the private channel.
     */
    private final ImplMessageCache messageCache;

    /**
     * Creates a new private channel.
     *
     * @param api The discord api instance.
     * @param data The json data of the channel.
     */
    public ImplGroupChannel(ImplDiscordApi api, JsonNode data) {
        this.api = api;

        for (JsonNode recipientJson : data.get("recipients")) {
            recipients.add(api.getOrCreateUser(recipientJson));
        }

        this.messageCache = new ImplMessageCache(
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
            return Optional.of(new ImplIcon(
                    getApi(),
                    new URL("https://cdn.discordapp.com/channel-icons/" + getIdAsString() + "/" + iconId + ".png")));
        } catch (MalformedURLException e) {
            logger.warn("Seems like the url of the icon is malformed! Please contact the developer!", e);
            return Optional.empty();
        }
    }

    @Override
    public GroupChannelUpdater createUpdater() {
        return new ImplGroupChannelUpdater(this);
    }

    @Override
    public ListenerManager<GroupChannelChangeNameListener> addGroupChannelChangeNameListener(
            GroupChannelChangeNameListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                GroupChannel.class, getId(), GroupChannelChangeNameListener.class, listener);
    }

    @Override
    public List<GroupChannelChangeNameListener> getGroupChannelChangeNameListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
                GroupChannel.class, getId(), GroupChannelChangeNameListener.class);
    }

    @Override
    public ListenerManager<GroupChannelDeleteListener> addGroupChannelDeleteListener(
            GroupChannelDeleteListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                GroupChannel.class, getId(), GroupChannelDeleteListener.class, listener);
    }

    @Override
    public List<GroupChannelDeleteListener> getGroupChannelDeleteListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(
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
                        return Stream.of(((ImplDiscordApi) getApi()).addObjectListener(GroupChannel.class, getId(),
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
                        ((ImplDiscordApi) getApi()).removeObjectListener(GroupChannel.class, getId(),
                                                                         listenerClass, listener);
                    }
                });
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends GroupChannelAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
    getGroupChannelAttachableListeners() {
        Map<T, List<Class<T>>> groupChannelListeners =
                ((ImplDiscordApi) getApi()).getObjectListeners(GroupChannel.class, getId());
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
        ((ImplDiscordApi) getApi()).removeObjectListener(GroupChannel.class, getId(), listenerClass, listener);
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

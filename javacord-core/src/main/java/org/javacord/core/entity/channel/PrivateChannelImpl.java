package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.channel.PrivateChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.listener.ChannelAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.TextChannelAttachableListener;
import org.javacord.api.listener.VoiceChannelAttachableListener;
import org.javacord.api.listener.channel.user.PrivateChannelAttachableListener;
import org.javacord.api.listener.channel.user.PrivateChannelDeleteListener;
import org.javacord.api.util.cache.MessageCache;
import org.javacord.api.util.event.ListenerManager;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.user.UserImpl;
import org.javacord.core.util.ClassHelper;
import org.javacord.core.util.Cleanupable;
import org.javacord.core.util.cache.MessageCacheImpl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The implementation of {@link PrivateChannel}.
 */
public class PrivateChannelImpl
        implements PrivateChannel, Cleanupable, InternalChannel, InternalTextChannel, InternalVoiceChannel {

    /**
     * The discord api instance.
     */
    private final DiscordApiImpl api;

    /**
     * The id of the channel.
     */
    private final long id;

    /**
     * The recipient of the private channel.
     */
    private final UserImpl recipient;

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
    public PrivateChannelImpl(DiscordApiImpl api, JsonNode data) {
        this.api = api;
        this.recipient = (UserImpl) api.getOrCreateUser(data.get("recipients").get(0));
        this.messageCache = new MessageCacheImpl(
                api, api.getDefaultMessageCacheCapacity(), api.getDefaultMessageCacheStorageTimeInSeconds());

        id = Long.parseLong(data.get("id").asText());
        recipient.setChannel(this);
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
    public User getRecipient() {
        return recipient;
    }

    @Override
    public ListenerManager<PrivateChannelDeleteListener> addPrivateChannelDeleteListener(
            PrivateChannelDeleteListener listener) {
        return ((DiscordApiImpl) getApi()).addObjectListener(
                PrivateChannel.class, getId(), PrivateChannelDeleteListener.class, listener);
    }

    @Override
    public List<PrivateChannelDeleteListener> getPrivateChannelDeleteListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(
                PrivateChannel.class, getId(), PrivateChannelDeleteListener.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends PrivateChannelAttachableListener & ObjectAttachableListener>
            Collection<ListenerManager<? extends PrivateChannelAttachableListener>>
                    addPrivateChannelAttachableListener(T listener) {
        return ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(PrivateChannelAttachableListener.class::isAssignableFrom)
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
                        return Stream.of(((DiscordApiImpl) getApi()).addObjectListener(PrivateChannel.class, getId(),
                                                                                       listenerClass, listener));
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends PrivateChannelAttachableListener & ObjectAttachableListener> void
            removePrivateChannelAttachableListener(T listener) {
        ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(PrivateChannelAttachableListener.class::isAssignableFrom)
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
                        ((DiscordApiImpl) getApi()).removeObjectListener(PrivateChannel.class, getId(),
                                                                         listenerClass, listener);
                    }
                });
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends PrivateChannelAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
            getPrivateChannelAttachableListeners() {
        Map<T, List<Class<T>>> privateChannelListeners =
                ((DiscordApiImpl) getApi()).getObjectListeners(PrivateChannel.class, getId());
        getTextChannelAttachableListeners().forEach((listener, listenerClasses) -> privateChannelListeners
                .merge((T) listener,
                        (List<Class<T>>) (Object) listenerClasses,
                        (listenerClasses1, listenerClasses2) -> {
                            listenerClasses1.addAll(listenerClasses2);
                            return listenerClasses1;
                        }));
        getVoiceChannelAttachableListeners().forEach((listener, listenerClasses) -> privateChannelListeners
                .merge((T) listener,
                        (List<Class<T>>) (Object) listenerClasses,
                        (listenerClasses1, listenerClasses2) -> {
                            listenerClasses1.addAll(listenerClasses2);
                            return listenerClasses1;
                        }));
        getChannelAttachableListeners().forEach((listener, listenerClasses) -> privateChannelListeners
                .merge((T) listener,
                        (List<Class<T>>) (Object) listenerClasses,
                        (listenerClasses1, listenerClasses2) -> {
                            listenerClasses1.addAll(listenerClasses2);
                            return listenerClasses1;
                        }));
        return privateChannelListeners;
    }

    @Override
    public <T extends PrivateChannelAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener) {
        ((DiscordApiImpl) getApi()).removeObjectListener(PrivateChannel.class, getId(), listenerClass, listener);
    }

    @Override
    public MessageCache getMessageCache() {
        return messageCache;
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
        return String.format("PrivateChannel (id: %s, recipient: %s)", getIdAsString(), getRecipient());
    }

}

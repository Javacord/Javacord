package org.javacord.core.entity.channel;

import org.javacord.api.entity.channel.Channel;
import org.javacord.api.listener.ChannelAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.util.event.ListenerManager;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.util.ClassHelper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface InternalChannel extends Channel {
    @Override
    @SuppressWarnings("unchecked")
    default <T extends ChannelAttachableListener & ObjectAttachableListener> Collection<ListenerManager<T>>
    addChannelAttachableListener(T listener) {
        return ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(ChannelAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .map(listenerClass -> ((DiscordApiImpl) getApi()).addObjectListener(Channel.class, getId(),
                                                                                    listenerClass, listener))
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    default <T extends ChannelAttachableListener & ObjectAttachableListener> void removeChannelAttachableListener(
            T listener) {
        ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(ChannelAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .forEach(listenerClass -> ((DiscordApiImpl) getApi()).removeObjectListener(Channel.class, getId(),
                                                                                           listenerClass, listener));
    }

    @Override
    default <T extends ChannelAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
    getChannelAttachableListeners() {
        return ((DiscordApiImpl) getApi()).getObjectListeners(Channel.class, getId());
    }

    @Override
    default <T extends ChannelAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener) {
        ((DiscordApiImpl) getApi()).removeObjectListener(Channel.class, getId(), listenerClass, listener);
    }

}

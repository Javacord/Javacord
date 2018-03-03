package de.btobastian.javacord.entity.channel;

import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.listener.ChannelAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.util.ClassHelper;
import de.btobastian.javacord.util.event.ListenerManager;

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
                .map(listenerClass -> ((ImplDiscordApi) getApi()).addObjectListener(Channel.class, getId(),
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
                .forEach(listenerClass -> ((ImplDiscordApi) getApi()).removeObjectListener(Channel.class, getId(),
                                                                                           listenerClass, listener));
    }

    @Override
    default <T extends ChannelAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
    getChannelAttachableListeners() {
        return ((ImplDiscordApi) getApi()).getObjectListeners(Channel.class, getId());
    }

    @Override
    default <T extends ChannelAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener) {
        ((ImplDiscordApi) getApi()).removeObjectListener(Channel.class, getId(), listenerClass, listener);
    }

}
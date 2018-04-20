package org.javacord.core.entity.channel;

import org.javacord.api.entity.channel.VoiceChannel;
import org.javacord.api.listener.ChannelAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.VoiceChannelAttachableListener;
import org.javacord.api.util.event.ListenerManager;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.util.ClassHelper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface InternalVoiceChannel extends VoiceChannel {

    @Override
    @SuppressWarnings("unchecked")
    default <T extends VoiceChannelAttachableListener & ObjectAttachableListener>
            Collection<ListenerManager<? extends VoiceChannelAttachableListener>>
                    addVoiceChannelAttachableListener(T listener) {
        return ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(VoiceChannelAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .flatMap(listenerClass -> {
                    if (ChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        return addChannelAttachableListener(
                                (ChannelAttachableListener & ObjectAttachableListener) listener).stream();
                    } else {
                        return Stream.of(((DiscordApiImpl) getApi()).addObjectListener(VoiceChannel.class, getId(),
                                                                                       listenerClass, listener));
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    default <T extends VoiceChannelAttachableListener & ObjectAttachableListener> void
            removeVoiceChannelAttachableListener(T listener) {
        ClassHelper.getInterfacesAsStream(listener.getClass())
                .filter(VoiceChannelAttachableListener.class::isAssignableFrom)
                .filter(ObjectAttachableListener.class::isAssignableFrom)
                .map(listenerClass -> (Class<T>) listenerClass)
                .forEach(listenerClass -> {
                    if (ChannelAttachableListener.class.isAssignableFrom(listenerClass)) {
                        removeChannelAttachableListener(
                                (ChannelAttachableListener & ObjectAttachableListener) listener);
                    } else {
                        ((DiscordApiImpl) getApi()).removeObjectListener(VoiceChannel.class, getId(),
                                                                         listenerClass, listener);
                    }
                });
    }

    @Override
    @SuppressWarnings("unchecked")
    default <T extends VoiceChannelAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
            getVoiceChannelAttachableListeners() {
        Map<T, List<Class<T>>> voiceChannelListeners =
                ((DiscordApiImpl) getApi()).getObjectListeners(VoiceChannel.class, getId());
        getChannelAttachableListeners().forEach((listener, listenerClasses) -> voiceChannelListeners
                .merge((T) listener,
                        (List<Class<T>>) (Object) listenerClasses,
                        (listenerClasses1, listenerClasses2) -> {
                            listenerClasses1.addAll(listenerClasses2);
                            return listenerClasses1;
                        }));
        return voiceChannelListeners;
    }

    @Override
    default <T extends VoiceChannelAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener) {
        ((DiscordApiImpl) getApi()).removeObjectListener(VoiceChannel.class, getId(), listenerClass, listener);
    }

}

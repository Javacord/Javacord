package de.btobastian.javacord.listeners.server.channel;

import de.btobastian.javacord.events.server.channel.ServerVoiceChannelChangeUserLimitEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;

/**
 * This listener listens to server voice channel user limit changes.
 */
@FunctionalInterface
public interface ServerVoiceChannelChangeUserLimitListener extends ServerAttachableListener,
                                                              ServerVoiceChannelAttachableListener,
                                                              GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a server voice channel's user limit changes.
     *
     * @param event The event.
     */
    void onServerVoiceChannelChangeUserLimit(ServerVoiceChannelChangeUserLimitEvent event);
}

package de.btobastian.javacord.listeners.server.channel;

import de.btobastian.javacord.events.server.channel.ServerTextChannelChangeTopicEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;

/**
 * This listener listens to server text channel topic changes.
 */
@FunctionalInterface
public interface ServerTextChannelChangeTopicListener extends ServerAttachableListener,
                                                              ServerTextChannelAttachableListener,
                                                              GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a server text channel's topic changes.
     *
     * @param event The event.
     */
    void onServerTextChannelChangeTopic(ServerTextChannelChangeTopicEvent event);
}

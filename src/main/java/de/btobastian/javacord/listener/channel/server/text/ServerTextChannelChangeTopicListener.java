package de.btobastian.javacord.listener.channel.server.text;

import de.btobastian.javacord.event.channel.server.text.ServerTextChannelChangeTopicEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;

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

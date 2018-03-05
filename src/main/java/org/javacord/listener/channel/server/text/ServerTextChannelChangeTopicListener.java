package org.javacord.listener.channel.server.text;

import org.javacord.event.channel.server.text.ServerTextChannelChangeTopicEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;

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

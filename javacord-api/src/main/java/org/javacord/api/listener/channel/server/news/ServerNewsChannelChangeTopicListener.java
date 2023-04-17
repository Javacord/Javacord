package org.javacord.api.listener.channel.server.news;

import org.javacord.api.event.channel.server.news.ServerNewsChannelChangeTopicEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to server text channel topic changes.
 */
@FunctionalInterface
public interface ServerNewsChannelChangeTopicListener extends ServerAttachableListener,
        ServerNewsChannelAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a server text channel's topic changes.
     *
     * @param event The event.
     */
    void onServerNewsChannelChangeTopic(ServerNewsChannelChangeTopicEvent event);
}

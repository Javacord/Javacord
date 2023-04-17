package org.javacord.api.listener.channel.server.news;

import org.javacord.api.event.channel.server.news.ServerNewsChannelChangeDefaultAutoArchiveDurationEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to server news channel default auto archive duration changes.
 */
public interface ServerNewsChannelChangeDefaultAutoArchiveDurationListener extends ServerAttachableListener,
        ServerNewsChannelAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a server news channel's default auto archive duration changes.
     *
     * @param event The event.
     */
    void onServerNewsChannelChangeDefaultAutoArchiveDuration(
            ServerNewsChannelChangeDefaultAutoArchiveDurationEvent event);
}

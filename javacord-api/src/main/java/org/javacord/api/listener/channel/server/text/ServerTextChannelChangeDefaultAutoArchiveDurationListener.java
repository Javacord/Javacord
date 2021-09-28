package org.javacord.api.listener.channel.server.text;

import org.javacord.api.event.channel.server.text.ServerTextChannelChangeDefaultAutoArchiveDurationEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to server text channel default auto archive duration changes.
 */
@FunctionalInterface
public interface ServerTextChannelChangeDefaultAutoArchiveDurationListener extends ServerAttachableListener,
        ServerTextChannelAttachableListener, GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a server text channel's default auto archive duration changes.
     *
     * @param event The event.
     */
    void onServerTextChannelChangeDefaultAutoArchiveDuration(
            ServerTextChannelChangeDefaultAutoArchiveDurationEvent event);

}

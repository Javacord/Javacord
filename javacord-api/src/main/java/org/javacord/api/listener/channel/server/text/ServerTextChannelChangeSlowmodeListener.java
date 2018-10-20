package org.javacord.api.listener.channel.server.text;

import org.javacord.api.event.channel.server.text.ServerTextChannelChangeSlowmodeEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to server text channel slowmode delay changes.
 */
@FunctionalInterface
public interface ServerTextChannelChangeSlowmodeListener extends ServerAttachableListener,
        ServerTextChannelAttachableListener, GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a server text channel's slowmode delay changes.
     *
     * @param event The event.
     */
    void onServerTextChannelChangeSlowmodeDelay(ServerTextChannelChangeSlowmodeEvent event);

}

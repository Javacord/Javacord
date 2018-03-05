package org.javacord.listener.channel.server;

import org.javacord.event.channel.server.ServerChannelChangeNsfwFlagEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.channel.server.text.ServerTextChannelAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;

/**
 * This listener listens to server channel nsfw flag changes.
 */
@FunctionalInterface
public interface ServerChannelChangeNsfwFlagListener extends ServerAttachableListener,
        ServerTextChannelAttachableListener,
                                                             ChannelCategoryAttachableListener,
        GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a server channel's nsfw flag changes.
     *
     * @param event The event.
     */
    void onServerChannelChangeNsfwFlag(ServerChannelChangeNsfwFlagEvent event);
}

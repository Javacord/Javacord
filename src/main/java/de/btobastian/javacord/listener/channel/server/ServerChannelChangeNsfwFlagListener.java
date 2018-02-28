package de.btobastian.javacord.listener.channel.server;

import de.btobastian.javacord.event.channel.server.ServerChannelChangeNsfwFlagEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.channel.server.text.ServerTextChannelAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;

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

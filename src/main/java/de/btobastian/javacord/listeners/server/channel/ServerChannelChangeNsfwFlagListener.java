package de.btobastian.javacord.listeners.server.channel;

import de.btobastian.javacord.events.server.channel.ServerChannelChangeNsfwFlagEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;

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

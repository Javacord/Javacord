package de.btobastian.javacord.listeners.server.channel;

import de.btobastian.javacord.events.server.channel.ServerChannelChangeCategoryEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;

/**
 * This listener listens to server channel category changes.
 */
@FunctionalInterface
public interface ServerChannelChangeCategoryListener extends ObjectAttachableListener, ServerAttachableListener,
        ServerTextChannelAttachableListener, GloballyAttachableListener, ChannelCategoryAttachableListener {

    /**
     * This method is called every time a server channel's category changes.
     *
     * @param event The event.
     */
    void onServerChannelChangeCategory(ServerChannelChangeCategoryEvent event);
}

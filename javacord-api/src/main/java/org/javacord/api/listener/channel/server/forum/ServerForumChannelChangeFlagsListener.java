package org.javacord.api.listener.channel.server.forum;

import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeFlagsEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;

/**
 * This listener listens to channel flag changes.
 */
@FunctionalInterface
public interface ServerForumChannelChangeFlagsListener extends GloballyAttachableListener,
        ServerForumChannelAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a channel's flags change.
     *
     * @param event The event.
     */
    void onServerForumChannelChangeFlags(ServerForumChannelChangeFlagsEvent event);
}

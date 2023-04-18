package org.javacord.api.listener.channel.server.forum;

import org.javacord.api.event.channel.server.forum.ServerForumChannelChangePermissionOverwritesEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to server forum channel permission overwrite changes.
 */
public interface ServerForumChannelChangePermissionOverwritesListener extends ServerAttachableListener,
        GloballyAttachableListener, ServerForumChannelAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a server forum channel's permission overwrites change.
     *
     * @param event The event.
     */
    void onServerForumChannelChangePermissionOverwrites(ServerForumChannelChangePermissionOverwritesEvent event);
}

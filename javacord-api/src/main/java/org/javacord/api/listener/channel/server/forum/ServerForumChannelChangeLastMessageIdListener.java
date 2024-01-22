package org.javacord.api.listener.channel.server.forum;

import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeLastMessageIdEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to server forum channel last message id changes.
 */
public interface ServerForumChannelChangeLastMessageIdListener extends ServerAttachableListener,
        GloballyAttachableListener, ServerForumChannelAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a server forum channel's last message id changes.
     *
     * @param event The event.
     */
    void onServerForumChannelChangeLastMessageId(ServerForumChannelChangeLastMessageIdEvent event);
}

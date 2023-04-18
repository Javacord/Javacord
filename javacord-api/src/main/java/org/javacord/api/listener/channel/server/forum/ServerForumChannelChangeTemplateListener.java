package org.javacord.api.listener.channel.server.forum;

import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeTemplateEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to server forum channel template changes.
 */
public interface ServerForumChannelChangeTemplateListener extends ServerAttachableListener,
        GloballyAttachableListener, ServerForumChannelAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a server forum channel's template changes.
     *
     * @param event The event.
     */
    void onServerForumChannelChangeTemplate(ServerForumChannelChangeTemplateEvent event);
}

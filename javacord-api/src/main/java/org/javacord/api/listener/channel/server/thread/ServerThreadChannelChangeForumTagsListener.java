package org.javacord.api.listener.channel.server.thread;

import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeForumTagsEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.ServerThreadChannelAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to thread applied tag ids updates.
 */
public interface ServerThreadChannelChangeForumTagsListener extends ServerAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener, ServerThreadChannelAttachableListener {

    /**
     * This method is called every time a thread applied tag ids update.
     *
     * @param event The event.
     */
    void onServerThreadForumTagsUpdate(ServerThreadChannelChangeForumTagsEvent event);
}

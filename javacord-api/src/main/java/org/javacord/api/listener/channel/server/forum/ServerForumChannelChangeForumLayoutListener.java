
package org.javacord.api.listener.channel.server.forum;

import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeForumLayoutEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to forum layout changes.
 */
public interface ServerForumChannelChangeForumLayoutListener extends ServerAttachableListener,
        GloballyAttachableListener, ServerForumChannelAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time the forum layout changes.
     *
     * @param event The event.
     */
    void onServerForumChannelChangeForumLayout(ServerForumChannelChangeForumLayoutEvent event);
}

package org.javacord.api.listener.channel.server.forum;

import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeDefaultReactionEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to forum channel default reaction changes.
 */
public interface ServerForumChannelChangeDefaultReactionListener extends ServerAttachableListener,
        GloballyAttachableListener, ServerForumChannelAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a forum channel's default reaction changes.
     *
     * @param event The event.
     */
    void onServerForumChannelChangeDefaultReaction(ServerForumChannelChangeDefaultReactionEvent event);
}

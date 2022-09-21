package org.javacord.api.listener.channel.server.forum;

import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeDefaultReactionEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;

/**
 * This listener listens to forum channel default reaction changes.
 */
public interface ServerForumChannelChangeDefaultReactionListener extends GloballyAttachableListener,
        ServerForumChannelAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a forum channel's default reaction changes.
     *
     * @param event The event.
     */
    void onServerForumChannelChangeDefaultReaction(ServerForumChannelChangeDefaultReactionEvent event);
}

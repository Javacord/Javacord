package org.javacord.api.listener.channel.server;

import org.javacord.api.event.channel.server.ServerChannelChangeNsfwFlagEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.server.forum.ServerForumChannelAttachableListener;
import org.javacord.api.listener.channel.server.text.ServerTextChannelAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to server channel nsfw flag changes.
 */
@FunctionalInterface
public interface ServerChannelChangeNsfwFlagListener extends ServerAttachableListener,
        ServerTextChannelAttachableListener, ChannelCategoryAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener,
        ServerForumChannelAttachableListener {

    /**
     * This method is called every time a server channel's nsfw flag changes.
     *
     * @param event The event.
     */
    void onServerChannelChangeNsfwFlag(ServerChannelChangeNsfwFlagEvent event);
}

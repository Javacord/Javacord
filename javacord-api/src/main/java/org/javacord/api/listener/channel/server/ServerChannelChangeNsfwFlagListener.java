package org.javacord.api.listener.channel.server;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.channel.server.ServerChannelChangeNsfwFlagEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.server.text.ServerTextChannelAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to server channel nsfw flag changes.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILDS})
public interface ServerChannelChangeNsfwFlagListener extends ServerAttachableListener,
        ServerTextChannelAttachableListener,
                                                             ChannelCategoryAttachableListener,
        GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a server channel's nsfw flag changes.
     *
     * @param event The event.
     */
    void onServerChannelChangeNsfwFlag(ServerChannelChangeNsfwFlagEvent event);
}

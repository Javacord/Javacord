package org.javacord.api.listener.channel.server.text;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.channel.server.text.ServerTextChannelChangeDefaultAutoArchiveDurationEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to server text channel default auto archive duration changes.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILDS})
public interface ServerTextChannelChangeDefaultAutoArchiveDurationListener extends ServerAttachableListener,
        ServerTextChannelAttachableListener, GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a server text channel's default auto archive duration changes.
     *
     * @param event The event.
     */
    void onServerTextChannelChangeDefaultAutoArchiveDuration(
            ServerTextChannelChangeDefaultAutoArchiveDurationEvent event);

}

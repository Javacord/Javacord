package org.javacord.api.listener.message;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.message.ChannelPinsUpdateEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.TextChannelAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to channel pin updates.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILDS, Intent.DIRECT_MESSAGES})
public interface ChannelPinsUpdateListener extends TextChannelAttachableListener, ServerAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a channel's pins update.
     *
     * @param event The event.
     */
    void onChannelPinsUpdate(ChannelPinsUpdateEvent event);
}

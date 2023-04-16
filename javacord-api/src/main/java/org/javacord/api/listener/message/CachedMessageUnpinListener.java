package org.javacord.api.listener.message;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.message.CachedMessageUnpinEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.TextChannelAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to message unpins of <b>cached</b> messages.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILD_MESSAGES, Intent.DIRECT_MESSAGES})
public interface CachedMessageUnpinListener extends ServerAttachableListener, TextChannelAttachableListener,
                                                    MessageAttachableListener, GloballyAttachableListener,
                                                    ObjectAttachableListener {

    /**
     * This method is called every time a cached message gets unpinned.
     *
     * @param event The event.
     */
    void onCachedMessageUnpin(CachedMessageUnpinEvent event);

}

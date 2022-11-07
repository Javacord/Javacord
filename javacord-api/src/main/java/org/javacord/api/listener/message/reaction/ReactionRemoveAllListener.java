package org.javacord.api.listener.message.reaction;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.message.reaction.ReactionRemoveAllEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.TextChannelAttachableListener;
import org.javacord.api.listener.message.MessageAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to all reaction being deleted at once.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILD_MESSAGE_REACTIONS, Intent.DIRECT_MESSAGE_REACTIONS})
public interface ReactionRemoveAllListener extends ServerAttachableListener, TextChannelAttachableListener,
        MessageAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time all reactions were removed from a message.
     *
     * @param event The event.
     */
    void onReactionRemoveAll(ReactionRemoveAllEvent event);

}

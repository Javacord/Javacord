package org.javacord.api.listener.message.reaction;

import org.javacord.api.event.message.reaction.ReactionRemoveAllEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.TextChannelAttachableListener;
import org.javacord.api.listener.message.MessageAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to all reaction being delete at once.
 */
@FunctionalInterface
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

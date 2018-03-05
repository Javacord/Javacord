package org.javacord.listener.message.reaction;

import org.javacord.event.message.reaction.ReactionRemoveAllEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.TextChannelAttachableListener;
import org.javacord.listener.message.MessageAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;

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

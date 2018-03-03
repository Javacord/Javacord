package org.javacord.listener.message.reaction;

import org.javacord.event.message.reaction.ReactionRemoveEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.TextChannelAttachableListener;
import org.javacord.listener.message.MessageAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;
import org.javacord.listener.user.UserAttachableListener;
import org.javacord.event.message.reaction.ReactionRemoveEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.TextChannelAttachableListener;
import org.javacord.listener.message.MessageAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;
import org.javacord.listener.user.UserAttachableListener;

/**
 * This listener listens to reaction deletions.
 */
@FunctionalInterface
public interface ReactionRemoveListener extends ServerAttachableListener, UserAttachableListener,
        TextChannelAttachableListener, MessageAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a reaction is removed from a message.
     *
     * @param event The event.
     */
    void onReactionRemove(ReactionRemoveEvent event);

}

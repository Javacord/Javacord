package org.javacord.api.listener.message.reaction;

import org.javacord.api.event.message.reaction.ReactionRemoveEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.TextChannelAttachableListener;
import org.javacord.api.listener.message.MessageAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.listener.user.UserAttachableListener;

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

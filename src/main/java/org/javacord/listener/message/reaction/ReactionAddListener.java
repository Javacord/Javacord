package org.javacord.listener.message.reaction;

import org.javacord.event.message.reaction.ReactionAddEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.TextChannelAttachableListener;
import org.javacord.listener.message.MessageAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;
import org.javacord.listener.user.UserAttachableListener;

/**
 * This listener listens to reaction adding.
 */
@FunctionalInterface
public interface ReactionAddListener extends ServerAttachableListener, UserAttachableListener,
        TextChannelAttachableListener, MessageAttachableListener,
        GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a reaction is added to a message.
     *
     * @param event The event.
     */
    void onReactionAdd(ReactionAddEvent event);

}

package de.btobastian.javacord.listener.message.reaction;

import de.btobastian.javacord.event.message.reaction.ReactionAddEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.TextChannelAttachableListener;
import de.btobastian.javacord.listener.message.MessageAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;
import de.btobastian.javacord.listener.user.UserAttachableListener;

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

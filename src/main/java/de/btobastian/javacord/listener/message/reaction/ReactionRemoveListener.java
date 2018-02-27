package de.btobastian.javacord.listener.message.reaction;

import de.btobastian.javacord.event.message.reaction.ReactionRemoveEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.TextChannelAttachableListener;
import de.btobastian.javacord.listener.message.MessageAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;
import de.btobastian.javacord.listener.user.UserAttachableListener;

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

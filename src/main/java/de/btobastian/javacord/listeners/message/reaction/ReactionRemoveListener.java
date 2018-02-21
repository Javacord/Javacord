package de.btobastian.javacord.listeners.message.reaction;

import de.btobastian.javacord.events.message.reaction.ReactionRemoveEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.TextChannelAttachableListener;
import de.btobastian.javacord.listeners.message.MessageAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;
import de.btobastian.javacord.listeners.user.UserAttachableListener;

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

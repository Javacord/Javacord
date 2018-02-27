package de.btobastian.javacord.listener.message.reaction;

import de.btobastian.javacord.event.message.reaction.ReactionRemoveAllEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.TextChannelAttachableListener;
import de.btobastian.javacord.listener.message.MessageAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;

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

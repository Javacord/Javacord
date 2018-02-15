package de.btobastian.javacord.listeners.message.reaction;

import de.btobastian.javacord.events.message.reaction.ReactionRemoveAllEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.TextChannelAttachableListener;
import de.btobastian.javacord.listeners.message.MessageAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;

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

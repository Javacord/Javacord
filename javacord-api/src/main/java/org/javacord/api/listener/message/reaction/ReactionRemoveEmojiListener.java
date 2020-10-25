package org.javacord.api.listener.message.reaction;

import org.javacord.api.event.message.reaction.ReactionRemoveEmojiEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.TextChannelAttachableListener;
import org.javacord.api.listener.message.MessageAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;

/**
 * This listener listens to all of a single reaction being deleted at once.
 */
@FunctionalInterface
public interface ReactionRemoveEmojiListener extends ServerAttachableListener, TextChannelAttachableListener,
        MessageAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time all of a single reaction is removed from a message.
     *
     * @param event The event.
     */
    void onReactionRemoveEmoji(ReactionRemoveEmojiEvent event);
}

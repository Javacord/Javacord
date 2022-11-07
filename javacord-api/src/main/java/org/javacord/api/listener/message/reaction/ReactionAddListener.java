package org.javacord.api.listener.message.reaction;

import org.javacord.api.entity.intent.Intent;
import org.javacord.api.event.message.reaction.ReactionAddEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.TextChannelAttachableListener;
import org.javacord.api.listener.message.MessageAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.listener.user.UserAttachableListener;
import org.javacord.api.util.annotation.RequiredIntent;

/**
 * This listener listens to reaction adding.
 */
@FunctionalInterface
@RequiredIntent({Intent.GUILD_MESSAGE_REACTIONS, Intent.DIRECT_MESSAGE_REACTIONS})
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

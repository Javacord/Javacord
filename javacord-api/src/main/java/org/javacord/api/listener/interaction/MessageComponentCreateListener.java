package org.javacord.api.listener.interaction;

import org.javacord.api.event.interaction.MessageComponentCreateEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.TextChannelAttachableListener;
import org.javacord.api.listener.message.MessageAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.listener.server.member.ServerMemberAttachableListener;
import org.javacord.api.listener.user.UserAttachableListener;

/**
 * This listener listens to message component interaction creations.
 */
@FunctionalInterface
public interface MessageComponentCreateListener extends ServerMemberAttachableListener, ServerAttachableListener,
        UserAttachableListener, TextChannelAttachableListener, MessageAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a message component interaction is created.
     *
     * @param event The event.
     */
    void onComponentCreate(MessageComponentCreateEvent event);
}

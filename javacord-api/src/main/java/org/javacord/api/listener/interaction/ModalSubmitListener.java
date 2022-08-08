package org.javacord.api.listener.interaction;

import org.javacord.api.event.interaction.ModalSubmitEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.TextChannelAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.listener.server.member.ServerMemberAttachableListener;
import org.javacord.api.listener.user.UserAttachableListener;

/**
 * This listener listens to modal submit interaction creations.
 */
@FunctionalInterface
public interface ModalSubmitListener extends ServerMemberAttachableListener, ServerAttachableListener,
        UserAttachableListener, TextChannelAttachableListener, GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a modal interaction is created.
     *
     * @param event The event.
     */
    void onModalSubmit(ModalSubmitEvent event);
}

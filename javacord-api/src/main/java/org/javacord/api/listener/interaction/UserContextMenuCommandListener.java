package org.javacord.api.listener.interaction;

import org.javacord.api.event.interaction.UserContextMenuCommandEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.TextChannelAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.listener.server.member.ServerMemberAttachableListener;
import org.javacord.api.listener.user.UserAttachableListener;

/**
 * This listener listens to user context menu interaction creations.
 */
public interface UserContextMenuCommandListener extends ServerMemberAttachableListener, ServerAttachableListener,
        UserAttachableListener, TextChannelAttachableListener, GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time an interaction is created.
     *
     * @param event The event.
     */
    void onUserContextMenuCommand(UserContextMenuCommandEvent event);
}

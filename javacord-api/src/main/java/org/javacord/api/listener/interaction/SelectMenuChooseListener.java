package org.javacord.api.listener.interaction;

import org.javacord.api.event.interaction.SelectMenuChooseEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.TextChannelAttachableListener;
import org.javacord.api.listener.message.MessageAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.listener.server.member.ServerMemberAttachableListener;
import org.javacord.api.listener.user.UserAttachableListener;

/**
 * This listener listens to select menu choices.
 */
@FunctionalInterface
public interface SelectMenuChooseListener extends ServerMemberAttachableListener, ServerAttachableListener,
        UserAttachableListener, TextChannelAttachableListener, MessageAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time when one or more select menu options have been chosen.
     *
     * @param event The event.
     */
    void onSelectMenuChoose(SelectMenuChooseEvent event);
}

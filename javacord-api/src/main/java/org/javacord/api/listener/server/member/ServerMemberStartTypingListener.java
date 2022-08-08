package org.javacord.api.listener.server.member;

import org.javacord.api.event.server.member.ServerMemberUserStartTypingEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.TextChannelAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.listener.user.UserAttachableListener;

/**
 * This listener listens to users typing.
 * If the user starts typing the "xyz is typing..." message is displayed for 5 seconds.
 * It also stops if the user sent a message.
 */
@FunctionalInterface
public interface ServerMemberStartTypingListener extends ServerMemberAttachableListener, ServerAttachableListener,
        UserAttachableListener, TextChannelAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a member started typing.
     *
     * @param event The event.
     */
    void onUserStartTyping(ServerMemberUserStartTypingEvent event);
}

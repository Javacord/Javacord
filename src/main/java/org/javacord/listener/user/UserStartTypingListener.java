package org.javacord.listener.user;


import org.javacord.event.user.UserStartTypingEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.TextChannelAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;

/**
 * This listener listens to users typing.
 * If the user starts typing the "xyz is typing..." message is displayed for 5 seconds.
 * It also stops if the user sent a message.
 */
@FunctionalInterface
public interface UserStartTypingListener extends ServerAttachableListener, UserAttachableListener,
        TextChannelAttachableListener, GloballyAttachableListener,
        ObjectAttachableListener {

    /**
     * This method is called every time a user started typing.
     *
     * @param event The event.
     */
    void onUserStartTyping(UserStartTypingEvent event);
}

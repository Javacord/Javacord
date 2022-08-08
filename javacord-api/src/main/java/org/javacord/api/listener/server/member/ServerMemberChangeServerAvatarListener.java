package org.javacord.api.listener.server.member;

import org.javacord.api.event.server.member.ServerMemberChangeServerAvatarEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.listener.user.UserAttachableListener;

/**
 * This listener listens to member server avatar changes.
 */
@FunctionalInterface
public interface ServerMemberChangeServerAvatarListener extends ServerMemberAttachableListener,
        ServerAttachableListener, UserAttachableListener, GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a member changes their server avatar.
     *
     * @param event The event.
     */
    void onUserChangeServerAvatar(ServerMemberChangeServerAvatarEvent event);

}

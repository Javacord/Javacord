package de.btobastian.javacord.listeners.server.member;

import de.btobastian.javacord.events.server.member.ServerMemberLeaveEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;
import de.btobastian.javacord.listeners.server.ServerLeaveListener;
import de.btobastian.javacord.listeners.user.UserAttachableListener;

/**
 * This listener listens to server member leaves.
 * Do not confuse it with the {@link ServerLeaveListener}:
 * ServerMemberRemoveListener is for other users and ServerLeaveListener is for yourself!
 */
@FunctionalInterface
public interface ServerMemberLeaveListener extends ServerAttachableListener, UserAttachableListener,
                                                   GloballyAttachableListener, ObjectAttachableListener {

    /**
     * This method is called every time a user leaves a server.
     *
     * @param event the event.
     */
    void onServerMemberLeave(ServerMemberLeaveEvent event);
}

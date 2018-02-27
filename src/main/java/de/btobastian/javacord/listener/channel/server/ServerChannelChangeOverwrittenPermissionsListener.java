package de.btobastian.javacord.listener.channel.server;

import de.btobastian.javacord.event.channel.server.ServerChannelChangeOverwrittenPermissionsEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;
import de.btobastian.javacord.listener.server.ServerAttachableListener;
import de.btobastian.javacord.listener.server.role.RoleAttachableListener;
import de.btobastian.javacord.listener.user.UserAttachableListener;

/**
 * This listener listens to server channel overwritten permissions changes.
 */
@FunctionalInterface
public interface ServerChannelChangeOverwrittenPermissionsListener extends ServerAttachableListener,
                                                                           UserAttachableListener,
                                                                           ServerChannelAttachableListener,
                                                                           RoleAttachableListener,
                                                                           GloballyAttachableListener,
                                                                           ObjectAttachableListener {

    /**
     * This method is called every time a server channel's overwritten permissions change.
     *
     * @param event The event.
     */
    void onServerChannelChangeOverwrittenPermissions(ServerChannelChangeOverwrittenPermissionsEvent event);
}

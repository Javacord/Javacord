package org.javacord.listener.channel.server;

import org.javacord.event.channel.server.ServerChannelChangeOverwrittenPermissionsEvent;
import org.javacord.listener.GloballyAttachableListener;
import org.javacord.listener.ObjectAttachableListener;
import org.javacord.listener.server.ServerAttachableListener;
import org.javacord.listener.server.role.RoleAttachableListener;
import org.javacord.listener.user.UserAttachableListener;

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

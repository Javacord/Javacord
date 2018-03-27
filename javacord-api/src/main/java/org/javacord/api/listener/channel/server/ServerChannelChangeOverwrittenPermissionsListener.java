package org.javacord.api.listener.channel.server;

import org.javacord.api.event.channel.server.ServerChannelChangeOverwrittenPermissionsEvent;
import org.javacord.api.listener.GloballyAttachableListener;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.server.ServerAttachableListener;
import org.javacord.api.listener.server.role.RoleAttachableListener;
import org.javacord.api.listener.user.UserAttachableListener;

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

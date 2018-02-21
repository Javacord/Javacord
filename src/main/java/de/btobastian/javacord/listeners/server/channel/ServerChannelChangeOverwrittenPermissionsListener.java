package de.btobastian.javacord.listeners.server.channel;

import de.btobastian.javacord.events.server.channel.ServerChannelChangeOverwrittenPermissionsEvent;
import de.btobastian.javacord.listeners.GloballyAttachableListener;
import de.btobastian.javacord.listeners.ObjectAttachableListener;
import de.btobastian.javacord.listeners.server.ServerAttachableListener;
import de.btobastian.javacord.listeners.server.role.RoleAttachableListener;
import de.btobastian.javacord.listeners.user.UserAttachableListener;

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

package de.btobastian.javacord.listeners.server.channel;

import de.btobastian.javacord.events.server.channel.ServerChannelChangeOverwrittenPermissionsEvent;

/**
 * This listener listens to server channel overwritten permissions changes.
 */
@FunctionalInterface
public interface ServerChannelChangeOverwrittenPermissionsListener {

    /**
     * This method is called every time a server channel's overwritten permissions change.
     *
     * @param event The event.
     */
    void onServerChannelChangeOverwrittenPermissions(ServerChannelChangeOverwrittenPermissionsEvent event);
}

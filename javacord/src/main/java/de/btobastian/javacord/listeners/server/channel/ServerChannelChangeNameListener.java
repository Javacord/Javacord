package de.btobastian.javacord.listeners.server.channel;

import de.btobastian.javacord.events.server.channel.ServerChannelChangeNameEvent;

/**
 * This listener listens to server channel name changes.
 */
@FunctionalInterface
public interface ServerChannelChangeNameListener {

    /**
     * This method is called every time a server channel's name changes.
     *
     * @param event The event.
     */
    void onServerChannelChangeName(ServerChannelChangeNameEvent event);
}

package de.btobastian.javacord.listeners.server.channel;

import de.btobastian.javacord.events.server.channel.ServerChannelDeleteEvent;

/**
 * This listener listens to server channel deletions.
 */
@FunctionalInterface
public interface ServerChannelDeleteListener {

    /**
     * This method is called every time a server channel is deleted.
     *
     * @param event The event.
     */
    void onServerChannelDelete(ServerChannelDeleteEvent event);

}

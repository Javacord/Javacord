package de.btobastian.javacord.listeners.server.channel;

import de.btobastian.javacord.events.server.channel.ServerChannelCreateEvent;

/**
 * This listener listens to server channel creations.
 */
@FunctionalInterface
public interface ServerChannelCreateListener {

    /**
     * This method is called every time a server channel is created.
     *
     * @param event The event.
     */
    void onServerChannelCreate(ServerChannelCreateEvent event);
}

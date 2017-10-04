package de.btobastian.javacord.listeners.server.channel;

import de.btobastian.javacord.events.server.channel.ServerChannelChangePositionEvent;

/**
 * This listener listens to server channel position changes.
 */
@FunctionalInterface
public interface ServerChannelChangePositionListener {

    /**
     * This method is called every time a server channel's position changes.
     *
     * @param event The event.
     */
    void onServerChannelChangePosition(ServerChannelChangePositionEvent event);
}

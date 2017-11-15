package de.btobastian.javacord.listeners.server.channel;

import de.btobastian.javacord.events.server.channel.ServerTextChannelChangeTopicEvent;

/**
 * This listener listens to server text channel topic changes.
 */
@FunctionalInterface
public interface ServerTextChannelChangeTopicListener {

    /**
     * This method is called every time a server text channel's topic changes.
     *
     * @param event The event.
     */
    void onServerTextChannelChangeTopic(ServerTextChannelChangeTopicEvent event);
}

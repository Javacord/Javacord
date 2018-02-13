package de.btobastian.javacord.listeners.user.channel;

import de.btobastian.javacord.events.user.channel.PrivateChannelCreateEvent;

/**
 * This listener listens to private channel creations.
 */
@FunctionalInterface
public interface PrivateChannelCreateListener {

    /**
     * This method is called every time a private channel is created.
     *
     * @param event The event.
     */
    void onPrivateChannelCreate(PrivateChannelCreateEvent event);
}

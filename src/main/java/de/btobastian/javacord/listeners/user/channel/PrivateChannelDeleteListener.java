package de.btobastian.javacord.listeners.user.channel;

import de.btobastian.javacord.events.user.channel.PrivateChannelDeleteEvent;

/**
 * This listener listens to private channel deletions.
 */
@FunctionalInterface
public interface PrivateChannelDeleteListener {

    /**
     * This method is called every time a private channel is deleted.
     *
     * @param event The event.
     */
    void onPrivateChannelDelete(PrivateChannelDeleteEvent event);

}

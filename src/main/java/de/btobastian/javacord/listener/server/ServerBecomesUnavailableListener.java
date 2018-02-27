package de.btobastian.javacord.listener.server;

import de.btobastian.javacord.event.server.ServerBecomesUnavailableEvent;
import de.btobastian.javacord.listener.GloballyAttachableListener;
import de.btobastian.javacord.listener.ObjectAttachableListener;

/**
 * This listener listens to servers becoming available.
 * Unavailability means, that a Discord server is down due to a temporary outage.
 *
 * @see <a href="https://discordapp.com/developers/docs/topics/gateway#guild-unavailability">Discord docs</a>
 */
@FunctionalInterface
public interface ServerBecomesUnavailableListener extends ServerAttachableListener, GloballyAttachableListener,
                                                          ObjectAttachableListener {

    /**
     * This method is called every time a server became available.
     *
     * @param event The event.
     */
    void onServerBecomesUnavailable(ServerBecomesUnavailableEvent event);
}

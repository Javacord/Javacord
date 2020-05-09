package org.javacord.api.listener.server;

import org.javacord.api.event.server.ServerBecomesAvailableEvent;
import org.javacord.api.listener.GloballyAttachableListener;

/**
 * This listener listens to servers becoming available.
 * Unavailability means, that a Discord server is down due to a temporary outage.
 *
 * @see <a href="https://discord.com/developers/docs/topics/gateway#guild-unavailability">Discord docs</a>
 */
@FunctionalInterface
public interface ServerBecomesAvailableListener extends GloballyAttachableListener {

    /**
     * This method is called every time a server became unavailable.
     *
     * @param event The event.
     */
    void onServerBecomesAvailable(ServerBecomesAvailableEvent event);
}

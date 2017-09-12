package de.btobastian.javacord.listeners.server;

import de.btobastian.javacord.events.server.ServerBecomesAvailableEvent;

/**
 * This listener listens to servers becoming available.
 * Unavailability means, that a Discord server is down due to a temporary outage.
 *
 * @see <a href="https://discordapp.com/developers/docs/topics/gateway#guild-unavailability">Discord docs</a>
 */
@FunctionalInterface
public interface ServerBecomesUnavailableListener {

    /**
     * This method is called every time a server became available.
     *
     * @param event The event.
     */
    void onServerBecomesUnavailable(ServerBecomesAvailableEvent event);
}

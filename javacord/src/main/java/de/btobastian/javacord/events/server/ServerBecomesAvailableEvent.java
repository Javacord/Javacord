package de.btobastian.javacord.events.server;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.Server;

/**
 * A server becomes available event.
 * Unavailability means, that a Discord server is down due to a temporary outage.
 *
 * @see <a href="https://discordapp.com/developers/docs/topics/gateway#guild-unavailability">Discord docs</a>
 */
public class ServerBecomesAvailableEvent extends ServerEvent {

    /**
     * Creates a new server becomes available event.
     *
     * @param api The api instance of the event.
     * @param server The server of the event.
     */
    public ServerBecomesAvailableEvent(DiscordApi api, Server server) {
        super(api, server);
    }

}

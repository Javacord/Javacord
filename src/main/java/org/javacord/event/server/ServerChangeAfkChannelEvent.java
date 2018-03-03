package org.javacord.event.server;

import org.javacord.DiscordApi;
import org.javacord.entity.channel.ServerVoiceChannel;
import org.javacord.entity.server.Server;

import java.util.Optional;

/**
 * A server change afk channel event.
 */
public class ServerChangeAfkChannelEvent extends ServerEvent {

    /**
     * The new afk channel of the server.
     */
    private final ServerVoiceChannel newAfkChannel;

    /**
     * The old afk channel of the server.
     */
    private final ServerVoiceChannel oldAfkChannel;

    /**
     * Creates a new server change afk channel event.
     *
     * @param api The api instance of the event.
     * @param server The server of the event.
     * @param newAfkChannel The new afk channel of the server.
     * @param oldAfkChannel The old afk channel of the server.
     */
    public ServerChangeAfkChannelEvent(
            DiscordApi api, Server server, ServerVoiceChannel newAfkChannel, ServerVoiceChannel oldAfkChannel) {
        super(api, server);
        this.newAfkChannel = newAfkChannel;
        this.oldAfkChannel = oldAfkChannel;
    }

    /**
     * Gets the old afk channel of the server.
     *
     * @return The old afk channel of the server.
     */
    public Optional<ServerVoiceChannel> getOldAfkChannel() {
        return Optional.ofNullable(oldAfkChannel);
    }

    /**
     * Gets the new afk channel of the server.
     *
     * @return The new afk channel of the server.
     */
    public Optional<ServerVoiceChannel> getNewAfkChannel() {
        return Optional.ofNullable(newAfkChannel);
    }

}

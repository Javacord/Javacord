package org.javacord.core.event.server;

import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.server.ServerChangeAfkChannelEvent;

import java.util.Optional;

/**
 * The implementation of {@link ServerChangeAfkChannelEvent}.
 */
public class ServerChangeAfkChannelEventImpl extends ServerEventImpl implements ServerChangeAfkChannelEvent {

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
     * @param server The server of the event.
     * @param newAfkChannel The new afk channel of the server.
     * @param oldAfkChannel The old afk channel of the server.
     */
    public ServerChangeAfkChannelEventImpl(
            Server server, ServerVoiceChannel newAfkChannel, ServerVoiceChannel oldAfkChannel) {
        super(server);
        this.newAfkChannel = newAfkChannel;
        this.oldAfkChannel = oldAfkChannel;
    }

    @Override
    public Optional<ServerVoiceChannel> getOldAfkChannel() {
        return Optional.ofNullable(oldAfkChannel);
    }

    @Override
    public Optional<ServerVoiceChannel> getNewAfkChannel() {
        return Optional.ofNullable(newAfkChannel);
    }

}

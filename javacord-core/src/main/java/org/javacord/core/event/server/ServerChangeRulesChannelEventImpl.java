package org.javacord.core.event.server;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.event.server.ServerChangeRulesChannelEvent;
import org.javacord.core.entity.server.ServerImpl;

import java.util.Optional;

/**
 * The implementation of {@link ServerChangeRulesChannelEvent}.
 */
public class ServerChangeRulesChannelEventImpl extends ServerEventImpl implements ServerChangeRulesChannelEvent {
    /**
     * The old rules channel of the server.
     */
    private final ServerTextChannel oldRulesChannel;
    /**
     * The new rules channel of the server.
     */
    private final ServerTextChannel newRulesChannel;

    /**
     * Creates a new rules channel change event.
     *
     * @param server          The server of the event.
     * @param newRulesChannel The new rules channel of the server.
     * @param oldRulesChannel The old rules channel of the server.
     */
    public ServerChangeRulesChannelEventImpl(ServerImpl server,
                                             ServerTextChannel newRulesChannel, ServerTextChannel oldRulesChannel) {
        super(server);
        this.newRulesChannel = newRulesChannel;
        this.oldRulesChannel = oldRulesChannel;
    }

    @Override
    public Optional<ServerTextChannel> getOldRulesChannel() {
        return Optional.ofNullable(oldRulesChannel);
    }

    @Override
    public Optional<ServerTextChannel> getNewRulesChannel() {
        return Optional.ofNullable(newRulesChannel);
    }
}

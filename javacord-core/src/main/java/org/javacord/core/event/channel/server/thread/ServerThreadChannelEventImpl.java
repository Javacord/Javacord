package org.javacord.core.event.channel.server.thread;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerThreadChannel;
import org.javacord.api.event.channel.server.thread.ServerThreadChannelEvent;
import org.javacord.core.event.channel.server.ServerChannelEventImpl;

/**
 * The implementation of {@link ServerThreadChannelEvent}.
 */
public abstract class ServerThreadChannelEventImpl extends ServerChannelEventImpl implements ServerThreadChannelEvent {
    /**
     * Creates a new server text channel event.
     *
     * @param channel The channel of the event.
     */
    public ServerThreadChannelEventImpl(ServerThreadChannel channel) {
        super(channel);
    }

    @Override
    public ServerThreadChannel getChannel() {
        return super.getChannel().asServerThreadChannel()
                .orElseThrow(() ->
                        new IllegalStateException("Channel is not a server thread channel."));
    }

    @Override
    public DiscordApi getApi() {
        return getChannel().getApi();
    }
}

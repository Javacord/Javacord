package org.javacord.core.event.channel.server.forum;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerForumChannel;
import org.javacord.api.event.channel.server.forum.ServerForumChannelEvent;
import org.javacord.core.event.channel.server.ServerChannelEventImpl;

/**
 * The implementation of {@link ServerForumChannelEvent}.
 */
public class ServerForumChannelEventImpl extends ServerChannelEventImpl implements ServerForumChannelEvent {
    /**
     * Creates a new server forum channel event.
     *
     * @param channel The channel of the event.
     */
    public ServerForumChannelEventImpl(ServerForumChannel channel) {
        super(channel);
    }

    @Override
    public ServerForumChannel getChannel() {
        return super.getChannel().asServerForumChannel()
                .orElseThrow(() ->
                        new IllegalStateException("Channel is not a server forum channel."));
    }

    @Override
    public DiscordApi getApi() {
        return getChannel().getApi();
    }
}

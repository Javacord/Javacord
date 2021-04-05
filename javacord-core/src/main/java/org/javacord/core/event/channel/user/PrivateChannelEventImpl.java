package org.javacord.core.event.channel.user;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.PrivateChannel;
import org.javacord.api.event.channel.user.PrivateChannelEvent;

/**
 * The implementation of {@link PrivateChannelEvent}.
 */
public abstract class PrivateChannelEventImpl implements PrivateChannelEvent {

    /**
     * The channel of the event.
     */
    private final PrivateChannel channel;

    /**
     * Creates a new private channel event.
     *
     * @param channel The channel of the event.
     */
    public PrivateChannelEventImpl(PrivateChannel channel) {
        this.channel = channel;
    }

    @Override
    public PrivateChannel getChannel() {
        return channel;
    }

    @Override
    public DiscordApi getApi() {
        return getChannel().getApi();
    }

}

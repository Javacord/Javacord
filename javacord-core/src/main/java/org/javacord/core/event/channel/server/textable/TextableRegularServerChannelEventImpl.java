package org.javacord.core.event.channel.server.textable;

import org.javacord.api.entity.channel.TextableRegularServerChannel;
import org.javacord.api.event.channel.server.textable.TextableRegularServerChannelEvent;
import org.javacord.core.event.channel.server.ServerChannelEventImpl;

public class TextableRegularServerChannelEventImpl extends ServerChannelEventImpl
        implements TextableRegularServerChannelEvent {

    /**
     * The channel of the event.
     */
    private final TextableRegularServerChannel channel;

    /**
     * Creates a new textable regular server channel event.
     *
     * @param channel The channel of the event.
     */
    public TextableRegularServerChannelEventImpl(TextableRegularServerChannel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public TextableRegularServerChannel getChannel() {
        return channel;
    }
}

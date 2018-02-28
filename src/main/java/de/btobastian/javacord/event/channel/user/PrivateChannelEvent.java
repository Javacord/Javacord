package de.btobastian.javacord.event.channel.user;

import de.btobastian.javacord.entity.channel.PrivateChannel;
import de.btobastian.javacord.event.user.UserEvent;

/**
 * A private channel event.
 */
public abstract class PrivateChannelEvent extends UserEvent {

    /**
     * The channel of the event.
     */
    private final PrivateChannel channel;

    /**
     * Creates a new private channel event.
     *
     * @param channel The channel of the event.
     */
    public PrivateChannelEvent(PrivateChannel channel) {
        super(channel.getApi(), channel.getRecipient());
        this.channel = channel;
    }

    /**
     * Gets the channel of the event.
     *
     * @return The channel of the event.
     */
    public PrivateChannel getChannel() {
        return channel;
    }

}

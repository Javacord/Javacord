package org.javacord.event.channel.group;

import org.javacord.entity.channel.GroupChannel;
import org.javacord.event.Event;

/**
 * A group channel event.
 */
public abstract class GroupChannelEvent extends Event {

    /**
     * The channel of the event.
     */
    private final GroupChannel channel;

    /**
     * Creates a new group channel event.
     *
     * @param channel The channel of the event.
     */
    public GroupChannelEvent(GroupChannel channel) {
        super(channel.getApi());
        this.channel = channel;
    }

    /**
     * Gets the channel of the event.
     *
     * @return The channel of the event.
     */
    public GroupChannel getChannel() {
        return channel;
    }

}

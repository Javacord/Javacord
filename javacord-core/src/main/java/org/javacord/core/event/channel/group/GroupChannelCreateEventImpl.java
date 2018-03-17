package org.javacord.core.event.channel.group;

import org.javacord.api.entity.channel.GroupChannel;
import org.javacord.api.event.channel.group.GroupChannelCreateEvent;

/**
 * The implementation of {@link GroupChannelCreateEvent}.
 */
public class GroupChannelCreateEventImpl extends GroupChannelEventImpl implements GroupChannelCreateEvent {

    /**
     * Creates a new group channel create event.
     *
     * @param channel The channel of the event.
     */
    public GroupChannelCreateEventImpl(GroupChannel channel) {
        super(channel);
    }

}

package org.javacord.core.event.channel.group;

import org.javacord.api.entity.channel.GroupChannel;
import org.javacord.api.event.channel.group.GroupChannelDeleteEvent;

/**
 * The implementation of {@link GroupChannelDeleteEvent}.
 */
public class GroupChannelDeleteEventImpl extends GroupChannelEventImpl implements GroupChannelDeleteEvent {

    /**
     * Creates a new group channel delete event.
     *
     * @param channel The channel of the event.
     */
    public GroupChannelDeleteEventImpl(GroupChannel channel) {
        super(channel);
    }

}

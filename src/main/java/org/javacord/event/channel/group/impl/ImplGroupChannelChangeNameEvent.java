package org.javacord.event.channel.group.impl;

import org.javacord.entity.channel.GroupChannel;
import org.javacord.event.channel.group.GroupChannelChangeNameEvent;

/**
 * The implementation of {@link GroupChannelChangeNameEvent}.
 */
public class ImplGroupChannelChangeNameEvent extends ImplGroupChannelEvent implements GroupChannelChangeNameEvent {

    /**
     * The new name of the channel.
     */
    private final String newName;

    /**
     * The old name of the channel.
     */
    private final String oldName;

    /**
     * Creates a new group channel change name event.
     *
     * @param channel The channel of the event.
     * @param newName The new name of the channel.
     * @param oldName The old name of the channel.
     */
    public ImplGroupChannelChangeNameEvent(GroupChannel channel, String newName, String oldName) {
        super(channel);
        this.newName = newName;
        this.oldName = oldName;
    }

    @Override
    public String getNewName() {
        return newName;
    }

    @Override
    public String getOldName() {
        return oldName;
    }
}

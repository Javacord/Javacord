package org.javacord.event.channel.server.impl;

import org.javacord.entity.channel.ServerChannel;
import org.javacord.event.channel.server.ServerChannelChangeNameEvent;

/**
 * The implementation of {@link ServerChannelChangeNameEvent}.
 */
public class ImplServerChannelChangeNameEvent extends ImplServerChannelEvent implements ServerChannelChangeNameEvent {

    /**
     * The new name of the channel.
     */
    private final String newName;

    /**
     * The old name of the channel.
     */
    private final String oldName;

    /**
     * Creates a new server channel change name event.
     *
     * @param channel The channel of the event.
     * @param newName The new name of the channel.
     * @param oldName The old name of the channel.
     */
    public ImplServerChannelChangeNameEvent(ServerChannel channel, String newName, String oldName) {
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

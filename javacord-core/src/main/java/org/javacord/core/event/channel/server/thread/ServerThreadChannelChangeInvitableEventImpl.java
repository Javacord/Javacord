package org.javacord.core.event.channel.server.thread;

import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeInvitableEvent;
import org.javacord.core.entity.channel.ServerThreadChannelImpl;

/**
 * The implementation of {@link ServerThreadChannelChangeInvitableEvent}.
 */
public class ServerThreadChannelChangeInvitableEventImpl extends ServerThreadChannelEventImpl
        implements ServerThreadChannelChangeInvitableEvent {
    /**
     * The new invitable state.
     */
    private final boolean newInvitable;

    /**
     * The old invitable state.
     */
    private final boolean oldInvitable;

    /**
     * Creates a new instance of this class.
     *
     * @param channel The channel.
     * @param newInvitable The new invitable state.
     * @param oldInvitable The old invitable state.
     */
    public ServerThreadChannelChangeInvitableEventImpl(ServerThreadChannelImpl channel, boolean newInvitable,
                                                       boolean oldInvitable) {
        super(channel);
        this.newInvitable = newInvitable;
        this.oldInvitable = oldInvitable;
    }


    @Override
    public boolean wasInvitable() {
        return oldInvitable;
    }

    @Override
    public boolean isInvitable() {
        return newInvitable;
    }
}

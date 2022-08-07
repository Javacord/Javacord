package org.javacord.core.event.channel.server.thread;

import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeLockedEvent;
import org.javacord.core.entity.channel.ServerThreadChannelImpl;

/**
 * The implementation of {@link ServerThreadChannelChangeLockedEvent}.
 */
public class ServerThreadChannelChangeLockedEventImpl extends ServerThreadChannelEventImpl
        implements ServerThreadChannelChangeLockedEvent {
    /**
     * The new locked state.
     */
    private final boolean isLocked;

    /**
     * The old locked state.
     */
    private final boolean wasLocked;

    /**
     * Creates a new instance of this class.
     *
     * @param channel The channel.
     * @param isLocked The new locked state.
     * @param wasLocked The old locked state.
     */
    public ServerThreadChannelChangeLockedEventImpl(ServerThreadChannelImpl channel, boolean isLocked,
                                                    boolean wasLocked) {
        super(channel);
        this.isLocked = isLocked;
        this.wasLocked = wasLocked;
    }

    @Override
    public boolean isLocked() {
        return isLocked;
    }

    @Override
    public boolean wasLocked() {
        return wasLocked;
    }
}

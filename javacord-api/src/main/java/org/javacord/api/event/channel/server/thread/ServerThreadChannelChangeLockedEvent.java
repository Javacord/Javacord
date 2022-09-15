package org.javacord.api.event.channel.server.thread;

public interface ServerThreadChannelChangeLockedEvent extends ServerThreadChannelEvent {
    /**
     * Gets the new locked state.
     *
     * @return The new locked state.
     */
    boolean isLocked();

    /**
     * Gets the old locked state.
     *
     * @return The old locked state.
     */
    boolean wasLocked();
}

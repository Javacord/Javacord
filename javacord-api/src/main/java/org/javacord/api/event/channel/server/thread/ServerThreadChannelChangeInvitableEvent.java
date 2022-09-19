package org.javacord.api.event.channel.server.thread;

public interface ServerThreadChannelChangeInvitableEvent extends ServerThreadChannelEvent {

    /**
     * Gets the old invitable state of the thread.
     *
     * @return The old invitable state of the thread.
     */
    boolean wasInvitable();

    /**
     * Gets the new invitable state of the thread.
     *
     * @return The new invitable state of the thread.
     */
    boolean isInvitable();
}

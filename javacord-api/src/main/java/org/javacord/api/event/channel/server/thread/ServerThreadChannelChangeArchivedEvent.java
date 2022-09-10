package org.javacord.api.event.channel.server.thread;

public interface ServerThreadChannelChangeArchivedEvent extends ServerThreadChannelEvent {

    /**
     * Gets whether the thread is archived.
     *
     * @return Whether the thread is archived.
     */
    boolean isArchived();

    /**
     * Gets whether the thread was archived.
     *
     * @return Whether the thread was archived.
     */
    boolean wasArchived();
}

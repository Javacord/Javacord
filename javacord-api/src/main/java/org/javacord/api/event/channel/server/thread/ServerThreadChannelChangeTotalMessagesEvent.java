package org.javacord.api.event.channel.server.thread;

public interface ServerThreadChannelChangeTotalMessagesEvent extends ServerThreadChannelEvent {

    /**
     * Gets the new total message count.
     *
     * @return The new total message count.
     */
    int getNewTotalMessageCount();

    /**
     * Gets the old total message count.
     *
     * @return The old total message count.
     */
    int getOldTotalMessageCount();
}

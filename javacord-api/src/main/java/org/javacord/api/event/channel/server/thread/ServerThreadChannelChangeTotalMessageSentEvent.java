package org.javacord.api.event.channel.server.thread;

public interface ServerThreadChannelChangeTotalMessageSentEvent extends ServerThreadChannelEvent {

    /**
     * Gets the new total amount of messages sent.
     *
     * @return The new total amount of messages sent.
     */
    int getNewTotalMessageSent();

    /**
     * Gets the old total amount of messages sent.
     *
     * @return The old total amount of messages sent.
     */
    int getOldTotalMessageSent();
}

package org.javacord.api.event.channel.server.thread;

public interface ServerThreadChannelChangeLastMessageIdEvent extends ServerThreadChannelEvent {
    /**
     * Gets the old last message id of the thread.
     *
     * @return The old last message id of the thread.
     */
    long getOldLastMessageId();

    /**
     * Gets the new last message id of the thread.
     *
     * @return The new last message id of the thread.
     */
    long getNewLastMessageId();
}

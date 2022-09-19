package org.javacord.api.event.channel.server.thread;

public interface ServerThreadChannelChangeMessageCountEvent extends ServerThreadChannelEvent {

    /**
     * Gets the new count of messages in this thread.
     *
     * @return The new count of messages in this thread.
     */
    int getNewMessageCount();

    /**
     * Gets the old count of messages in this thread.
     *
     * @return The old count of messages in this thread.
     */
    int getOldMessageCount();
}


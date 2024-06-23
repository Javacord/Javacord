package org.javacord.api.event.channel.server.forum;

public interface ServerForumChannelChangeLastMessageIdEvent extends ServerForumChannelEvent {
    /**
     * Gets the new last message id of the channel.
     *
     * @return The new last message id of the channel.
     */
    Long getNewLastMessageId();

    /**
     * Gets the old last message id of the channel.
     *
     * @return The old last message id of the channel.
     */
    Long getOldLastMessageId();
}

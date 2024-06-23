package org.javacord.api.event.channel.server.forum;

import org.javacord.api.entity.channel.forum.DefaultReaction;

/**
 * An event signaling that a server forum channel's default reaction has changed.
 */
public interface ServerForumChannelChangeDefaultReactionEvent extends ServerForumChannelEvent {
    /**
     * Gets the old default reaction.
     *
     * @return The old default reaction.
     */
    DefaultReaction getOldDefaultReaction();

    /**
     * Gets the new default reaction.
     *
     * @return The new default reaction.
     */
    DefaultReaction getNewDefaultReaction();
}

package org.javacord.core.event.channel.server.forum;

import org.javacord.api.entity.channel.ServerForumChannel;
import org.javacord.api.entity.channel.forum.DefaultReaction;
import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeDefaultReactionEvent;

/**
 * The implementation of {@link ServerForumChannelChangeDefaultReactionEvent}.
 */
public class ServerForumChannelChangeDefaultReactionEventImpl extends ServerForumChannelEventImpl
        implements ServerForumChannelChangeDefaultReactionEvent {

    /**
     * The old default reaction of the channel.
     */
    private final DefaultReaction oldDefaultReaction;

    /**
     * The new default reaction of the channel.
     */
    private final DefaultReaction newDefaultReaction;

    /**
     * Creates a new server forum channel change default reaction event.
     *
     * @param channel The channel of the event.
     * @param oldDefaultReaction The old default reaction of the channel.
     * @param newDefaultReaction The new default reaction of the channel.
     */
    public ServerForumChannelChangeDefaultReactionEventImpl(ServerForumChannel channel,
                                                            DefaultReaction oldDefaultReaction,
                                                            DefaultReaction newDefaultReaction) {
        super(channel);
        this.oldDefaultReaction = oldDefaultReaction;
        this.newDefaultReaction = newDefaultReaction;
    }

    @Override
    public DefaultReaction getOldDefaultReaction() {
        return oldDefaultReaction;
    }

    @Override
    public DefaultReaction getNewDefaultReaction() {
        return newDefaultReaction;
    }
}

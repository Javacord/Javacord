package org.javacord.core.event.channel.server.forum;

import org.javacord.api.entity.channel.ServerForumChannel;
import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeAppliedTagsEvent;

import java.util.List;

/**
 * The implementation of {@link ServerForumChannelChangeAppliedTagsEvent}.
 */
public class ServerForumChannelChangeAppliedTagsEventImpl extends ServerForumChannelEventImpl
        implements ServerForumChannelChangeAppliedTagsEvent {

    /**
     * The new applied tags.
     */
    private final List<Long> newAppliedTags;

    /**
     * The old applied tags.
     */
    private final List<Long> oldAppliedTags;

    /**
     * Creates a new server forum channel change tags event.
     *
     * @param channel        The channel of the event.
     * @param oldAppliedTags The old applied tags.
     * @param newAppliedTags The new applied tags.
     */
    public ServerForumChannelChangeAppliedTagsEventImpl(ServerForumChannel channel, List<Long> oldAppliedTags,
                                                        List<Long> newAppliedTags) {
        super(channel);
        this.newAppliedTags = newAppliedTags;
        this.oldAppliedTags = oldAppliedTags;
    }

    @Override
    public List<Long> getNewAppliedTags() {
        return newAppliedTags;
    }

    @Override
    public List<Long> getOldAppliedTags() {
        return oldAppliedTags;
    }
}

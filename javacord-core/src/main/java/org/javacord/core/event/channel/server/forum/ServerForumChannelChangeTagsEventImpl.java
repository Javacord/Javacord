package org.javacord.core.event.channel.server.forum;

import org.javacord.api.entity.channel.ServerForumChannel;
import org.javacord.api.entity.channel.forum.ForumTag;
import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeTagsEvent;

import java.util.List;

/**
 * The implementation of {@link ServerForumChannelChangeTagsEvent}.
 */
public class ServerForumChannelChangeTagsEventImpl extends ServerForumChannelEventImpl
        implements ServerForumChannelChangeTagsEvent {

    /**
     * The old tags.
     */
    private final List<ForumTag> oldTags;

    /**
     * The new tags.
     */
    private final List<ForumTag> newTags;

    /**
     * Creates a new server forum channel change tags event.
     *
     * @param channel The channel of the event.
     * @param oldTags The old tags.
     * @param newTags The new tags.
     */
    public ServerForumChannelChangeTagsEventImpl(ServerForumChannel channel, List<ForumTag> oldTags,
                                                 List<ForumTag> newTags) {
        super(channel);
        this.oldTags = oldTags;
        this.newTags = newTags;
    }

    @Override
    public List<ForumTag> getOldTags() {
        return oldTags;
    }

    @Override
    public List<ForumTag> getNewTags() {
        return newTags;
    }
}

package org.javacord.core.event.channel.server.forum;

import org.javacord.api.entity.channel.ServerForumChannel;
import org.javacord.api.entity.channel.forum.ForumTag;
import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeForumTagsEvent;

import java.util.Set;

/**
 * The implementation of {@link ServerForumChannelChangeForumTagsEvent}.
 */
public class ServerForumChannelChangeForumTagsEventImpl extends ServerForumChannelEventImpl
        implements ServerForumChannelChangeForumTagsEvent {

    /**
     * The old forum tags.
     */
    private final Set<ForumTag> oldForumTags;

    /**
     * The new forum tags.
     */
    private final Set<ForumTag> newForumTags;

    /**
     * Creates a new server forum channel change forum tags event.
     *
     * @param channel The channel of the event.
     * @param oldForumTags The old forum tags.
     * @param newForumTags The new forum tags.
     */
    public ServerForumChannelChangeForumTagsEventImpl(ServerForumChannel channel,
                                                      Set<ForumTag> oldForumTags,
                                                      Set<ForumTag> newForumTags) {
        super(channel);
        this.oldForumTags = oldForumTags;
        this.newForumTags = newForumTags;
    }

    @Override
    public Set<ForumTag> getOldForumTags() {
        return oldForumTags;
    }

    @Override
    public Set<ForumTag> getNewForumTags() {
        return newForumTags;
    }
}

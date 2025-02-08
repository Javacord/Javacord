package org.javacord.core.event.channel.server.thread;

import org.javacord.api.entity.channel.ServerThreadChannel;
import org.javacord.api.entity.channel.forum.ForumTag;
import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeForumTagsEvent;

import java.util.Set;

/**
 * The implementation of {@link ServerThreadChannelChangeForumTagsEvent}.
 */
public class ServerThreadChannelChangeForumTagsEventImpl extends ServerThreadChannelEventImpl
        implements ServerThreadChannelChangeForumTagsEvent {

    /**
     * The old forum tags of the channel.
     */
    private final Set<ForumTag> oldForumTags;

    /**
     * The new forum tags of the channel.
     */
    private final Set<ForumTag> newForumTags;

    /**
     * Creates a new server thread channel change forum tags event.
     *
     * @param channel The channel of the event.
     * @param oldForumTags The old forum tags of the channel.
     * @param newForumTags The new forum tags of the channel.
     */
    public ServerThreadChannelChangeForumTagsEventImpl(ServerThreadChannel channel,
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

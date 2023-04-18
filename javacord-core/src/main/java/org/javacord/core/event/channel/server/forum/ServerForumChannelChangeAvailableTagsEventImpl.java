package org.javacord.core.event.channel.server.forum;

import org.javacord.api.entity.channel.ServerForumChannel;
import org.javacord.api.entity.channel.forum.AvailableTag;
import org.javacord.api.event.channel.server.forum.ServerForumChannelChangeAvailableTagsEvent;

import java.util.List;

/**
 * The implementation of {@link ServerForumChannelChangeAvailableTagsEvent}.
 */
public class ServerForumChannelChangeAvailableTagsEventImpl extends ServerForumChannelEventImpl
        implements ServerForumChannelChangeAvailableTagsEvent {

    /**
     * The old tags.
     */
    private final List<AvailableTag> oldTags;

    /**
     * The new tags.
     */
    private final List<AvailableTag> newTags;

    /**
     * Creates a new server forum channel change tags event.
     *
     * @param channel The channel of the event.
     * @param oldTags The old tags.
     * @param newTags The new tags.
     */
    public ServerForumChannelChangeAvailableTagsEventImpl(ServerForumChannel channel, List<AvailableTag> oldTags,
                                                          List<AvailableTag> newTags) {
        super(channel);
        this.oldTags = oldTags;
        this.newTags = newTags;
    }

    @Override
    public List<AvailableTag> getOldTags() {
        return oldTags;
    }

    @Override
    public List<AvailableTag> getNewTags() {
        return newTags;
    }
}

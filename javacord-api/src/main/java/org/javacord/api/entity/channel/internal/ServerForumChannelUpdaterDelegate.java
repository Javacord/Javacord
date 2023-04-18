package org.javacord.api.entity.channel.internal;

import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.ChannelFlag;
import org.javacord.api.entity.channel.ServerForumChannelUpdater;
import org.javacord.api.entity.channel.forum.ForumLayoutType;
import org.javacord.api.entity.channel.forum.ForumTag;
import org.javacord.api.entity.channel.forum.SortOrderType;

import java.util.EnumSet;
import java.util.Set;

/**
 * This class is internally used by the {@link ServerForumChannelUpdater} to update server forum channels.
 * You usually don't want to interact with this object.
 */
public interface ServerForumChannelUpdaterDelegate extends RegularServerChannelUpdaterDelegate {

    /**
     * Queues the name to be updated.
     *
     * @param name The new name of the channel.
     */
    void setName(String name);

    /**
     * Queues the topic to be updated.
     *
     * @param topic The new topic of the channel.
     */
    void setTopic(String topic);

    /**
     * Queues the rate limit per user to be updated.
     *
     * @param rateLimitPerUser The new rate limit per user of the channel.
     */
    void setRateLimitPerUser(int rateLimitPerUser);

    /**
     * Queues the position to be updated.
     *
     * @param position The new position of the channel.
     */
    void setPosition(int position);

    /**
     * Queues the nsfw flag to be updated.
     *
     * @param nsfw The new nsfw flag of the channel.
     */
    void setNsfw(boolean nsfw);

    /**
     * Queues the forum channel's flags to be updated.
     *
     * @param flags The new flags of the forum channel.
     */
    void setFlags(EnumSet<ChannelFlag> flags);

    /**
     * Queues the forum channel's forum tags to be updated.
     *
     * @param tags The new tags of the forum channel.
     */
    void setForumTags(Set<ForumTag> tags);

    /**
     * Queues the template to be updated.
     *
     * @param template The new template of the forum channel.
     */
    void setTemplate(String template);

    /**
     * Queues the default sort type to be updated.
     *
     * @param defaultType The new default type of the forum channel.
     */
    void setDefaultSortType(SortOrderType defaultType);

    /**
     * Queues the default reaction to be updated.
     *
     * @param defaultReactionId The new default reaction id of the forum channel.
     */
    void setDefaultReaction(long defaultReactionId);

    /**
     * Queues the default reaction to be updated.
     *
     * @param defaultReactionId The new default reaction id of the forum channel.
     */
    default void setDefaultReaction(String defaultReactionId) {
        setDefaultReaction(Long.parseUnsignedLong(defaultReactionId));
    }

    /**
     * Queues the layout type to be updated.
     *
     * @param layoutType The new layout type of the forum channel.
     */
    void setForumLayoutType(ForumLayoutType layoutType);

    /**
     * Queues the category to be updated.
     *
     * @param category The new category of the channel.
     */
    void setCategory(ChannelCategory category);

    /**
     * Queues the category to be removed.
     */
    void removeCategory();
}

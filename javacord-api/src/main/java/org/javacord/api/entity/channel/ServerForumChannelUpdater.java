package org.javacord.api.entity.channel;

import org.javacord.api.entity.channel.forum.ForumLayoutType;
import org.javacord.api.entity.channel.forum.ForumTag;
import org.javacord.api.entity.channel.forum.SortOrderType;
import org.javacord.api.entity.channel.internal.ServerForumChannelUpdaterDelegate;
import org.javacord.api.util.internal.DelegateFactory;

import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update server forum channels.
 */
public class ServerForumChannelUpdater extends RegularServerChannelUpdater<ServerForumChannelUpdater> {

    /**
     * The server forum channel delegate used by this instance.
     */
    private final ServerForumChannelUpdaterDelegate delegate;

    /**
     * Creates a new server forum channel updater.
     *
     * @param channel The channel to update.
     */
    public ServerForumChannelUpdater(ServerForumChannel channel) {
        super(DelegateFactory.createServerForumChannelUpdaterDelegate(channel));
        delegate = (ServerForumChannelUpdaterDelegate) super.regularServerChannelUpdaterDelegate;
    }

    /**
     * Queues the category to be updated.
     *
     * @param category The new category of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerForumChannelUpdater setCategory(ChannelCategory category) {
        delegate.setCategory(category);
        return this;
    }

    /**
     * Queues the topic to be updated.
     *
     * @param topic The new topic of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerForumChannelUpdater setTopic(String topic) {
        delegate.setTopic(topic);
        return this;
    }

    /**
     * Queues the rate limit per user to be updated.
     *
     * @param rateLimitPerUser The new rate limit per user of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerForumChannelUpdater setRateLimitPerUser(int rateLimitPerUser) {
        delegate.setRateLimitPerUser(rateLimitPerUser);
        return this;
    }

    /**
     * Queues the position to be updated.
     *
     * @param position The new position of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerForumChannelUpdater setPosition(int position) {
        delegate.setPosition(position);
        return this;
    }

    /**
     * Queues the nsfw flag to be updated.
     *
     * @param nsfw The new nsfw flag of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerForumChannelUpdater setNsfw(boolean nsfw) {
        delegate.setNsfw(nsfw);
        return this;
    }

    /**
     * Queues the flags to be updated.
     *
     * @param flags The new flags of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerForumChannelUpdater setFlags(EnumSet<ChannelFlag> flags) {
        delegate.setFlags(flags);
        return this;
    }

    /**
     * Queues the template to be updated.
     *
     * @param template The new template of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerForumChannelUpdater setTemplate(String template) {
        delegate.setTemplate(template);
        return this;
    }

    /**
     * Queues the sort order to be updated.
     *
     * @param sortOrder The new sort order of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerForumChannelUpdater setSortOrder(SortOrderType sortOrder) {
        delegate.setDefaultSortType(sortOrder);
        return this;
    }

    /**
     * Queues the default reaction to be updated.
     *
     * @param defaultReaction The new default reaction of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerForumChannelUpdater setDefaultReaction(long defaultReaction) {
        delegate.setDefaultReaction(defaultReaction);
        return this;
    }

    /**
     * Queues the forum tags to be updated.
     *
     * @param tags The new tags of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerForumChannelUpdater setForumTags(Set<ForumTag> tags) {
        delegate.setForumTags(tags);
        return this;
    }

    /**
     * Queues the forum layout type to be updated.
     *
     * @param layoutType The new forum layout type of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerForumChannelUpdater setForumLayoutType(ForumLayoutType layoutType) {
        delegate.setForumLayoutType(layoutType);
        return this;
    }

    /**
     * Queues the category to be removed.
     *
     * @return The current instance in order to chain call methods.
     */
    public ServerForumChannelUpdater removeCategory() {
        delegate.removeCategory();
        return this;
    }

    @Override
    public CompletableFuture<Void> update() {
        return delegate.update();
    }

}

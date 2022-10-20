package org.javacord.api.entity.channel;

import org.javacord.api.entity.channel.forum.PermissionOverwrite;
import org.javacord.api.entity.channel.internal.ServerForumChannelUpdaterDelegate;
import org.javacord.api.util.internal.DelegateFactory;

import java.util.List;
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
     * Queues the permission overwrites to be updated.
     *
     * @param permissionOverwrites The new permission overwrites of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerForumChannelUpdater setPermissionOverwrites(List<PermissionOverwrite> permissionOverwrites) {
        delegate.setPermissionOverwrites(permissionOverwrites);
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

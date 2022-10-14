package org.javacord.api.entity.channel;

import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.channel.forum.DefaultReaction;
import org.javacord.api.entity.channel.forum.ForumTag;
import org.javacord.api.entity.channel.forum.PermissionOverwrite;
import org.javacord.api.entity.channel.forum.SortOrderType;
import org.javacord.api.entity.channel.thread.ThreadMetadata;
import org.javacord.api.listener.channel.server.forum.ServerForumChannelAttachableListenerManager;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * This class represents a server forum channel.
 */
public interface ServerForumChannel extends RegularServerChannel, Mentionable, Categorizable,
        ServerForumChannelAttachableListenerManager {

    /**
     * Gets the topic of the forum channel.
     *
     * @return The topic of the forum channel.
     */
    String getTopic();

    /**
     * Gets the amount of seconds a user has to wait before sending another message (0-21600).
     *
     * @return The amount of seconds a user has to wait before sending another message.
     */
    int getRateLimitPerUser();

    /**
     * Gets the position of the forum channel.
     *
     * @return The position of the forum channel.
     */
    int getPosition();

    /**
     * Gets the permission overwrites of the forum channel.
     *
     * @return The permission overwrites of the forum channel.
     */
    List<PermissionOverwrite> getPermissionOverwrites();

    /**
     * Gets whether the forum channel is nsfw.
     *
     * @return Whether the forum channel is nsfw.
     */
    boolean isNsfw();

    /**
     * Gets the last message id of the forum channel.
     *
     * @return The last message id of the forum channel.
     */
    Optional<Long> getLastMessageId();

    /**
     * Gets the last message id of the forum channel as a string.
     *
     * @return The last message id of the forum channel as a string.
     */
    default Optional<String> getLastMessageIdAsString() {
        return getLastMessageId().map(Long::toUnsignedString);
    }

    /**
     * Gets the default rate limit per user of the forum channel.
     *
     * @return The default rate limit per user of the forum channel.
     */
    int getDefaultRateLimitPerUser();

    /**
     * Gets the forum channel's flags.
     *
     * @return The forum channel's flags.
     */
    EnumSet<ChannelFlag> getFlags();

    /**
     * Used to get the set of tags that can be used in the forum channel.
     *
     * @return The set of tags that can be used in the forum channel.
     */
    List<ForumTag> getAvailableTags();

    /**
     * Used to get the template for the forum channel.
     *
     * @return The template for the forum channel.
     */
    Optional<String> getTemplate();

    /**
     * Gets the default sort type used to order the posts in the forum channel.
     *
     * @return The default sort type used to order the posts in the forum channel.
     */
    Optional<SortOrderType> getDefaultSortType();

    /**
     * Gets the default emoji shown in the add reaction button.
     *
     * @return The default emoji shown in the add reaction button.
     */
    Optional<DefaultReaction> getDefaultReaction();

    /**
     * Creates an updater for this channel.
     *
     * @return An updater for this channel.
     */
    default ServerForumChannelUpdater createUpdater() {
        return new ServerForumChannelUpdater(this);
    }

    /**
     * {@inheritDoc}
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerForumChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param category The new category of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateCategory(ChannelCategory category) {
        return createUpdater().setCategory(category).update();
    }

    /**
     * {@inheritDoc}
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerForumChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> removeCategory() {
        return createUpdater().removeCategory().update();
    }

}

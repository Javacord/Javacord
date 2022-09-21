package org.javacord.api.entity.channel;

import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.channel.forum.DefaultReaction;
import org.javacord.api.entity.channel.forum.ForumTag;
import org.javacord.api.listener.channel.server.forum.ServerForumChannelAttachableListenerManager;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * This class represents a server forum channel.
 */
public interface ServerForumChannel extends RegularServerChannel, Mentionable, Categorizable,
        ServerForumChannelAttachableListenerManager {

    /**
     * Gets the channel's flags.
     *
     * @return The channel's flags.
     */
    EnumSet<ChannelFlag> getFlags();

    /**
     * Gets the set of tags that can be used.
     *
     * @return The set of tags that can be used.
     */
    List<ForumTag> getTags();

    /**
     * Gets the list of applied tags ids.
     *
     * @return The list of applied tags ids.
     */
    List<Long> getAppliedTags();

    /**
     * Gets the default emoji shown in the add reaction button.
     *
     * @return The default emoji shown in the add reaction button.
     */
    Optional<DefaultReaction> getDefaultReaction();

    /**
     * Gets the list of applied tags ids as strings.
     *
     * @return The list of applied tags ids as strings.
     */
    default List<String> getAppliedTagsAsString() {
        return getAppliedTags().stream().map(Long::toUnsignedString)
                .collect(Collectors.toList());
    }

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

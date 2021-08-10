package org.javacord.api.entity.channel;

import org.javacord.api.entity.user.User;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a channel thread.
 */
public interface ChannelThread extends ServerTextChannel {

    /**
     * The parent server text channel of this thread.
     *
     * @return The parent of this thread.
     */
    ServerTextChannel getParent();

    /**
     * Gets an approximate count of messages in this thread that stops counting at 50.
     *
     * @return The count of messages in this thread.
     */
    int getMessageCount();

    /**
     * Gets an approximate count of users in this thread that stops counting at 50.
     *
     * @return The count of users in this thread.
     */
    int getMemberCount();

    /**
     * Gets the default duration for newly created threads, in minutes, to automatically
     * archive the thread after recent activity, can be set to: 60, 1440, 4320, 10080.
     *
     * @return The default duration for newly created threads.
     */
    int getDefaultAutoArchiveDuration();

    /**
     * Gets the duration for newly created threads, in minutes, to automatically
     * archive the thread after recent activity, can be set to: 60, 1440, 4320, 10080.
     *
     * @return The duration for newly created threads.
     */
    int getAutoArchiveDuration();

    /**
     * Whether this thread is archived.
     *
     * @return Whether this thread is archived.
     */
    boolean isArchived();

    /**
     * Whether this thread is locked.
     * When a thread is locked, only users with MANAGE_THREADS can unarchive it.
     *
     * @return Whether this thread is locked.
     */
    boolean isLocked();

    /**
     * Whether this thread is private.
     * When a thread is private, it is only viewable by those invited and those with the MANAGE_THREADS permission.
     *
     * @return Whether this thread is private.
     */
    default boolean isPrivate() {
        return getType() == ChannelType.SERVER_PRIVATE_THREAD;
    }

    /**
     * Gets the id of the creator of the tread.
     *
     * @return The id of the owner.
     */
    long getOwnerId();

    /**
     * Gets the creator of the thread.
     *
     * @return The owner.
     */
    default Optional<User> getOwner() {
        return getApi().getCachedUserById(getOwnerId());
    }

    /**
     * Gets the timestamp when the thread's archive status was last changed, used for calculating recent activity.
     *
     * @return The timestamp when the thread's archive status was last changed.
     */
    Instant getArchiveTimestamp();

    @Override
    default Optional<ChannelCategory> getCategory() {
        return getParent().getCategory();
    }

    @Override
    default CompletableFuture<Void> updateCategory(ChannelCategory category) {
        return getParent().updateCategory(category);
    }

    @Override
    default CompletableFuture<Void> removeCategory() {
        return getParent().removeCategory();
    }
}

package org.javacord.api.entity.channel;

import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.user.User;
import org.javacord.api.listener.channel.ServerThreadChannelAttachableListenerManager;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a channel thread.
 */
public interface ServerThreadChannel extends ServerChannel, TextChannel, Mentionable,
        ServerThreadChannelAttachableListenerManager {

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
     * Whether this thread is private.
     * When a thread is private, it is only viewable by those invited and those with the MANAGE_THREADS permission.
     *
     * @return Whether this thread is private.
     */
    default boolean isPublic() {
        return getType() == ChannelType.SERVER_PUBLIC_THREAD;
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
     * <p>If the creator is in the cache, the creator is served from the cache.
     *
     * @return The creator of the thread.
     */
    default CompletableFuture<User> requestOwner() {
        return getApi().getUserById(getOwnerId());
    }

    /**
     * Gets the timestamp when the thread's archive status was last changed, used for calculating recent activity.
     *
     * @return The timestamp when the thread's archive status was last changed.
     */
    Instant getArchiveTimestamp();

    /**
     * List of the members of the thread.
     *
     * @return The members of the current thread.
     */
    List<ThreadMember> getMembers();

    @Override
    default Optional<ServerThreadChannel> getCurrentCachedInstance() {
        return getApi().getServerById(getServer().getId()).flatMap(server -> server.getThreadChannelById(getId()));
    }

    @Override
    default CompletableFuture<ServerThreadChannel> getLatestInstance() {
        Optional<ServerThreadChannel> currentCachedInstance = getCurrentCachedInstance();
        if (currentCachedInstance.isPresent()) {
            return CompletableFuture.completedFuture(currentCachedInstance.get());
        } else {
            CompletableFuture<ServerThreadChannel> result = new CompletableFuture<>();
            result.completeExceptionally(new NoSuchElementException());
            return result;
        }
    }

    /**
     * Adds a member to this thread.
     *
     * @param user The user which should be added.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> addThreadMember(User user) {
        return addThreadMember(user.getId());
    }

    /**
     * Adds a member to this thread.
     *
     * @param userId The user ID which should be added.
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> addThreadMember(long userId);

    /**
     * Removes a member to this thread.
     *
     * @param user The user which should be removed.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> removeThreadMember(User user) {
        return removeThreadMember(user.getId());
    }

    /**
     * Removes a member to this thread.
     *
     * @param userId The user ID which should be removed.
     * @return A future to check if the update was successful.
     */
    CompletableFuture<Void> removeThreadMember(long userId);

    /**
     * Joins this ServerThreadChannel.
     *
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> joinThread() {
        return getServer().joinServerThreadChannel(getId());
    }

    /**
     * Leaves this ServerThreadChannel.
     *
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> leaveThread() {
        return getServer().leaveServerThreadChannel(getId());
    }

    /**
     * Gets a list of all members in this thread.
     * Requires the {@link org.javacord.api.entity.intent.Intent#GUILD_MEMBERS} intent.
     *
     * @return a list of all members in this thread.
     */
    CompletableFuture<List<ThreadMember>> getThreadMembers();

    /**
     * Creates an updater for this thread.
     *
     * @return An updater for this thread.
     */
    @Override
    default ServerThreadChannelUpdater createUpdater() {
        return new ServerThreadChannelUpdater(this);
    }

}

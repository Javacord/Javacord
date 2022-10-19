package org.javacord.api.entity.channel;

import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.channel.thread.ThreadMetadata;
import org.javacord.api.entity.user.User;
import org.javacord.api.listener.channel.ServerThreadChannelAttachableListenerManager;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a channel thread.
 */
public interface ServerThreadChannel extends ServerChannel, TextChannel, Mentionable,
        ServerThreadChannelAttachableListenerManager {

    /**
     * The parent regular server channel of this thread.
     *
     * @return The parent of this thread.
     */
    RegularServerChannel getParent();

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
     * Gets the id of the last message sent in this thread.
     *
     * @return The id of the last message sent in this thread.
     */
    long getLastMessageId();

    /**
     * Gets the total number of messages ever sent in this thread.
     * Similar to {@link #getMessageCount()} but will not decrease when messages are deleted.
     *
     * @return The total number of messages sent in this thread.
     */
    int getTotalNumberOfMessagesSent();

    /**
     * Gets the amount of seconds a user has to wait before sending another message (0-21600).
     *
     * @return The amount of seconds a user has to wait before sending another message.
     */
    int getRateLimitPerUser();

    /**
     * Gets the extra data about this thread.
     *
     * @return The extra data about this thread.
     */
    ThreadMetadata getMetadata();

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
     * Gets all members of the thread.
     *
     * @return The members of the current thread.
     */
    Set<ThreadMember> getMembers();

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
     * Gets a thread member by their id.
     *
     * @param userId The id of the user.
     * @return The thread member.
     */
    CompletableFuture<ThreadMember> requestThreadMemberById(long userId);

    /**
     * Gets a thread member by their id.
     *
     * @param userId The id of the user.
     * @return The thread member.
     */
    default CompletableFuture<ThreadMember> requestThreadMemberById(String userId) {
        return requestThreadMemberById(Long.parseLong(userId));
    }

    /**
     * Gets all members in this thread.
     * Requires the {@link org.javacord.api.entity.intent.Intent#GUILD_MEMBERS} intent.
     *
     * @return All members in this thread.
     * @deprecated Use {@link #requestThreadMembers()} instead.
     */
    @Deprecated
    default CompletableFuture<Set<ThreadMember>> getThreadMembers() {
        return requestThreadMembers();
    }

    /**
     * Gets all members in this thread.
     * Requires the {@link org.javacord.api.entity.intent.Intent#GUILD_MEMBERS} intent.
     *
     * @return All members in this thread.
     */
    CompletableFuture<Set<ThreadMember>> requestThreadMembers();

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

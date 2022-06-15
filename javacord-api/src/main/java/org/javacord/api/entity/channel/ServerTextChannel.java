package org.javacord.api.entity.channel;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.server.ArchivedThreads;
import org.javacord.api.listener.channel.server.text.ServerTextChannelAttachableListenerManager;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a server text channel.
 */
public interface ServerTextChannel extends RegularServerChannel, TextableRegularServerChannel,
        ServerTextChannelAttachableListenerManager {

    @Override
    default String getMentionTag() {
        return "<#" + getIdAsString() + ">";
    }

    /**
     * Gets the default auto archive duration for threads that will be created in this channel.
     *
     * @return The default auto archive duration for this channel.
     */
    int getDefaultAutoArchiveDuration();

    /**
     * Gets the topic of the channel.
     *
     * @return The topic of the channel.
     */
    String getTopic();

    /**
     * Creates an updater for this channel.
     *
     * @return An updater for this channel.
     */
    default ServerTextChannelUpdater createUpdater() {
        return new ServerTextChannelUpdater(this);
    }

    /**
     * Updates the topic of the channel.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerTextChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param topic The new topic of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateTopic(String topic) {
        return createUpdater().setTopic(topic).update();
    }

    /**
     * Updates the nsfw flag of the channel.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerTextChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param nsfw The new nsfw flag of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateNsfwFlag(boolean nsfw) {
        return createUpdater().setNsfwFlag(nsfw).update();
    }

    /**
     * {@inheritDoc}
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerTextChannelUpdater} from {@link #createUpdater()} which provides a better performance!
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
     * {@link ServerTextChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> removeCategory() {
        return createUpdater().removeCategory().update();
    }

    /**
     * Gets the delay for slowmode.
     *
     * @return The delay in seconds.
     */
    int getSlowmodeDelayInSeconds();

    /**
     * Check whether slowmode is activated for this channel.
     *
     * @return Whether this channel enforces a slowmode.
     */
    default boolean hasSlowmode() {
        return getSlowmodeDelayInSeconds() != 0;
    }

    /**
     * Set a slowmode for this channel.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerTextChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param delay The slowmode delay in seconds.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateSlowmodeDelayInSeconds(int delay) {
        return createUpdater().setSlowmodeDelayInSeconds(delay).update();
    }

    /**
     * Deactivate slowmode for this channel.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerTextChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> unsetSlowmode() {
        return createUpdater().unsetSlowmode().update();
    }

    @Override
    default Optional<ServerTextChannel> getCurrentCachedInstance() {
        return getApi().getServerById(getServer().getId()).flatMap(server -> server.getTextChannelById(getId()));
    }

    @Override
    default CompletableFuture<ServerTextChannel> getLatestInstance() {
        Optional<ServerTextChannel> currentCachedInstance = getCurrentCachedInstance();
        if (currentCachedInstance.isPresent()) {
            return CompletableFuture.completedFuture(currentCachedInstance.get());
        } else {
            CompletableFuture<ServerTextChannel> result = new CompletableFuture<>();
            result.completeExceptionally(new NoSuchElementException());
            return result;
        }
    }

    /**
     * Creates a thread for a message in this ServerTextChannel.
     *
     * @param message             The message to create the thread for.
     * @param name                The Thread name.
     * @param autoArchiveDuration Duration in minutes to automatically archive the thread after recent activity.
     * @return The created ServerThreadChannel.
     */
    default CompletableFuture<ServerThreadChannel> createThreadForMessage(Message message, String name,
                                                                          AutoArchiveDuration autoArchiveDuration) {
        return createThreadForMessage(message, name, autoArchiveDuration.asInt());
    }

    /**
     * Creates a thread for a message in this ServerTextChannel.
     *
     * @param message             The message to create the thread for.
     * @param name                The Thread name.
     * @param autoArchiveDuration Duration in minutes to automatically archive the thread after recent activity.
     * @return The created ServerThreadChannel.
     */
    default CompletableFuture<ServerThreadChannel> createThreadForMessage(Message message, String name,
                                                                          Integer autoArchiveDuration) {
        return new ServerThreadChannelBuilder(message, name).setAutoArchiveDuration(autoArchiveDuration).create();
    }

    /**
     * Creates a thread which is not related to a message.
     *
     * <p>Creating a private thread requires the server to be boosted.
     * The 3 day and 7 day archive durations require the server to be boosted.
     * The server features will indicate if that is possible for the server.
     *
     * @param channelType         A ChannelType for a thread.
     * @param name                The thread name.
     * @param autoArchiveDuration Duration in minutes to automatically archive the thread after recent activity.
     * @return The created ServerThreadChannel.
     */
    default CompletableFuture<ServerThreadChannel> createThread(ChannelType channelType, String name,
                                                                Integer autoArchiveDuration) {
        return createThread(channelType, name, autoArchiveDuration, null);
    }

    /**
     * Creates a thread which is not related to a message.
     *
     * <p>Creating a private thread requires the server to be boosted.
     * The 3 day and 7 day archive durations require the server to be boosted.
     * The server features will indicate if that is possible for the server.
     *
     * @param channelType         A ChannelType for a thread.
     * @param name                The thread name.
     * @param autoArchiveDuration Duration in minutes to automatically archive the thread after recent activity.
     * @return The created ServerThreadChannel.
     */
    default CompletableFuture<ServerThreadChannel> createThread(ChannelType channelType, String name,
                                                                AutoArchiveDuration autoArchiveDuration) {
        return createThread(channelType, name, autoArchiveDuration.asInt(), null);
    }

    /**
     * Creates a thread which is not related to a message.
     *
     * <p>Creating a private thread requires the server to be boosted.
     * The 3 day and 7 day archive durations require the server to be boosted.
     * The server features will indicate if that is possible for the server.
     *
     * @param channelType         A ChannelType for a thread.
     * @param name                The thread name.
     * @param autoArchiveDuration Duration in minutes to automatically archive the thread after recent activity.
     * @param inviteable          Whether non-moderators can add other non-moderators to a thread;
     *                            only available when creating a private thread.
     * @return The created ServerThreadChannel.
     */
    default CompletableFuture<ServerThreadChannel> createThread(ChannelType channelType, String name,
                                                                Integer autoArchiveDuration,
                                                                Boolean inviteable) {
        return new ServerThreadChannelBuilder(this, channelType, name)
                .setAutoArchiveDuration(autoArchiveDuration)
                .setInvitableFlag(inviteable).create();
    }

    /**
     * Creates a thread which is not related to a message.
     *
     * <p>Creating a private thread requires the server to be boosted.
     * The 3 day and 7 day archive durations require the server to be boosted.
     * The server features will indicate if that is possible for the server.
     *
     * @param channelType         A ChannelType for a thread.
     * @param name                The thread name.
     * @param autoArchiveDuration Duration in minutes to automatically archive the thread after recent activity.
     * @param inviteable          Whether non-moderators can add other non-moderators to a thread;
     *                            only available when creating a private thread.
     * @return The created ServerThreadChannel.
     */
    default CompletableFuture<ServerThreadChannel> createThread(ChannelType channelType, String name,
                                                                AutoArchiveDuration autoArchiveDuration,
                                                                Boolean inviteable) {
        return createThread(channelType, name, autoArchiveDuration.asInt(), inviteable);
    }

    /**
     * Gets the public archived threads.
     *
     * <p>Returns archived threads in the channel that are public.
     * When called on a SERVER_TEXT_CHANNEL, returns threads of type SERVER_PUBLIC_THREAD.
     * When called on a SERVER_NEWS_CHANNEL returns threads of type SERVER_NEWS_THREAD.
     * Threads are ordered by archive_timestamp, in descending order.
     * Requires the READ_MESSAGE_HISTORY permission.
     *
     * @return The ArchivedThreads.
     */
    default CompletableFuture<ArchivedThreads> getPublicArchivedThreads() {
        return getPublicArchivedThreads(null, null);
    }

    /**
     * Gets the public archived threads.
     *
     * <p>Returns archived threads in the channel that are public.
     * When called on a SERVER_TEXT_CHANNEL, returns threads of type SERVER_PUBLIC_THREAD.
     * When called on a SERVER_NEWS_CHANNEL returns threads of type SERVER_NEWS_THREAD.
     * Threads are ordered by archive_timestamp, in descending order.
     * Requires the READ_MESSAGE_HISTORY permission.
     *
     * @param before Get public archived threads before the thread with this id.
     * @return The ArchivedThreads.
     */
    default CompletableFuture<ArchivedThreads> getPublicArchivedThreads(long before) {
        return getPublicArchivedThreads(before, null);
    }

    /**
     * Gets the public archived threads.
     *
     * <p>Returns archived threads in the channel that are public.
     * When called on a SERVER_TEXT_CHANNEL, returns threads of type SERVER_PUBLIC_THREAD.
     * When called on a SERVER_NEWS_CHANNEL returns threads of type SERVER_NEWS_THREAD.
     * Threads are ordered by archive_timestamp, in descending order.
     * Requires the READ_MESSAGE_HISTORY permission.
     *
     * @param limit The maximum amount of public archived threads.
     * @return The ArchivedThreads.
     */
    default CompletableFuture<ArchivedThreads> getPublicArchivedThreads(int limit) {
        return getPublicArchivedThreads(null, limit);
    }

    /**
     * Gets the public archived threads.
     *
     * <p>Returns archived threads in the channel that are public.
     * When called on a SERVER_TEXT_CHANNEL, returns threads of type SERVER_PUBLIC_THREAD.
     * When called on a SERVER_NEWS_CHANNEL returns threads of type SERVER_NEWS_THREAD.
     * Threads are ordered by archive_timestamp, in descending order.
     * Requires the READ_MESSAGE_HISTORY permission.
     *
     * @param before Get public archived threads before the thread with this id.
     * @param limit  The maximum amount of public archived threads.
     * @return The ArchivedThreads.
     */
    CompletableFuture<ArchivedThreads> getPublicArchivedThreads(Long before, Integer limit);

    /**
     * Gets the private archived threads.
     *
     * <p>Returns archived threads in the channel that are of type SERVER_PRIVATE_THREAD.
     * Threads are ordered by archive_timestamp, in descending order.
     * Requires both the READ_MESSAGE_HISTORY and MANAGE_THREADS permissions.
     *
     * @return The ArchivedThreads.
     */
    default CompletableFuture<ArchivedThreads> getPrivateArchivedThreads() {
        return getPrivateArchivedThreads(null, null);
    }

    /**
     * Gets the private archived threads.
     *
     * <p>Returns archived threads in the channel that are of type SERVER_PRIVATE_THREAD.
     * Threads are ordered by archive_timestamp, in descending order.
     * Requires both the READ_MESSAGE_HISTORY and MANAGE_THREADS permissions.
     *
     * @param before Get private archived threads before the thread with this id.
     * @return The ArchivedThreads.
     */
    default CompletableFuture<ArchivedThreads> getPrivateArchivedThreads(long before) {
        return getPrivateArchivedThreads(before, null);
    }

    /**
     * Gets the private archived threads.
     *
     * <p>Returns archived threads in the channel that are of type SERVER_PRIVATE_THREAD.
     * Threads are ordered by archive_timestamp, in descending order.
     * Requires both the READ_MESSAGE_HISTORY and MANAGE_THREADS permissions.
     *
     * @param limit The maximum amount of private archived threads.
     * @return The ArchivedThreads.
     */
    default CompletableFuture<ArchivedThreads> getPrivateArchivedThreads(int limit) {
        return getPrivateArchivedThreads(null, limit);
    }

    /**
     * Gets the private archived threads.
     *
     * <p>Returns archived threads in the channel that are of type SERVER_PRIVATE_THREAD.
     * Threads are ordered by archive_timestamp, in descending order.
     * Requires both the READ_MESSAGE_HISTORY and MANAGE_THREADS permissions.
     *
     * @param before Get private archived threads before the thread with this id.
     * @param limit  The maximum amount of private archived threads.
     * @return The ArchivedThreads.
     */
    CompletableFuture<ArchivedThreads> getPrivateArchivedThreads(Long before, Integer limit);

    /**
     * Gets the joined private archived threads.
     *
     * <p>Returns archived threads in the channel that are of type SERVER_PRIVATE_THREAD, and the user has joined.
     *
     * @return The ArchivedThreads.
     */
    default CompletableFuture<ArchivedThreads> getJoinedPrivateArchivedThreads() {
        return getJoinedPrivateArchivedThreads(null, null);
    }

    /**
     * Gets the joined private archived threads.
     *
     * <p>Returns archived threads in the channel that are of type SERVER_PRIVATE_THREAD, and the user has joined.
     *
     * @param before Get the joined private archived threads before the thread with this id.
     * @return The ArchivedThreads.
     */
    default CompletableFuture<ArchivedThreads> getJoinedPrivateArchivedThreads(long before) {
        return getJoinedPrivateArchivedThreads(before, null);
    }

    /**
     * Gets the joined private archived threads.
     *
     * <p>Returns archived threads in the channel that are of type SERVER_PRIVATE_THREAD, and the user has joined.
     *
     * @param limit The maximum amount of private archived threads.
     * @return The ArchivedThreads.
     */
    default CompletableFuture<ArchivedThreads> getJoinedPrivateArchivedThreads(int limit) {
        return getJoinedPrivateArchivedThreads(null, limit);
    }

    /**
     * Gets the joined private archived threads.
     *
     * <p>Returns archived threads in the channel that are of type SERVER_PRIVATE_THREAD, and the user has joined.
     *
     * @param before Get the joined private archived threads before the thread with this id.
     * @param limit  The maximum amount of private archived threads.
     * @return The ArchivedThreads.
     */
    CompletableFuture<ArchivedThreads> getJoinedPrivateArchivedThreads(Long before, Integer limit);
}

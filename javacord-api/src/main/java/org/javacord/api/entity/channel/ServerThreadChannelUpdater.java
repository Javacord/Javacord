package org.javacord.api.entity.channel;

import org.javacord.api.entity.channel.forum.ForumTag;
import org.javacord.api.entity.channel.internal.ServerThreadChannelUpdaterDelegate;
import org.javacord.api.util.internal.DelegateFactory;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update server thread channels.
 */
public class ServerThreadChannelUpdater extends ServerChannelUpdater<ServerThreadChannelUpdater> {

    /**
     * The server thread channel updater delegate used by this instance.
     */
    private final ServerThreadChannelUpdaterDelegate delegate;

    /**
     * Creates a new server thread channel updater.
     *
     * @param thread The thread to update.
     */
    public ServerThreadChannelUpdater(ServerThreadChannel thread) {
        super(thread);
        delegate = DelegateFactory.createServerThreadChannelUpdaterDelegate(thread);
    }

    /**
     * Queues the archived flag to be updated.
     *
     * @param archived The new archived flag of the thread.
     * @return The current instance in order to chain call methods.
     */
    public ServerThreadChannelUpdater setArchivedFlag(boolean archived) {
        delegate.setArchivedFlag(archived);
        return this;
    }

    /**
     * Queues the auto archive duration to be updated.
     *
     * @param autoArchiveDuration The new auto archive duration of the thread.
     * @return The current instance in order to chain call methods.
     */
    public ServerThreadChannelUpdater setAutoArchiveDuration(AutoArchiveDuration autoArchiveDuration) {
        delegate.setAutoArchiveDuration(autoArchiveDuration);
        return this;
    }

    /**
     * Queues the locked flag to be updated.
     *
     * @param locked The new locked flag of the thread.
     * @return The current instance in order to chain call methods.
     */
    public ServerThreadChannelUpdater setLockedFlag(boolean locked) {
        delegate.setLockedFlag(locked);
        return this;
    }

    /**
     * Queues the invitable flag to be updated. Only available for private threads.
     *
     * @param invitable The new invitable flag of the thread.
     * @return The current instance in order to chain call methods.
     */
    public ServerThreadChannelUpdater setInvitableFlag(boolean invitable) {
        delegate.setInvitableFlag(invitable);
        return this;
    }

    /**
     * Queues slowmode delay to be updated.
     *
     * @param delay The delay in seconds.
     * @return The current instance in order to chain call methods.
     */
    public ServerThreadChannelUpdater setSlowmodeDelayInSeconds(int delay) {
        delegate.setSlowmodeDelayInSeconds(delay);
        return this;
    }

    /**
     * Queues the (forum) thread's forum tags to be updated.
     *
     * @param forumTags The new forum tags of the thread.
     * @return The current instance in order to chain call methods.
     */
    public ServerThreadChannelUpdater setForumTags(Set<ForumTag> forumTags) {
        delegate.setForumTags(forumTags);
        return this;
    }

    /**
     * Performs the queued updates.
     *
     * @return A future to check if the update was successful.
     */
    @Override
    public CompletableFuture<Void> update() {
        return delegate.update();
    }
}

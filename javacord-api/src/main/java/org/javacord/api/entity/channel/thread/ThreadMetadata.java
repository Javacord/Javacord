package org.javacord.api.entity.channel.thread;

import java.time.Instant;
import java.util.Optional;

public interface ThreadMetadata {
    /**
     * Whether this thread is archived.
     *
     * @return Whether this thread is archived.
     */
    boolean isArchived();

    /**
     * Gets the duration for newly created threads, in minutes, to automatically
     * archive the thread after recent activity, can be set to: 60, 1440, 4320, 10080.
     *
     * @return The duration for newly created threads.
     */
    int getAutoArchiveDuration();

    /**
     * Whether this thread is locked.
     * When a thread is locked, only users with MANAGE_THREADS can unarchive it.
     *
     * @return Whether this thread is locked.
     */
    boolean isLocked();

    /**
     * Gets the timestamp when the thread's archive status was last changed, used for calculating recent activity.
     *
     * @return The timestamp when the thread's archive status was last changed.
     */
    Instant getArchiveTimestamp();

    /**
     * Informs you weather someone who is not a moderator can add non-moderator users to the thread.
     *
     * @return Whether non-moderator users can be added to the thread.
     */
    Optional<Boolean> isInvitable();

    /**
     * Gets the timestamp of when the thread was created.
     * Only populated for threads created after 2022-01-09
     *
     * @return The timestamp of when the thread was created.
     */
    Optional<Instant> getCreationTimestamp();
}

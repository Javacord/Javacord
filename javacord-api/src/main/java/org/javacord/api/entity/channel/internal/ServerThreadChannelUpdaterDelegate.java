package org.javacord.api.entity.channel.internal;

import org.javacord.api.entity.channel.AutoArchiveDuration;
import org.javacord.api.entity.channel.ServerThreadChannelUpdater;
import org.javacord.api.entity.channel.forum.ForumTag;

import java.util.Set;

/**
 * This class is internally used by the {@link ServerThreadChannelUpdater} to update server thread channels.
 * You usually don't want to interact with this object.
 */
public interface ServerThreadChannelUpdaterDelegate extends ServerChannelUpdaterDelegate {

    /**
     * Queues the archived flag to be updated.
     *
     * @param archived The new archived flag of the thread.
     */
    void setArchivedFlag(boolean archived);

    /**
     * Queues the auto archive duration to be updated.
     *
     * @param autoArchiveDuration The new auto archive duration of the thread.
     */
    void setAutoArchiveDuration(AutoArchiveDuration autoArchiveDuration);

    /**
     * Queues the locked flag to be updated.
     *
     * @param locked The new locked flag of the thread.
     */
    void setLockedFlag(boolean locked);

    /**
     * Queues the invitable flag to be updated.
     *
     * @param invitable The new invitable flag of the thread.
     */
    void setInvitableFlag(boolean invitable);

    /**
     * Queues slowmode delay to be updated.
     *
     * @param delay The delay in seconds.
     */
    void setSlowmodeDelayInSeconds(int delay);

    /**
     * Queues the (forum) thread's forum tags to be updated.
     *
     * @param forumTags The new forum tags of the thread.
     */
    void setForumTags(Set<ForumTag> forumTags);
}

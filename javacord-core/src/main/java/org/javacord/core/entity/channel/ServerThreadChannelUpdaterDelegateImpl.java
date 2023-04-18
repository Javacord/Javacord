package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.channel.AutoArchiveDuration;
import org.javacord.api.entity.channel.ServerThreadChannel;
import org.javacord.api.entity.channel.forum.ForumTag;
import org.javacord.api.entity.channel.internal.ServerThreadChannelUpdaterDelegate;

import java.util.Set;

/**
 * The implementation of {@link ServerThreadChannelUpdaterDelegate}.
 */
public class ServerThreadChannelUpdaterDelegateImpl extends ServerChannelUpdaterDelegateImpl
        implements ServerThreadChannelUpdaterDelegate {

    /**
     * The archived flag to update.
     */
    private Boolean archived = null;

    /**
     * The auto archive duration to update.
     */
    private AutoArchiveDuration autoArchiveDuration = null;

    /**
     * the locked flag to update.
     */
    private Boolean locked = null;

    /**
     * The invitable flag to update.
     */
    private Boolean invitable = null;

    /**
     * The slowmode delay.
     */
    private Integer delay = null;

    /**
     * The forum tags to update.
     */
    private Set<ForumTag> forumTags = null;

    /**
     * Creates a new server thread channel delegate.
     *
     * @param thread The thread to update.
     */
    public ServerThreadChannelUpdaterDelegateImpl(ServerThreadChannel thread) {
        super(thread);
    }

    @Override
    public void setArchivedFlag(boolean archived) {
        this.archived = archived;
    }

    @Override
    public void setAutoArchiveDuration(AutoArchiveDuration autoArchiveDuration) {
        this.autoArchiveDuration = autoArchiveDuration;
    }

    @Override
    public void setLockedFlag(boolean locked) {
        this.locked = locked;
    }

    @Override
    public void setInvitableFlag(boolean invitable) {
        this.invitable = invitable;
    }

    @Override
    public void setSlowmodeDelayInSeconds(int delay) {
        this.delay = delay;
    }

    @Override
    public void setForumTags(Set<ForumTag> forumTags) {
        channel.asServerForumChannel().ifPresent(serverForumChannel -> {
            for (ForumTag tag : forumTags) {
                if (!serverForumChannel.getForumTags().contains(tag)) {
                    throw new IllegalArgumentException("The tag " + tag.getName() + " is not a valid forum tag");
                }
            }

            this.forumTags = forumTags;
        });
    }

    @Override
    protected boolean prepareUpdateBody(ObjectNode body) {
        boolean patchThread = super.prepareUpdateBody(body);

        if (name != null) {
            body.put("name", name);
            patchThread = true;
        }
        if (archived != null) {
            body.put("archived", archived);
            patchThread = true;
        }
        if (autoArchiveDuration != null) {
            body.put("auto_archive_duration", autoArchiveDuration.asInt());
            patchThread = true;
        }
        if (locked != null) {
            body.put("locked", locked);
            patchThread = true;
        }
        if (invitable != null) {
            body.put("invitable", invitable);
            patchThread = true;
        }
        if (delay != null) {
            body.put("rate_limit_per_user", delay);
            patchThread = true;
        }
        if (forumTags != null && !forumTags.isEmpty()) {
            for (ForumTag tag : forumTags) {
                body.putArray("forum_tags").add(tag.getId());
            }
        }
        return patchThread;
    }

}
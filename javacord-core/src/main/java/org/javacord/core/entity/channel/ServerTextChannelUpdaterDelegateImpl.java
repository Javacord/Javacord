package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.internal.ServerTextChannelUpdaterDelegate;

/**
 * The implementation of {@link ServerTextChannelUpdaterDelegate}.
 */
public class ServerTextChannelUpdaterDelegateImpl extends RegularServerChannelUpdaterDelegateImpl
        implements ServerTextChannelUpdaterDelegate {

    /**
     * The topic to update.
     */
    protected String topic = null;

    /**
     * The nsfw flag to update.
     */
    protected Boolean nsfw = null;

    /**
     * The category to update.
     */
    protected ChannelCategory category = null;

    /**
     * Whether the category should be modified or not.
     */
    protected boolean modifyCategory = false;

    /**
     * The slowmode delay.
     */
    protected int delay = 0;

    /**
     * Whether the slowmode delay should be modified or not.
     */
    protected boolean modifyDelay = false;

    /**
     * Creates a new server text channel updater delegate.
     *
     * @param channel The channel to update.
     */
    public ServerTextChannelUpdaterDelegateImpl(ServerTextChannel channel) {
        super(channel);
    }

    @Override
    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public void setNsfwFlag(boolean nsfw) {
        this.nsfw = nsfw;
    }

    @Override
    public void setCategory(ChannelCategory category) {
        this.category = category;
        this.modifyCategory = true;
    }

    @Override
    public void removeCategory() {
        setCategory(null);
    }

    @Override
    public void setSlowmodeDelayInSeconds(int delay) {
        this.delay = delay;
        this.modifyDelay = true;
    }

    @Override
    protected boolean prepareUpdateBody(ObjectNode body) {
        boolean patchChannel = super.prepareUpdateBody(body);
        if (topic != null) {
            body.put("topic", topic);
            patchChannel = true;
        }
        if (nsfw != null) {
            body.put("nsfw", nsfw.booleanValue());
            patchChannel = true;
        }
        if (modifyCategory) {
            body.put("parent_id", category == null ? null : category.getIdAsString());
            patchChannel = true;
        }
        if (modifyDelay) {
            body.put("rate_limit_per_user", delay);
            patchChannel = true;
        }
        return patchChannel;
    }
}

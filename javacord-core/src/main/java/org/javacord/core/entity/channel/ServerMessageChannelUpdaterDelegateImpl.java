package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.ServerMessageChannel;
import org.javacord.api.entity.channel.internal.ServerMessageChannelUpdaterDelegate;

/**
 * The implementation of {@link ServerMessageChannelUpdaterDelegate}.
 */
public class ServerMessageChannelUpdaterDelegateImpl extends RegularServerChannelUpdaterDelegateImpl
        implements ServerMessageChannelUpdaterDelegate {

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
     * Creates a new server message channel updater delegate.
     *
     * @param channel The channel to update.
     */
    public ServerMessageChannelUpdaterDelegateImpl(ServerMessageChannel channel) {
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
        return patchChannel;
    }
}

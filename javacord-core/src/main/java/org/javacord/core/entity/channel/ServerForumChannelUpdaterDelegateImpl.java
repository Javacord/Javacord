package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.ServerForumChannel;
import org.javacord.api.entity.channel.internal.ServerForumChannelUpdaterDelegate;

/**
 * The implementation of {@link ServerForumChannelUpdaterDelegate}.
 */
public class ServerForumChannelUpdaterDelegateImpl extends RegularServerChannelUpdaterDelegateImpl
        implements ServerForumChannelUpdaterDelegate {

    /**
     * The category to update.
     */
    protected ChannelCategory category = null;

    /**
     * Whether the category should be modified or not.
     */
    protected boolean modifyCategory = false;


    /**
     * Creates a new server forum channel updater delegate.
     *
     * @param channel The channel to update.
     */
    public ServerForumChannelUpdaterDelegateImpl(ServerForumChannel channel) {
        super(channel);
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

        if (modifyCategory) {
            body.put("parent_id", category == null ? null : category.getIdAsString());
            patchChannel = true;
        }

        return patchChannel;
    }
}

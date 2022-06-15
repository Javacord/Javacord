package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.internal.ServerVoiceChannelUpdaterDelegate;

/**
 * The implementation of {@link ServerVoiceChannelUpdaterDelegate}.
 */
public class ServerVoiceChannelUpdaterDelegateImpl extends RegularServerChannelUpdaterDelegateImpl
        implements ServerVoiceChannelUpdaterDelegate {

    /**
     * The bitrate to update.
     */
    protected Integer bitrate = null;

    /**
     * The nsfw flag to update.
     */
    protected Boolean nsfw = null;

    /**
     * The user limit to update.
     */
    protected Integer userLimit = null;

    /**
     * The category to update.
     */
    protected ChannelCategory category = null;

    /**
     * Whether the category should be modified or not.
     */
    protected boolean modifyCategory = false;

    /**
     * Creates a new server voice channel updater.
     *
     * @param channel The channel to update.
     */
    public ServerVoiceChannelUpdaterDelegateImpl(ServerVoiceChannel channel) {
        super(channel);
    }

    @Override
    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    @Override
    public void setUserLimit(int userLimit) {
        this.userLimit = userLimit;
    }

    @Override
    public void removeUserLimit() {
        this.userLimit = 0;
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
    public void setNsfw(Boolean nsfw) {
        this.nsfw = nsfw;
    }

    @Override
    protected boolean prepareUpdateBody(ObjectNode body) {
        boolean patchChannel = super.prepareUpdateBody(body);
        if (bitrate != null) {
            body.put("bitrate", bitrate.intValue());
            patchChannel = true;
        }
        if (nsfw != null) {
            body.put("nsfw", nsfw.booleanValue());
            patchChannel = true;
        }
        if (userLimit != null) {
            body.put("user_limit", userLimit.intValue());
            patchChannel = true;
        }
        if (modifyCategory) {
            body.put("parent_id", category == null ? null : category.getIdAsString());
            patchChannel = true;
        }
        return patchChannel;
    }

}

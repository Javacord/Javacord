package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.internal.ServerVoiceChannelUpdaterDelegate;

/**
 * The implementation of {@link ServerVoiceChannelUpdaterDelegate}.
 */
public class ServerVoiceChannelUpdaterDelegateImpl extends TextableRegularServerChannelUpdaterDelegateImpl
        implements ServerVoiceChannelUpdaterDelegate {

    /**
     * The bitrate to update.
     */
    protected Integer bitrate = null;

    /**
     * The user limit to update.
     */
    protected Integer userLimit = null;

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
    protected boolean prepareUpdateBody(ObjectNode body) {
        boolean patchChannel = super.prepareUpdateBody(body);
        if (bitrate != null) {
            body.put("bitrate", bitrate.intValue());
            patchChannel = true;
        }
        if (userLimit != null) {
            body.put("user_limit", userLimit.intValue());
            patchChannel = true;
        }
        return patchChannel;
    }

}

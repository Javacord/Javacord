package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.internal.ServerTextChannelUpdaterDelegate;

/**
 * The implementation of {@link ServerTextChannelUpdaterDelegate}.
 */
public class ServerTextChannelUpdaterDelegateImpl extends TextableRegularServerChannelUpdaterDelegateImpl
        implements ServerTextChannelUpdaterDelegate {

    /**
     * The topic to update.
     */
    protected String topic = null;

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
    protected boolean prepareUpdateBody(ObjectNode body) {
        boolean patchChannel = super.prepareUpdateBody(body);
        if (topic != null) {
            body.put("topic", topic);
            patchChannel = true;
        }
        return patchChannel;
    }
}

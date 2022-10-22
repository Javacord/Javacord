package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.internal.ServerTextChannelUpdaterDelegate;

/**
 * The implementation of {@link ServerTextChannelUpdaterDelegate}.
 */
public class ServerTextChannelUpdaterDelegateImpl extends ServerMessageChannelUpdaterDelegateImpl
        implements ServerTextChannelUpdaterDelegate {

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
    public void setSlowmodeDelayInSeconds(int delay) {
        this.delay = delay;
        this.modifyDelay = true;
    }

    @Override
    protected boolean prepareUpdateBody(ObjectNode body) {
        boolean patchChannel = super.prepareUpdateBody(body);
        if (modifyDelay) {
            body.put("rate_limit_per_user", delay);
            patchChannel = true;
        }
        return patchChannel;
    }
}

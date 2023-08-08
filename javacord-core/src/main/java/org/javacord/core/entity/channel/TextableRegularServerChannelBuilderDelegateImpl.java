package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.internal.ServerTextChannelBuilderDelegate;
import org.javacord.api.entity.channel.internal.TextableRegularServerChannelBuilderDelegate;
import org.javacord.core.entity.server.ServerImpl;

/**
 * The implementation of {@link TextableRegularServerChannelBuilderDelegate}.
 */
public class TextableRegularServerChannelBuilderDelegateImpl extends RegularServerChannelBuilderDelegateImpl
        implements TextableRegularServerChannelBuilderDelegate {

    /**
     * The category of the channel.
     */
    private ChannelCategory category = null;

    /**
     * The slowmode delay of the channel.
     */
    private int delay;

    /**
     * Whether the delay has been modified from the original value.
     */
    private boolean delayModified;

    /**
     * Creates a new server text channel builder delegate.
     *
     * @param server The server of the server text channel.
     */
    public TextableRegularServerChannelBuilderDelegateImpl(ServerImpl server) {
        super(server);
    }

    @Override
    public void setCategory(ChannelCategory category) {
        this.category = category;
    }

    @Override
    public void setSlowmodeDelayInSeconds(int delay) {
        this.delay = delay;
        delayModified = true;
    }

    @Override
    protected void prepareBody(ObjectNode body) {
        super.prepareBody(body);

        if (category != null) {
            body.put("parent_id", category.getIdAsString());
        }
        if (delayModified) {
            body.put("rate_limit_per_user", delay);
        }
    }
}

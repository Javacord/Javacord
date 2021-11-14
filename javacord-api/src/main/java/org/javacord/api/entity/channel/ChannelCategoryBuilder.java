package org.javacord.api.entity.channel;

import org.javacord.api.entity.channel.internal.ChannelCategoryBuilderDelegate;
import org.javacord.api.entity.server.Server;
import org.javacord.api.util.internal.DelegateFactory;
import java.util.concurrent.CompletableFuture;

/**
 * This class is used to create new channel categories.
 */
public class ChannelCategoryBuilder extends RegularServerChannelBuilder<ChannelCategoryBuilder> {

    /**
     * The channel category delegate used by this instance.
     */
    private final ChannelCategoryBuilderDelegate delegate;

    /**
     * Creates a new channel category builder.
     *
     * @param server The server of the channel category.
     */
    public ChannelCategoryBuilder(Server server) {
        super(ChannelCategoryBuilder.class, DelegateFactory.createChannelCategoryBuilderDelegate(server));
        delegate = (ChannelCategoryBuilderDelegate) super.delegate;
    }

    /**
     * Creates the channel category.
     *
     * @return The created channel category.
     */
    public CompletableFuture<ChannelCategory> create() {
        return delegate.create();
    }

}

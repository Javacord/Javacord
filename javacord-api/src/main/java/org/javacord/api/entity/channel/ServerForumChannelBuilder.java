package org.javacord.api.entity.channel;

import org.javacord.api.entity.channel.internal.ServerForumChannelBuilderDelegate;
import org.javacord.api.entity.server.Server;
import org.javacord.api.util.internal.DelegateFactory;
import java.util.concurrent.CompletableFuture;

/**
 * This class is used to create new server forum channels.
 */
public class ServerForumChannelBuilder extends RegularServerChannelBuilder<ServerForumChannelBuilder> {

    /**
     * The server forum channel delegate used by this instance.
     */
    private final ServerForumChannelBuilderDelegate delegate;

    /**
     * Creates a new server forum channel builder.
     *
     * @param server The server of the server forum channel.
     */
    public ServerForumChannelBuilder(Server server) {
        super(ServerForumChannelBuilder.class, DelegateFactory.createServerForumChannelBuilderDelegate(server));
        delegate = (ServerForumChannelBuilderDelegate) super.delegate;
    }

    /**
     * Sets the category of the channel.
     *
     * @param category The category of the channel.
     * @return The current instance in order to chain call methods.
     */
    public ServerForumChannelBuilder setCategory(ChannelCategory category) {
        delegate.setCategory(category);
        return this;
    }


    /**
     * Creates the server forum channel.
     *
     * @return The created forum channel.
     */
    public CompletableFuture<ServerForumChannel> create() {
        return delegate.create();
    }

}

package org.javacord.api.entity.channel;

import org.javacord.api.entity.Mentionable;
import org.javacord.api.listener.channel.server.forum.ServerForumChannelAttachableListenerManager;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a server forum channel.
 */
public interface ServerForumChannel extends RegularServerChannel, Mentionable, Categorizable,
        ServerForumChannelAttachableListenerManager {

    /**
     * Creates an updater for this channel.
     *
     * @return An updater for this channel.
     */
    default ServerForumChannelUpdater createUpdater() {
        return new ServerForumChannelUpdater(this);
    }

    /**
     * {@inheritDoc}
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerForumChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param category The new category of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateCategory(ChannelCategory category) {
        return createUpdater().setCategory(category).update();
    }

    /**
     * {@inheritDoc}
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerForumChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> removeCategory() {
        return createUpdater().removeCategory().update();
    }

}

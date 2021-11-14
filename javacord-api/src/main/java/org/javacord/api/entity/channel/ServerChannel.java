package org.javacord.api.entity.channel;

import org.javacord.api.entity.Nameable;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.server.invite.InviteBuilder;
import org.javacord.api.entity.server.invite.RichInvite;
import org.javacord.api.listener.channel.server.ServerChannelAttachableListenerManager;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a server channel.
 */
public interface ServerChannel extends Channel, Nameable, ServerChannelAttachableListenerManager {

    /**
     * Gets the server of the channel.
     *
     * @return The server of the channel.
     */
    Server getServer();

    /**
     * Creates an invite builder for this channel.
     *
     * @return An invite builder for this channel.
     */
    default InviteBuilder createInviteBuilder() {
        return new InviteBuilder(this);
    }

    /**
     * Gets the invites of the server.
     *
     * @return The invites of the server.
     */
    CompletableFuture<Collection<RichInvite>> getInvites();

    /**
     * Create an updater for this channel.
     *
     * @return An updater for this channel.
     */
    default ServerChannelUpdater createUpdater() {
        return new ServerChannelUpdater(this);
    }

    /**
     * Updates the name of the channel.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param name The new name of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateName(String name) {
        return createUpdater().setName(name).update();
    }

    /**
     * Deletes the channel.
     *
     * @return A future to tell us if the deletion was successful.
     */
    default CompletableFuture<Void> delete() {
        return delete(null);
    }

    /**
     * Deletes the channel.
     *
     * @param reason The audit log reason for the deletion.
     * @return A future to tell us if the deletion was successful.
     */
    CompletableFuture<Void> delete(String reason);

    @Override
    default Optional<? extends ServerChannel> getCurrentCachedInstance() {
        return getApi().getServerById(getServer().getId()).flatMap(server -> server.getChannelById(getId()));
    }

    @Override
    default CompletableFuture<? extends ServerChannel> getLatestInstance() {
        Optional<? extends ServerChannel> currentCachedInstance = getCurrentCachedInstance();
        if (currentCachedInstance.isPresent()) {
            return CompletableFuture.completedFuture(currentCachedInstance.get());
        } else {
            CompletableFuture<? extends ServerChannel> result = new CompletableFuture<>();
            result.completeExceptionally(new NoSuchElementException());
            return result;
        }
    }

}

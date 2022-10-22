package org.javacord.api.entity.channel;

import org.javacord.api.listener.channel.server.text.ServerTextChannelAttachableListenerManager;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a server text channel.
 */
public interface ServerTextChannel extends ServerMessageChannel, ServerTextChannelAttachableListenerManager {

    /**
     * Gets the type of channel.
     *
     * @return The type of channel.
     */
    @Override
    default ChannelType getType() {
        return ChannelType.SERVER_TEXT_CHANNEL;
    }

    /**
     * Gets the delay for slowmode.
     *
     * @return The delay in seconds.
     */
    int getSlowmodeDelayInSeconds();

    /**
     * Check whether slowmode is activated for this channel.
     *
     * @return Whether this channel enforces a slowmode.
     */
    default boolean hasSlowmode() {
        return getSlowmodeDelayInSeconds() != 0;
    }

    /**
     * Set a slowmode for this channel.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerTextChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param delay The slowmode delay in seconds.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateSlowmodeDelayInSeconds(int delay) {
        return createUpdater().setSlowmodeDelayInSeconds(delay).update();
    }

    /**
     * Deactivate slowmode for this channel.
     *
     * <p>If you want to update several settings at once, it's recommended to use the
     * {@link ServerTextChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> unsetSlowmode() {
        return createUpdater().unsetSlowmode().update();
    }

    /**
     * Creates an updater for this channel.
     *
     * @return An updater for this channel.
     */
    default ServerTextChannelUpdater createUpdater() {
        return new ServerTextChannelUpdater(this);
    }
}

package de.btobastian.javacord.entities.channels;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a server voice channel.
 */
public interface ServerVoiceChannel extends ServerChannel, VoiceChannel, Categorizable {

    @Override
    default ChannelType getType() {
        return ChannelType.SERVER_VOICE_CHANNEL;
    }

    /**
     * Gets the updater for this channel.
     *
     * @return The updater for this channel.
     */
    default ServerVoiceChannelUpdater getUpdater() {
        return new ServerVoiceChannelUpdater(this);
    }

    /**
     * Updates the bitrate of the channel.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerTextChannelUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param bitrate The new bitrate of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateBitrate(int bitrate) {
        return getUpdater().setBitrate(bitrate).update();
    }

    /**
     * Updates the user limit of the channel.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerTextChannelUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param userLimit The new user limit of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateUserLimit(int userLimit) {
        return getUpdater().setUserLimit(userLimit).update();
    }

    /**
     * Removes the user limit of the channel.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerVoiceChannelUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> removeUserLimit() {
        return getUpdater().removeUserLimit().update();
    }

    /**
     * {@inheritDoc}
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerTextChannelUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param category The new category of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateCategory(ChannelCategory category) {
        return getUpdater().setCategory(category).update();
    }

    /**
     * {@inheritDoc}
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerVoiceChannelUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> removeCategory() {
        return getUpdater().removeCategory().update();
    }

}

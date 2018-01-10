package de.btobastian.javacord.entities.channels;

import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.Mentionable;
import de.btobastian.javacord.listeners.server.channel.ServerTextChannelChangeTopicListener;
import de.btobastian.javacord.utils.ListenerManager;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a server text channel.
 */
public interface ServerTextChannel extends ServerChannel, TextChannel, Mentionable, Categorizable {

    @Override
    default ChannelType getType() {
        return ChannelType.SERVER_TEXT_CHANNEL;
    }

    /**
     * Checks is the channel is "not safe for work".
     *
     * @return Whether the channel is "not safe for work" or not.
     */
    boolean isNsfw();

    /**
     * Gets the topic of the channel.
     *
     * @return The topic of the channel.
     */
    String getTopic();

    /**
     * Gets the updater for this channel.
     *
     * @return The updater for this channel.
     */
    default ServerTextChannelUpdater getUpdater() {
        return new ServerTextChannelUpdater(this);
    }

    /**
     * Updates the topic of the channel.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerTextChannelUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param topic The new topic of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateTopic(String topic) {
        return getUpdater().setTopic(topic).update();
    }

    /**
     * Updates the nsfw flag of the channel.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerTextChannelUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @param nsfw The new nsfw flag of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateNsfwFlag(boolean nsfw) {
        return getUpdater().setNsfwFlag(nsfw).update();
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
     * {@link ServerTextChannelUpdater} from {@link #getUpdater()} which provides a better performance!
     *
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> removeCategory() {
        return getUpdater().removeCategory().update();
    }

    /**
     * Adds a listener, which listens to topic changes of this channel.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    default ListenerManager<ServerTextChannelChangeTopicListener> addServerTextChannelChangeTopicListener(
            ServerTextChannelChangeTopicListener listener) {
        return ((ImplDiscordApi) getApi()).addObjectListener(
                ServerTextChannel.class, getId(), ServerTextChannelChangeTopicListener.class, listener);
    }

    /**
     * Gets a list with all registered server text channel change topic listeners.
     *
     * @return A list with all registered server text channel change topic listeners.
     */
    default List<ServerTextChannelChangeTopicListener> getServerTextChannelChangeTopicListeners() {
        return ((ImplDiscordApi) getApi())
                .getObjectListeners(ServerTextChannel.class, getId(), ServerTextChannelChangeTopicListener.class);
    }

}

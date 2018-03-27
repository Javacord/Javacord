package org.javacord.api.entity.channel;

import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.webhook.WebhookBuilder;
import org.javacord.api.listener.ObjectAttachableListener;
import org.javacord.api.listener.channel.server.ServerChannelChangeNsfwFlagListener;
import org.javacord.api.listener.channel.server.text.ServerTextChannelAttachableListener;
import org.javacord.api.listener.channel.server.text.ServerTextChannelChangeTopicListener;
import org.javacord.api.listener.channel.server.text.WebhooksUpdateListener;
import org.javacord.api.util.event.ListenerManager;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
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
     * Creates a webhook builder for this channel.
     *
     * @return A webhook builder.
     */
    default WebhookBuilder createWebhookBuilder() {
        return new WebhookBuilder(this);
    }

    /**
     * Creates an updater for this channel.
     *
     * @return An updater for this channel.
     */
    default ServerTextChannelUpdater createUpdater() {
        return new ServerTextChannelUpdater(this);
    }

    /**
     * Updates the topic of the channel.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerTextChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param topic The new topic of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateTopic(String topic) {
        return createUpdater().setTopic(topic).update();
    }

    /**
     * Updates the nsfw flag of the channel.
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerTextChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param nsfw The new nsfw flag of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateNsfwFlag(boolean nsfw) {
        return createUpdater().setNsfwFlag(nsfw).update();
    }

    /**
     * {@inheritDoc}
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerTextChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @param category The new category of the channel.
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> updateCategory(ChannelCategory category) {
        return createUpdater().setCategory(category).update();
    }

    /**
     * {@inheritDoc}
     * <p>
     * If you want to update several settings at once, it's recommended to use the
     * {@link ServerTextChannelUpdater} from {@link #createUpdater()} which provides a better performance!
     *
     * @return A future to check if the update was successful.
     */
    default CompletableFuture<Void> removeCategory() {
        return createUpdater().removeCategory().update();
    }

    /**
     * Adds a listener, which listens to topic changes of this channel.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerTextChannelChangeTopicListener> addServerTextChannelChangeTopicListener(
            ServerTextChannelChangeTopicListener listener);

    /**
     * Gets a list with all registered server text channel change topic listeners.
     *
     * @return A list with all registered server text channel change topic listeners.
     */
    List<ServerTextChannelChangeTopicListener> getServerTextChannelChangeTopicListeners();

    /**
     * Adds a listener, which listens to server channel nsfw flag changes of this channel.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<ServerChannelChangeNsfwFlagListener> addServerChannelChangeNsfwFlagListener(
            ServerChannelChangeNsfwFlagListener listener);

    /**
     * Gets a list with all registered server channel change nsfw flag listeners.
     *
     * @return A list with all registered server channel change nsfw flag listeners.
     */
    List<ServerChannelChangeNsfwFlagListener> getServerChannelChangeNsfwFlagListeners();

    /**
     * Adds a listener, which listens to webhook updates of this channel.
     *
     * @param listener The listener to add.
     * @return The manager of the listener.
     */
    ListenerManager<WebhooksUpdateListener> addWebhooksUpdateListener(WebhooksUpdateListener listener);

    /**
     * Gets a list with all registered webhooks update listeners.
     *
     * @return A list with all registered webhooks update listeners.
     */
    List<WebhooksUpdateListener> getWebhooksUpdateListeners();

    /**
     * Adds a listener that implements one or more {@code ServerTextChannelAttachableListener}s.
     * Adding a listener multiple times will only add it once
     * and return the same listener managers on each invocation.
     * The order of invocation is according to first addition.
     *
     * @param listener The listener to add.
     * @param <T> The type of the listener.
     * @return The managers for the added listener.
     */
    <T extends ServerTextChannelAttachableListener & ObjectAttachableListener>
    Collection<ListenerManager<? extends ServerTextChannelAttachableListener>> addServerTextChannelAttachableListener(
            T listener);

    /**
     * Removes a listener that implements one or more {@code ServerTextChannelAttachableListener}s.
     *
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    <T extends ServerTextChannelAttachableListener & ObjectAttachableListener> void
    removeServerTextChannelAttachableListener(T listener);

    /**
     * Gets a map with all registered listeners that implement one or more {@code ServerTextChannelAttachableListener}s
     * and their assigned listener classes they listen to.
     *
     * @param <T> The type of the listeners.
     * @return A map with all registered listeners that implement one or more
     * {@code ServerTextChannelAttachableListener}s and their assigned listener classes they listen to.
     */
    <T extends ServerTextChannelAttachableListener & ObjectAttachableListener> Map<T, List<Class<T>>>
    getServerTextChannelAttachableListeners();

    /**
     * Removes a listener from this server text channel.
     *
     * @param listenerClass The listener class.
     * @param listener The listener to remove.
     * @param <T> The type of the listener.
     */
    <T extends ServerTextChannelAttachableListener & ObjectAttachableListener> void removeListener(
            Class<T> listenerClass, T listener);

    @Override
    default Optional<ServerTextChannel> getCurrentCachedInstance() {
        return getApi().getServerById(getServer().getId()).flatMap(server -> server.getTextChannelById(getId()));
    }

    @Override
    default CompletableFuture<ServerTextChannel> getLatestInstance() {
        Optional<ServerTextChannel> currentCachedInstance = getCurrentCachedInstance();
        if (currentCachedInstance.isPresent()) {
            return CompletableFuture.completedFuture(currentCachedInstance.get());
        } else {
            CompletableFuture<ServerTextChannel> result = new CompletableFuture<>();
            result.completeExceptionally(new NoSuchElementException());
            return result;
        }
    }

}

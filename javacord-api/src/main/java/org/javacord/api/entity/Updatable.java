package org.javacord.api.entity;

import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents an entity which is updatable.
 */
public interface Updatable<T extends DiscordEntity> {

    /**
     * Gets an updated instance of this entity from the cache or from Discord directly. This is for example necessary
     * if an instance got invalid by a reconnect to Discord which invalidates all existing instances which means they
     * do not get any further updates from Discord applied. Due to that, references to instances should usually not be
     * held for an extended period of time. If they are, this method can be used to retrieve the current instance from
     * the cache if present or from Discord directly.
     *
     * <p>This method returns the currently cached entity if present, or request the entity from Discord if it is not
     * cached or not permanently cached. If the entity is a fully cached entity and is not in the cache any longer,
     * for example because it was deleted or the message was thrown out of the cache, the {@code CompletableFuture}
     * completes exceptionally with a {@link NoSuchElementException}. If a request to Discord is made, the according
     * remote call exception will be used to complete the {@code CompletableFuture} exceptionally.
     *
     * @return The current cached instance.
     */
    CompletableFuture<T> getLatestInstance();

}

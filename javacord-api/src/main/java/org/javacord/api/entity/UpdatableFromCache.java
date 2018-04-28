package org.javacord.api.entity;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents an entity which is updatable from cache.
 */
public interface UpdatableFromCache<T extends DiscordEntity> extends Updatable<T> {

    /**
     * Gets an updated instance of this entity from the cache. This is for example necessary if an instance got invalid
     * by a reconnect to Discord which invalidates all existing instances which means they do not get any further
     * updates from Discord applied. Due to that, references to instances should usually not be held for an extended
     * period of time. If they are, this method can be used to retrieve the current instance from the cache, that gets
     * updates from Discord, in case this one was invalidated.
     *
     * <p>This method returns the currently cached entity, or an empty {@code Optional} if the entity is not cached
     * any longer, for example because it was deleted or the message was thrown out of the cache.
     *
     * @return The current cached instance.
     */
    Optional<T> getCurrentCachedInstance();

    @Override
    default CompletableFuture<T> getLatestInstance() {
        Optional<T> currentCachedInstance = getCurrentCachedInstance();
        if (currentCachedInstance.isPresent()) {
            return CompletableFuture.completedFuture(currentCachedInstance.get());
        } else {
            CompletableFuture<T> result = new CompletableFuture<>();
            result.completeExceptionally(new NoSuchElementException());
            return result;
        }
    }

}

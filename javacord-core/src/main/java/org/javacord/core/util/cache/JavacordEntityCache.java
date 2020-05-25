package org.javacord.core.util.cache;

import java.util.function.UnaryOperator;

/**
 * An immutable cache with all Javacord entites.
 */
public class JavacordEntityCache {

    private static final JavacordEntityCache EMPTY_CACHE = new JavacordEntityCache(ChannelCache.empty());

    private final ChannelCache channelCache;

    /**
     * Gets an empty Javacord cache.
     *
     * @return An empty Javacord cache.
     */
    public static JavacordEntityCache empty() {
        return EMPTY_CACHE;
    }

    private JavacordEntityCache(ChannelCache channelCache) {
        this.channelCache = channelCache;
    }

    /**
     * Gets the channel cache.
     *
     * @return The channel cache.
     */
    public ChannelCache getChannelCache() {
        return channelCache;
    }

    /**
     * Updates the channel cache.
     *
     * @param mapper A function that takes the old channel cache and returns the new one.
     * @return The new Javacord entity cache.
     */
    public JavacordEntityCache updateChannelCache(UnaryOperator<ChannelCache> mapper) {
        return new JavacordEntityCache(mapper.apply(channelCache));
    }

    /**
     * Sets the channel cache.
     *
     * @param channelCache The channel cache to set.
     * @return The new Javacord entity cache.
     */
    public JavacordEntityCache setChannelCache(ChannelCache channelCache) {
        return new JavacordEntityCache(channelCache);
    }
}

package org.javacord.core.util.cache;

import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.Map;
import io.vavr.collection.Set;

import java.util.Optional;
import java.util.function.Function;

/**
 * An immutable cache, optionally with indexes.
 *
 * <p>The cache can store any elements and supports indexes to quickly access a subset of elements by a specific
 * criteria (the "key") with an effective (assuming an even distribution of hash keys) time-complexity of {@code O(1)}.
 *
 * <p>Ideally, the cache is only filled with immutable elements, but it also supports mutable objects.
 * However, for mutable objects, the {@link #updateIndexesOfElement(Object)} methods should be called every time an
 * element update changes the key for this element for one of the indexes.
 *
 * @param <T> The type of the elements in the cache.
 */
public class Cache<T> {

    /**
     * A set with all elements in the cache.
     */
    private final Set<T> elements;

    /**
     * A map with all indexes.
     *
     * <p>The map's key is the index name and the value is the index itself.
     */
    private final Map<String, Index<Object, T>> indexes;

    /**
     * Creates a new cache.
     *
     * @param elements The elements in the cache.
     * @param indexes The indexes.
     */
    private Cache(Set<T> elements, Map<String, Index<Object, T>> indexes) {
        this.elements = elements;
        this.indexes = indexes;
    }

    /**
     * Gets an empty cache with no elements and no indexes.
     *
     * @param <T> The type of the elements in the cache.
     * @return An empty element cache.
     */
    public static <T> Cache<T> empty() {
        return new Cache<>(HashSet.empty(), HashMap.empty());
    }

    /**
     * Adds an index to the cache.
     *
     * <p>Indexes allow quick access (effectively {@code O(1)} to elements in the cache by the key for the index like
     * for example the id or sub type.
     *
     * <p>Compound indexes can easily be achieved by using a {@link io.vavr.Tuple} or {@link io.vavr.collection.Seq}
     * with all keys as the return value of the mapping function.
     *
     * <p>This method has a time-complexity of {@code O(n)} with {@code n} being the amount of elements in the cache.
     *
     * @param indexName The name of the index.
     * @param mappingFunction A function to map elements to their key.
     *                        The function is allowed to return {@code null} which means that the element will not be
     *                        included in the index.
     * @return The new cache with the added index.
     * @throws IllegalStateException If the cache already has an index with the given name.
     */
    public Cache<T> addIndex(String indexName, Function<T, Object> mappingFunction) {
        if (indexes.containsKey(indexName)) {
            throw new IllegalStateException("The cache already has an index with name " + indexName);
        }
        Index<Object, T> index = new Index<>(mappingFunction);
        for (T element : elements) {
            index = index.addElement(element);
        }
        Map<String, Index<Object, T>> newIndexes = indexes.put(indexName, index);
        return new Cache<>(elements, newIndexes);
    }

    /**
     * Adds an element to the cache.
     *
     * <p>This method has an effective time complexity of {@code O(1)} regarding the amount of elements already in the
     * cache and a time complexity of {@code O(n)} regarding the amount of indexes in the cache (assuming the index's
     * mapping function has an effective complexity of {@code O(1)}).
     *
     * @param element The element to add.
     * @return The new cache after adding the element.
     */
    public Cache<T> addElement(T element) {
        Set<T> newElements = elements.add(element);
        Map<String, Index<Object, T>> newIndexes = indexes.mapValues(index -> index.addElement(element));
        return new Cache<>(newElements, newIndexes);
    }

    /**
     * Removes an element from the cache.
     *
     * <p>This method has an effective time complexity of {@code O(1)} regarding the amount of elements already in the
     * cache and a time complexity of {@code O(n)} regarding the amount of indexes in the cache.
     *
     * @param element The element to remove.
     * @return The new cache after removing the element.
     */
    public Cache<T> removeElement(T element) {
        Set<T> newElements = elements.remove(element);
        Map<String, Index<Object, T>> newIndexes = indexes.mapValues(index -> index.removeElement(element));
        return new Cache<>(newElements, newIndexes);
    }

    /**
     * Updates the indexes of the cache for the given element.
     *
     * <p>The update method should be called every time an element's mutation changes the key of one of the indexes.
     * Ideally the {@code IndexedCache} is only filled with immutable elements which would make this method obsolete.
     *
     * <p>This method has an effective time complexity of {@code O(1)} regarding the amount of elements already in the
     * cache and a time complexity of {@code O(n)} regarding the amount of indexes in the cache (assuming the index's
     * mapping function has an effective complexity of {@code O(1)}).
     *
     * @param element The element of which the values did change.
     * @return The new cache after updating the element.
     */
    public Cache<T> updateIndexesOfElement(T element) {
        if (!elements.contains(element)) {
            return this;
        }
        return removeElement(element).addElement(element);
    }

    /**
     * Gets a set with all elements in the cache.
     *
     * <p>This method has a time complexity of {@code O(1)}.
     *
     * @return All elements in the cache.
     */
    public Set<T> getAll() {
        return elements;
    }

    /**
     * Gets any element in the cache that has the given key.
     *
     * <p>This method has an effective time complexity of {@code O(1)}.
     *
     * @param indexName The name of the index.
     * @param key The key of the element.
     * @return An element with the given key.
     * @throws IllegalArgumentException If the cache has no index with the given name.
     */
    public Optional<T> findAnyByIndex(String indexName, Object key) {
        Index<Object, T> index = indexes.get(indexName).getOrElseThrow(
                () -> new IllegalArgumentException("No index with given name (" + indexName + ") found"));
        return index.findAny(key);
    }

    /**
     * Gets a set with all elements in the cache that have the given key.
     *
     * <p>This method has an effective time complexity of {@code O(1)}.
     *
     * @param indexName The name of the index.
     * @param key The key of the elements.
     * @return A set with all the elements that have the given key.
     * @throws IllegalArgumentException If the cache has no index with the given name.
     */
    public Set<T> findByIndex(String indexName, Object key) {
        Index<Object, T> index = indexes.get(indexName).getOrElseThrow(
                () -> new IllegalArgumentException("No index with given name (" + indexName + ") found"));
        return index.find(key);
    }

}

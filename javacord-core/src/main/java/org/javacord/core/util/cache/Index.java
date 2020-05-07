package org.javacord.core.util.cache;

import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.Map;
import io.vavr.collection.Set;

import java.util.Optional;
import java.util.function.Function;

/**
 * An immutable index.
 *
 * @param <K> The type of the key.
 * @param <E> The type of the elements.
 */
public class Index<K, E> {

    /**
     * A function that maps an element to its key.
     */
    private final Function<E, K> keyMapper;

    /**
     * A map that contains the elements by their key.
     */
    private final Map<K, Set<E>> elementsByKey;

    /**
     * A map that contains the element as its key and the key of the element when it was added to
     * this index as its value.
     *
     * <p>It allows for a reverse lookup of index keys without having to call the {@code keyMapper}.
     */
    private final Map<E, K> keyByElement;

    /**
     * Creates a new index.
     *
     * @param keyMapper A function to map elements to their key.
     */
    public Index(Function<E, K> keyMapper) {
        this(keyMapper, HashMap.empty(), HashMap.empty());
    }

    /**
     * Creates a new index.
     *
     * @param keyMapper A function to map elements to their key.
     * @param elementsByKey The elements by their key.
     * @param keyByElement The key by the element.
     */
    private Index(Function<E, K> keyMapper, Map<K, Set<E>> elementsByKey, Map<E, K> keyByElement) {
        this.keyMapper = keyMapper;
        this.elementsByKey = elementsByKey;
        this.keyByElement = keyByElement;
    }

    /**
     * Gets the mapping function to map elements to their key.
     *
     * <p>Compound indexes can easily be achieved by using a {@link io.vavr.Tuple} or {@link io.vavr.collection.Seq}
     * with all keys as the return value of the mapping function.
     *
     * @return The mapping function.
     */
    public Function<E, K> getKeyMapper() {
        return keyMapper;
    }

    /**
     * Adds an element to the index.
     *
     * <p>This method has an effective time complexity of {@code O(1)}.
     *
     * @param element The element to add.
     * @return The new index with the added element.
     */
    public Index<K, E> addElement(E element) {
        K key = keyMapper.apply(element);
        if (key == null) {
            return this;
        }
        Set<E> elements = find(key);
        if (elements.contains(element)) {
            return this;
        }
        if (keyByElement.containsKey(element)) {
            throw new IllegalStateException("The given element is already in the index with a different key");
        }
        Map<K, Set<E>> newElementsByKey = elementsByKey.put(key, elements.add(element));
        Map<E, K> newKeyByElement = keyByElement.put(element, key);
        return new Index<>(keyMapper, newElementsByKey, newKeyByElement);
    }

    /**
     * Removes an element from the index.
     *
     * <p>This method has an effective time complexity of {@code O(1)}.
     *
     * @param element The element to remove.
     * @return The new index with the element removed.
     */
    public Index<K, E> removeElement(E element) {
        K key = keyByElement.getOrElse(element, null);
        if (key == null) {
            return this;
        }
        Set<E> elements = find(key);
        if (!elements.contains(element)) {
            return this;
        }
        Map<K, Set<E>> newElementsByKey = elementsByKey.put(key, elements.remove(element));
        Map<E, K> newKeyByElement = keyByElement.remove(element);
        return new Index<>(keyMapper, newElementsByKey, newKeyByElement);
    }

    /**
     * Gets a set with all elements with the given key.
     *
     * <p>This method has an effective time complexity of {@code O(1)}.
     *
     * @param key The key of the elements.
     * @return The elements with the given key.
     */
    public Set<E> find(K key) {
        return elementsByKey.getOrElse(key, HashSet.empty());
    }

    /**
     * Gets any element with the given key.
     *
     * <p>This method has an effective time complexity of {@code O(1)}.
     *
     * @param key The key of the element.
     * @return An element with the given key.
     */
    public Optional<E> findAny(K key) {
        return find(key).headOption().toJavaOptional();
    }
}

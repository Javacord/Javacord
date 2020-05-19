package org.javacord.core.util;

import java.util.Set;

/**
 * Maps the immutable collections to unmodifiable Java collections.
 */
public class ImmutableToJavaMapper {

    private ImmutableToJavaMapper() {
        throw new UnsupportedOperationException();
    }

    /**
     * Maps the given immutable Vavr set to an unmodifiable java set.
     *
     * @param set The immutable Vavr set.
     * @param <J> The type of the unmodifiable java set.
     *            The caller must make sure that the given Vavr set only contain elements of this type.
     * @param <V> The type of the immutable Vavr set.
     * @return The unmodifiable java set.
     */
    @SuppressWarnings("unchecked")
    public static <J extends V, V> Set<J> mapToJava(io.vavr.collection.Set<V> set) {
        return (Set<J>) new VavrSetView<>(set, true);
    }

}

package org.javacord.api.util;

import java.util.Objects;
import java.util.Optional;

/**
 * An object that may be specialized further and recast as a subclass.
 * @param <S> The base type of which subclasses are eligible to be cast.
 */
public interface Specializable<S> {

    /**
     * Get this instance as a desired subtype.
     *
     * <p>If the instance is not castable to the specified type, the {@code Optional} will be empty.
     *
     * @param type The type as which to obtain this instance.
     * @param <T> The desired type.
     * @return Returns an {@code Optional} of this instance if it could be cast, otherwise an empty result.
     * @throws NullPointerException If the type is {@code null}.
     */
    default <T extends S> Optional<T> as(Class<T> type) {
        Objects.requireNonNull(type, "type must not be null");
        return type.isAssignableFrom(this.getClass()) ? Optional.of(type.cast(this)) : Optional.empty();
    }
}

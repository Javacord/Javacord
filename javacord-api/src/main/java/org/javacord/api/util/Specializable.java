package org.javacord.api.util;

import java.util.Objects;
import java.util.Optional;

/**
 * This interface is used to provide a more stream lined way to interact with the instance as sub types.
 * @param <S> Base instance type.
 */
public interface Specializable<S> {

    /**
     * Get an {@link Optional} instance of an instance as specified type.
     * If the instance is not castable to the specified type, the {@link Optional} will be empty.
     * @param type Specified type, this instance will be tried to casted to.
     * @param instance Instance which will be tried to get casted.
     * @param <T> Specified type.
     * @return Returns an {@link Optional} of the specified type if the instance is castable, if not it returns an empty
     *         {@link Optional}.
     * @throws NullPointerException Throws a {@link NullPointerException} if either {@code type} or {@code instance} is
     *                              {@code null}.
     * @see java.util.Optional
     */
    static <T> Optional<T> tryToSpecify(Class<T> type, Object instance) {
        Objects.requireNonNull(type, "type must not be null");
        Objects.requireNonNull(instance, "instance must not be null");

        return type.isAssignableFrom(instance.getClass()) ? Optional.of((T) instance) : Optional.empty();
    }

    /**
     * Get an {@link Optional} of this instance as specified type.
     * If the instance is not castable to the specified type, the {@link Optional} will be empty.
     * @param type Specified type, this instance will be tried to casted to.
     * @param <T> Specified type.
     * @return Returns an {@link Optional} of the specified type if the instance is castable, if not it returns an empty
     *         {@link Optional}.
     * @see Specializable#tryToSpecify(Class, Object)
     */
    default <T extends S> Optional<T> as(Class<T> type) {
        Objects.requireNonNull(type, "type must not be null");

        return tryToSpecify(type, this);
    }
}

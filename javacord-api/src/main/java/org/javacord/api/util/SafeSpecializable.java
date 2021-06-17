package org.javacord.api.util;

import java.util.Optional;

/**
 * An object that may be specialized further and recast as a subclass.
 * As opposed to {@link Specializable}, it will not throw a {@code NullPointerException} if the passed type is null.
 *
 * @param <S> The base type of which subclasses are eligible to be cast.
 */
public interface SafeSpecializable<S> extends Specializable<S> {

    /**
     * Get this instance as a desired subtype.
     *
     * <p>If the instance is not castable to the specified type, the {@code Optional} will be empty.
     *
     * <p>If the passed type is {@code null}, an empty {@code Optional} will be returned.
     *
     * @param type The type as which to obtain this instance.
     * @param <T>  The desired type.
     * @return Returns an {@code Optional} of this instance if it could be cast, otherwise an empty result.
     */
    @Override
    default <T extends S> Optional<T> as(Class<T> type) {
        if (type == null) {
            return Optional.empty();
        }
        return Specializable.super.as(type);
    }
}

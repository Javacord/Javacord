package org.javacord.api.entity.auditlog;

import java.util.Optional;

/**
 * This class represents an audit log change.
 *
 * @param <T> The type of the changed param.
 */
public interface AuditLogChange<T> {

    /**
     * Gets the type of the change.
     *
     * @return The type of the change.
     */
    AuditLogChangeType getType();

    /**
     * Gets the old value of the change.
     *
     * @return The old value of the change.
     */
    Optional<T> getOldValue();

    /**
     * Gets the new value of the change.
     *
     * @return The new value of the change.
     */
    Optional<T> getNewValue();

}

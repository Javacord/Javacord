package org.javacord.core.entity.auditlog;

import org.javacord.api.entity.auditlog.AuditLogChange;
import org.javacord.api.entity.auditlog.AuditLogChangeType;

import java.util.Optional;

/**
 * The implementation of {@link AuditLogChange}.
 *
 * @param <T> The type of the changed param.
 */
public class AuditLogChangeImpl<T> implements AuditLogChange<T> {

    private final AuditLogChangeType type;
    private final T oldValue;
    private final T newValue;

    /**
     * Creates a new audit log change.
     *
     * @param type The type of the change.
     * @param oldValue The old value.
     * @param newValue The new value.
     */
    public AuditLogChangeImpl(AuditLogChangeType type, T oldValue, T newValue) {
        this.type = type;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Override
    public AuditLogChangeType getType() {
        return type;
    }

    @Override
    public Optional<T> getOldValue() {
        return Optional.ofNullable(oldValue);
    }

    @Override
    public Optional<T> getNewValue() {
        return Optional.ofNullable(newValue);
    }
}

package org.javacord.api.entity.auditlog;

import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.user.User;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * The interface represents an audit log entry.
 */
public interface AuditLogEntry extends DiscordEntity {

    /**
     * Gets the audit log this entry belongs to.
     *
     * @return The audit log this entry belong to.
     */
    AuditLog getAuditLog();

    /**
     * Gets the user who made the changes.
     *
     * @return The user who made the changes.
     */
    CompletableFuture<User> getUser();

    /**
     * Gets the reason for the change.
     *
     * @return The reason for the change.
     */
    Optional<String> getReason();

    /**
     * Gets the type of the entry.
     *
     * @return The type of the entry.
     */
    AuditLogActionType getType();

    /**
     * Gets the target of the entry.
     *
     * @return The target of the entry.
     */
    Optional<AuditLogEntryTarget> getTarget();

    /**
     * Gets a list with all changes.
     *
     * @return A list with all changes.
     */
    List<AuditLogChange<?>> getChanges();

    /**
     * Gets the audit log before this entry.
     *
     * @param limit The maximum amount of audit log entries.
     * @return The audit log.
     */
    default CompletableFuture<AuditLog> getAuditLogBefore(int limit) {
        return getAuditLog().getServer().getAuditLogBefore(limit, this);
    }

    /**
     * Gets the audit log before this entry.
     *
     * @param limit The maximum amount of audit log entries.
     * @param type The action type of the audit log.
     * @return The audit log.
     */
    default CompletableFuture<AuditLog> getAuditLogBefore(int limit, AuditLogActionType type) {
        return getAuditLog().getServer().getAuditLogBefore(limit, this, type);
    }

}

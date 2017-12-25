package de.btobastian.javacord.entities.auditlog.impl;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.auditlog.AuditLogEntry;
import de.btobastian.javacord.entities.auditlog.AuditLogEntryTarget;

;

/**
 * The implementation of {@link AuditLogEntryTarget}.
 */
public class ImplAuditLogEntryTarget implements AuditLogEntryTarget {

    /**
     * The id of the target.
     */
    private final long id;

    /**
     * The audit log entry, this target belongs to.
     */
    private final AuditLogEntry auditLogEntry;

    /**
     * Creates a new audit log entry target.
     *
     * @param id The id of the target.
     * @param auditLogEntry audit log entry this target belongs to.
     */
    public ImplAuditLogEntryTarget(long id, AuditLogEntry auditLogEntry) {
        this.id = id;
        this.auditLogEntry = auditLogEntry;
    }

    @Override
    public DiscordApi getApi() {
        return auditLogEntry.getApi();
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public AuditLogEntry getAuditLogEntry() {
        return auditLogEntry;
    }

}

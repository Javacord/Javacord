package de.btobastian.javacord.entities.auditlog.impl;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.Webhook;
import de.btobastian.javacord.entities.auditlog.AuditLog;
import de.btobastian.javacord.entities.auditlog.AuditLogEntry;
import de.btobastian.javacord.entities.impl.ImplWebhook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The implementation of {@link AuditLog}.
 */
public class ImplAuditLog implements AuditLog {

    /**
     * The discord api instance.
     */
    private final DiscordApi api;

    /**
     * A collection with all involved webhooks.
     */
    private final Collection<Webhook> involvedWebhooks = new ArrayList<>();

    /**
     * A collection with all involved users.
     */
    private final Collection<User> involvedUsers = new ArrayList<>();

    /**
     * A list with all entries.
     */
    private final List<AuditLogEntry> entries = new ArrayList<>();

    /**
     * Creates a new audit log.
     *
     * @param api The discord api instance.
     * @param data The data of the audit log.
     */
    public ImplAuditLog(DiscordApi api, JsonNode data) {
        this.api = api;
        for (JsonNode webhook : data.get("webhooks")) {
            involvedWebhooks.add(new ImplWebhook(api, webhook));
        }
        for (JsonNode user : data.get("users")) {
            involvedUsers.add(((ImplDiscordApi) api).getOrCreateUser(user));
        }
        for (JsonNode entry : data.get("audit_log_entries")) {
            entries.add(new ImplAuditLogEntry(this, entry));
        }
    }

    @Override
    public DiscordApi getApi() {
        return api;
    }

    @Override
    public Collection<Webhook> getInvolvedWebhooks() {
        return involvedWebhooks;
    }

    @Override
    public Collection<User> getInvolvedUsers() {
        return involvedUsers;
    }

    @Override
    public List<AuditLogEntry> getEntries() {
        return entries;
    }
}

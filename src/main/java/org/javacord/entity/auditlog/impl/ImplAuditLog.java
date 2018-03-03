package org.javacord.entity.auditlog.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.DiscordApi;
import org.javacord.ImplDiscordApi;
import org.javacord.entity.auditlog.AuditLog;
import org.javacord.entity.auditlog.AuditLogEntry;
import org.javacord.entity.user.User;
import org.javacord.entity.webhook.Webhook;
import org.javacord.entity.webhook.impl.ImplWebhook;
import org.javacord.entity.auditlog.AuditLog;
import org.javacord.entity.auditlog.AuditLogEntry;
import org.javacord.entity.user.User;
import org.javacord.entity.webhook.Webhook;
import org.javacord.entity.webhook.impl.ImplWebhook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
        return Collections.unmodifiableCollection(involvedWebhooks);
    }

    @Override
    public Collection<User> getInvolvedUsers() {
        return Collections.unmodifiableCollection(involvedUsers);
    }

    @Override
    public List<AuditLogEntry> getEntries() {
        return Collections.unmodifiableList(entries);
    }
}

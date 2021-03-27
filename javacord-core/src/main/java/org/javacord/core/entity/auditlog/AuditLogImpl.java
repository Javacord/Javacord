package org.javacord.core.entity.auditlog;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.auditlog.AuditLog;
import org.javacord.api.entity.auditlog.AuditLogEntry;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.entity.webhook.Webhook;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.entity.user.MemberImpl;
import org.javacord.core.entity.user.UserImpl;
import org.javacord.core.entity.webhook.WebhookImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * The implementation of {@link AuditLog}.
 */
public class AuditLogImpl implements AuditLog {

    /**
     * The discord api instance.
     */
    private final DiscordApi api;

    /**
     * The server of the audit log.
     */
    private final Server server;

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
     * @param server The server of the audit log.
     */
    public AuditLogImpl(Server server) {
        this.api = server.getApi();
        this.server = server;
    }

    /**
     * Adds entries to the audit log.
     *
     * @param data The json data of the audit log.
     */
    public void addEntries(JsonNode data) {
        for (JsonNode webhookJson : data.get("webhooks")) {
            boolean alreadyAdded = involvedWebhooks.stream()
                    .anyMatch(webhook -> webhook.getId() == webhookJson.get("id").asLong());
            if (!alreadyAdded) {
                involvedWebhooks.add(WebhookImpl.createWebhook(api, webhookJson));
            }
        }
        for (JsonNode userJson : data.get("users")) {
            boolean alreadyAdded = involvedUsers.stream()
                    .anyMatch(user -> user.getId() == userJson.get("id").asLong());
            if (!alreadyAdded) {
                involvedUsers.add(new UserImpl((DiscordApiImpl) api, userJson, (MemberImpl) null, (ServerImpl) server));
            }
        }
        for (JsonNode entry : data.get("audit_log_entries")) {
            entries.add(new AuditLogEntryImpl(this, entry));
        }
    }

    @Override
    public DiscordApi getApi() {
        return api;
    }

    @Override
    public Server getServer() {
        return server;
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

package org.javacord.api.entity.auditlog;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.entity.webhook.Webhook;

import java.util.List;
import java.util.Set;

/**
 * The class represents an audit log.
 */
public interface AuditLog {

    /**
     * Gets the discord api instance.
     *
     * @return The discord api instance.
     */
    DiscordApi getApi();

    /**
     * Gets the server of the audit log.
     *
     * @return The server of the audit log.
     */
    Server getServer();

    /**
     * Gets a set with all webhooks, which can be found in this audit log.
     *
     * @return A set with all webhooks, which can be found in this audit log.
     */
    Set<Webhook> getInvolvedWebhooks();

    /**
     * Gets a set with all users, which can be found in this audit log.
     *
     * @return A set with all users, which can be found in this audit log.
     */
    Set<User> getInvolvedUsers();

    /**
     * Gets a list with all entries.
     *
     * @return A list with all entries.
     */
    List<AuditLogEntry> getEntries();

}

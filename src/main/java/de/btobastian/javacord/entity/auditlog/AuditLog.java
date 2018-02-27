package de.btobastian.javacord.entity.auditlog;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entity.user.User;
import de.btobastian.javacord.entity.webhook.Webhook;

import java.util.Collection;
import java.util.List;

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
     * Gets a collection with all webhooks, which can be found in this audit log.
     *
     * @return A collection with all webhooks, which can be found in this audit log.
     */
    Collection<Webhook> getInvolvedWebhooks();

    /**
     * Gets a collection with all users, which can be found in this audit log.
     *
     * @return A collection with all users, which can be found in this audit log.
     */
    Collection<User> getInvolvedUsers();

    /**
     * Gets a list with all entries.
     *
     * @return A list with all entries.
     */
    List<AuditLogEntry> getEntries();

}

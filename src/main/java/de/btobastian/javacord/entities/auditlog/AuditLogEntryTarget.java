package de.btobastian.javacord.entities.auditlog;

import de.btobastian.javacord.entities.DiscordEntity;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.Webhook;
import de.btobastian.javacord.entities.channels.ServerChannel;

import java.util.Optional;

/**
 * An audit log entry target.
 */
public interface AuditLogEntryTarget extends DiscordEntity {

    /**
     * Gets the audit log entry this target belongs to.
     *
     * @return The audit log entry this target belongs to.
     */
    AuditLogEntry getAuditLogEntry();

    /**
     * Gets the target as user.
     *
     * @return The target as user.
     */
    default Optional<User> asUser() {
        return getApi().getUserById(getId());
    }

    /**
     * Gets the target as server.
     *
     * @return The target as server.
     */
    default Optional<Server> asServer() {
        return getApi().getServerById(getId());
    }

    /**
     * Gets the target as channel.
     *
     * @return The target as channel.
     */
    default Optional<ServerChannel> asChannel() {
        return getApi().getServerChannelById(getId());
    }

    /**
     * Gets the target as webhook.
     *
     * @return The target as webhook.
     */
    default Optional<Webhook> asWebhook() {
        for (Webhook webhook : getAuditLogEntry().getAuditLog().getInvolvedWebhooks()) {
            if (webhook.getId() == getId()) {
                return Optional.of(webhook);
            }
        }
        return Optional.empty();
    }

}

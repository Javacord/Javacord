package org.javacord.entity.auditlog;

import org.javacord.entity.DiscordEntity;
import org.javacord.entity.channel.ServerChannel;
import org.javacord.entity.server.Server;
import org.javacord.entity.user.User;
import org.javacord.entity.webhook.Webhook;
import org.javacord.entity.DiscordEntity;
import org.javacord.entity.channel.ServerChannel;
import org.javacord.entity.server.Server;
import org.javacord.entity.user.User;
import org.javacord.entity.webhook.Webhook;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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
    default CompletableFuture<User> asUser() {
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

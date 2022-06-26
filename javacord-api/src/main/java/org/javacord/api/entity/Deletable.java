package org.javacord.api.entity;

import org.javacord.api.DiscordApi;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * An entity that can be deleted.
 */
public interface Deletable {

    /**
     * Gets the DiscordApi managing this entity.
     *
     * @return The api instance.
     */
    DiscordApi getApi();

    /**
     * Deletes the entity.
     *
     * @return A future to tell if the deletion was successful.
     */
    default CompletableFuture<Void> delete() {
        return delete(null);
    }

    /**
     * Deletes the entity.
     *
     * @param reason The audit log reason for the deletion.
     * @return A future to tell if the deletion was successful.
     */
    CompletableFuture<Void> delete(String reason);

    /**
     * Deletes the entity after the given time.
     *
     * <p><b>Caution:</b> If the bot shuts down before the scheduled time, the entity will not be deleted.</p>
     *
     * @param duration The duration.
     * @param unit The unit for the duration.
     * @return A future that completes when the entity has been deleted.
     */
    default CompletableFuture<Void> deleteAfter(long duration, TimeUnit unit) {
        return deleteAfter(duration, unit, "Scheduled deletion");
    }

    /**
     * Deletes the entity after the given time.
     *
     * <p><b>Caution:</b> If the bot shuts down before the scheduled time, the entity will not be deleted.</p>
     *
     * @param duration The duration.
     * @param unit The unit for the duration.
     * @param auditLogReason The reason to log for the audit log.
     * @return A future that completes when the entity has been deleted.
     */
    default CompletableFuture<Void> deleteAfter(long duration, TimeUnit unit, String auditLogReason) {
        return getApi().getThreadPool().runAfter(() -> this.delete(auditLogReason), duration, unit);
    }

    /**
     * Deletes the entity after the given time.
     *
     * <p><b>Caution:</b> If the bot shuts down before the scheduled time, the entity will not be deleted.</p>
     *
     * @param duration The duration.
     * @return A future that completes when the entity has been deleted.
     */
    default CompletableFuture<Void> deleteAfter(Duration duration) {
        return deleteAfter(duration.toMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * Deletes the entity after the given time.
     *
     * <p><b>Caution:</b> If the bot shuts down before the scheduled time, the entity will not be deleted.</p>
     *
     * @param duration The duration.
     * @param auditLogReason The reason to log for the audit log.
     * @return A future that completes when the entity has been deleted.
     */
    default CompletableFuture<Void> deleteAfter(Duration duration, String auditLogReason) {
        return deleteAfter(duration.toMillis(), TimeUnit.MILLISECONDS, auditLogReason);
    }

}

package org.javacord.api.entity.server.scheduledevent.internal;

import org.javacord.api.entity.server.scheduledevent.ServerScheduledEvent;
import org.javacord.api.entity.server.scheduledevent.ServerScheduledEventBuilder;
import org.javacord.api.entity.server.scheduledevent.ServerScheduledEventPrivacyLevel;
import org.javacord.api.entity.server.scheduledevent.ServerScheduledEventType;
import java.io.File;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

/**
 * This class is internally used by the {@link ServerScheduledEventBuilder} to create server scheduled events.
 * You usually don't want to interact with this object.
 */
public interface ServerScheduledEventBuilderDelegate {

    /**
     * Sets the reason for the creation. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     */
    void setAuditLogReason(String reason);

    /**
     * Sets the name of the server scheduled event.
     *
     * @param name The name of the server scheduled event.
     */
    void setName(String name);

    /**
     * Sets the description of the server scheduled event.
     *
     * @param description The description of the server scheduled event.
     */
    void setDescription(String description);

    /**
     * Sets the scheduled start time of the server scheduled event.
     *
     * @param scheduledStartTime The scheduled start time of the server scheduled event.
     */
    void setScheduledStartTime(Instant scheduledStartTime);

    /**
     * Sets the scheduled end time of the server scheduled event.
     *
     * @param scheduledEndTime The scheduled end time of the server scheduled event.
     */
    void setScheduledEndTime(Instant scheduledEndTime);

    /**
     * Sets the privacy level of the server scheduled event.
     *
     * @param serverScheduledEventPrivacyLevel The privacy level of the server scheduled event.
     */
    void setPrivacyLevel(ServerScheduledEventPrivacyLevel serverScheduledEventPrivacyLevel);

    /**
     * Sets the channel id of the server scheduled event.
     *
     * @param channelId The channel id of the server scheduled event.
     */
    void setChannelId(Long channelId);

    /**
     * Sets the entity type of the server scheduled event.
     *
     * @param serverScheduledEventType The entity type of the server scheduled event.
     */
    void setEntityType(ServerScheduledEventType serverScheduledEventType);

    /**
     * Sets the entity metadata location of the server scheduled event.
     *
     * @param entityMetadataLocation The entity metadata location of the server scheduled event.
     */
    void setEntityMetadataLocation(String entityMetadataLocation);

    /**
     * Sets the image of the server scheduled event.
     *
     * @param image The image of the server scheduled event.
     */
    void setImage(File image);

    /**
     * Creates the server scheduled event.
     *
     * @return The created server scheduled event.
     */
    CompletableFuture<ServerScheduledEvent> create();
}

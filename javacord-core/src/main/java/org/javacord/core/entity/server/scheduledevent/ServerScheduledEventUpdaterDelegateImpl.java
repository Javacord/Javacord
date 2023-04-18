package org.javacord.core.entity.server.scheduledevent;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.server.scheduledevent.ServerScheduledEvent;
import org.javacord.api.entity.server.scheduledevent.ServerScheduledEventPrivacyLevel;
import org.javacord.api.entity.server.scheduledevent.ServerScheduledEventStatus;
import org.javacord.api.entity.server.scheduledevent.ServerScheduledEventType;
import org.javacord.api.entity.server.scheduledevent.internal.ServerScheduledEventUpdaterDelegate;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link ServerScheduledEventUpdaterDelegate}.
 */
public class ServerScheduledEventUpdaterDelegateImpl implements ServerScheduledEventUpdaterDelegate {

    /**
     * The server scheduled event to update.
     */
    private final ServerScheduledEvent scheduledEvent;

    /**
     * The reason for the update.
     */
    private String reason = null;

    /**
     * The name of the server scheduled event.
     */
    private String name = null;

    /**
     * The description of the server scheduled event.
     */
    private String description = null;

    /**
     * Whether to patch the description of the server scheduled event.
     */
    private boolean patchDescription = false;

    /**
     * The scheduled start time of the server scheduled event.
     */
    private Instant scheduledStartTime = null;

    /**
     * The scheduled end time of the server scheduled event.
     */
    private Instant scheduledEndTime = null;

    /**
     * The privacy level of the server scheduled event.
     */
    private ServerScheduledEventPrivacyLevel serverScheduledEventPrivacyLevel = null;

    /**
     * The channel id of the server scheduled event.
     */
    private Long channelId = null;

    /**
     * Whether to patch the channel id of the server scheduled event.
     */
    private boolean patchChannelId = false;

    /**
     * The entity type of the server scheduled event.
     */
    private ServerScheduledEventType serverScheduledEventType = null;

    /**
     * The entity metadata location of the server scheduled event.
     */
    private String entityMetadataLocation = null;

    /**
     * Whether to patch the entity metadata location of the server scheduled event.
     */
    private boolean patchEntityMetadataLocation = false;

    /**
     * The image of the server scheduled event.
     */
    private String image = null;

    /**
     * The status of the server scheduled event.
     */
    private ServerScheduledEventStatus serverScheduledEventStatus = null;

    /**
     * Creates a new server scheduled event updater delegate.
     *
     * @param scheduledEvent The server scheduled event to update.
     */
    public ServerScheduledEventUpdaterDelegateImpl(ServerScheduledEvent scheduledEvent) {
        this.scheduledEvent = scheduledEvent;
    }

    @Override
    public void setAuditLogReason(String reason) {
        this.reason = reason;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
        patchDescription = true;
    }

    @Override
    public void setScheduledStartTime(Instant scheduledStartTime) {
        this.scheduledStartTime = scheduledStartTime;
    }

    @Override
    public void setScheduledEndTime(Instant scheduledEndTime) {
        this.scheduledEndTime = scheduledEndTime;
    }

    @Override
    public void setPrivacyLevel(ServerScheduledEventPrivacyLevel serverScheduledEventPrivacyLevel) {
        this.serverScheduledEventPrivacyLevel = serverScheduledEventPrivacyLevel;
    }

    @Override
    public void setChannelId(Long channelId) {
        this.channelId = channelId;
        patchChannelId = true;
    }

    @Override
    public void setEntityType(ServerScheduledEventType serverScheduledEventType) {
        this.serverScheduledEventType = serverScheduledEventType;
    }

    @Override
    public void setEntityMetadataLocation(String entityMetadataLocation) {
        this.entityMetadataLocation = entityMetadataLocation;
        patchEntityMetadataLocation = true;
    }

    @Override
    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public void setEventStatus(ServerScheduledEventStatus serverScheduledEventStatus) {
        this.serverScheduledEventStatus = serverScheduledEventStatus;
    }

    @Override
    public CompletableFuture<Void> update() {
        boolean patchScheduledEvent = false;
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        if (null != description) {
            body.put("description", description);
            patchScheduledEvent = true;
        }
        if (null == description && patchDescription) {
            body.putNull("description");
            patchScheduledEvent = true;
        }

        if (null != scheduledStartTime) {
            body.put("scheduled_start_time", scheduledStartTime.toString());
            patchScheduledEvent = true;
        }

        if (null != scheduledEndTime) {
            body.put("scheduled_end_time", scheduledEndTime.toString());
            patchScheduledEvent = true;
        }

        if (null != serverScheduledEventPrivacyLevel) {
            body.put("privacy_level", serverScheduledEventPrivacyLevel.getValue());
            patchScheduledEvent = true;
        }

        if (null != channelId) {
            body.put("channel_id", channelId);
            patchScheduledEvent = true;
        }

        if (null == channelId && patchChannelId) {
            body.putNull("channel_id");
            patchScheduledEvent = true;
        }

        if (null != serverScheduledEventType) {
            body.put("entity_type", serverScheduledEventType.getValue());
            patchScheduledEvent = true;
        }

        if (null != entityMetadataLocation) {
            ObjectNode node = body.putObject("entity_metadata");
            node.put("location", entityMetadataLocation);
            patchScheduledEvent = true;
        }

        if (null == entityMetadataLocation && patchEntityMetadataLocation) {
            body.putNull("entity_metadata");
            patchScheduledEvent = true;
        }

        if (null != image) {
            body.put("image", image);
            patchScheduledEvent = true;
        }

        if (null != serverScheduledEventStatus) {
            body.put("status", serverScheduledEventStatus.getValue());
            patchScheduledEvent = true;
        }

        if (patchScheduledEvent) {
            return new RestRequest<Void>(scheduledEvent.getApi(), RestMethod.PATCH, RestEndpoint.SCHEDULED_EVENTS)
                    .setUrlParameters(scheduledEvent.getServer().getIdAsString(), scheduledEvent.getIdAsString())
                    .setBody(body)
                    .setAuditLogReason(reason)
                    .execute(result -> null);
        } else {
            return CompletableFuture.completedFuture(null);
        }
    }

}

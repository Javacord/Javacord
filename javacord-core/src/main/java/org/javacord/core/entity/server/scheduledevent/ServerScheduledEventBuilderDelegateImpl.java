package org.javacord.core.entity.server.scheduledevent;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.server.scheduledevent.ServerScheduledEvent;
import org.javacord.api.entity.server.scheduledevent.ServerScheduledEventPrivacyLevel;
import org.javacord.api.entity.server.scheduledevent.ServerScheduledEventType;
import org.javacord.api.entity.server.scheduledevent.internal.ServerScheduledEventBuilderDelegate;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.util.io.FileUtils;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link ServerScheduledEventBuilderDelegate}.
 */
public class ServerScheduledEventBuilderDelegateImpl implements ServerScheduledEventBuilderDelegate {

    /**
     * The server of the server scheduled event.
     */
    private final ServerImpl server;

    /**
     * The reason for the creation.
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
     * The entity type of the server scheduled event.
     */
    private ServerScheduledEventType serverScheduledEventType = null;

    /**
     * The entity metadata location of the server scheduled event.
     */
    private String entityMetadataLocation = null;

    /**
     * The image of the server scheduled event.
     */
    private File image = null;

    /**
     * Creates a new server scheduled event builder delegate for the given server.
     *
     * @param server The server for which the server scheduled event should be created.
     */
    public ServerScheduledEventBuilderDelegateImpl(ServerImpl server) {
        this.server = server;
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
    }

    @Override
    public void setEntityType(ServerScheduledEventType serverScheduledEventType) {
        this.serverScheduledEventType = serverScheduledEventType;
    }

    @Override
    public void setEntityMetadataLocation(String entityMetadataLocation) {
        this.entityMetadataLocation = entityMetadataLocation;
    }

    @Override
    public void setImage(File image) {
        this.image = image;
    }

    @Override
    public CompletableFuture<ServerScheduledEvent> create() {
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        if (name != null) {
            body.put("name", name);
        }

        if (description != null) {
            body.put("description", description);
        }

        if (scheduledStartTime != null) {
            body.put("scheduled_start_time", scheduledStartTime.toString());
        }

        if (scheduledEndTime != null) {
            body.put("scheduled_end_time", scheduledEndTime.toString());
        }

        if (serverScheduledEventPrivacyLevel != null) {
            body.put("privacy_level", serverScheduledEventPrivacyLevel.getValue());
        }

        if (channelId != null) {
            body.put("channel_id", channelId);
        }

        if (serverScheduledEventType != null) {
            body.put("entity_type", serverScheduledEventType.getValue());
        }

        if (entityMetadataLocation != null) {
            ObjectNode node = body.putObject("entity_metadata");
            node.put("location", entityMetadataLocation);
        }

        if (image != null) {
            try {
                String dataUri = FileUtils.convertFileToDataUri(image);
                body.put("image", dataUri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return new RestRequest<ServerScheduledEvent>(server.getApi(), RestMethod.POST,
                RestEndpoint.SCHEDULED_EVENTS).setUrlParameters(server.getIdAsString()).setBody(body)
                .setAuditLogReason(reason).execute(
                        result -> new ServerScheduledEventImpl((DiscordApiImpl) server.getApi(), result.getJsonBody()));
    }

}

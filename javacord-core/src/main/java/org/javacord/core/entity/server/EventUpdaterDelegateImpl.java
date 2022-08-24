package org.javacord.core.entity.server;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.server.*;
import org.javacord.api.entity.server.internal.EventUpdaterDelegate;
import org.javacord.core.util.FileContainer;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

public class EventUpdaterDelegateImpl implements EventUpdaterDelegate {
    private final Server server;
    private final ScheduledEvent event;

    private Long channelId;
    private String name;
    private PrivacyLevel privacyLevel;
    private Instant startTime;
    private Instant endTime;
    private String description;
    private ScheduledEventType eventType;
    private String location;
    private FileContainer image;
    private ScheduledEventStatus status;
    private String reason;

    public EventUpdaterDelegateImpl(ServerImpl server, ScheduledEvent event) {
        this.server = server;
        this.event = event;
    }

    @Override
    public void setAuditLogReason(String reason) {
        this.reason = reason;
    }

    @Override
    public void setChannel(long channelId) {
        this.channelId = channelId;
    }

    @Override
    public void setChannel(Channel channel) {
        this.channelId = channel.getId();
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setPrivacyLevel(PrivacyLevel privacyLevel) {
        this.privacyLevel = privacyLevel;
    }

    @Override
    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    @Override
    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setType(ScheduledEventType eventType) {
        this.eventType = eventType;
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public void setStatus(ScheduledEventStatus status) {
        this.status = status;
    }

    @Override
    public void setImage(BufferedImage image) {
        this.image = (image == null) ? null : new FileContainer(image, "png");
    }

    @Override
    public void setImage(BufferedImage image, String fileType) {
        this.image = (image == null) ? null : new FileContainer(image, fileType);
    }

    @Override
    public void setImage(File image) {
        this.image = (image == null) ? null : new FileContainer(image);
    }

    @Override
    public void setImage(URL image) {
        this.image = (image == null) ? null : new FileContainer(image);
    }

    @Override
    public void setImage(byte[] image) {
        this.image = (image == null) ? null : new FileContainer(image, "png");
    }

    @Override
    public void setImage(byte[] image, String fileType) {
        this.image = (image == null) ? null : new FileContainer(image, fileType);
    }

    @Override
    public void setImage(InputStream image) {
        this.image = (image == null) ? null : new FileContainer(image, "png");
    }

    @Override
    public void setImage(InputStream image, String fileType) {
        this.image = (image == null) ? null : new FileContainer(image, fileType);
    }

    @Override
    public CompletableFuture<ScheduledEvent> update() {
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        if (name != null) {
            body.put("name", name);
        }
        if (channelId != null) {
            body.put("channel_id", channelId.longValue());
        }
        if (location != null) { // we put this into the entity_metadata field, but it only for now has the location field as of August 2022.
            body.putObject("entity_metadata").put("location", location);
        }
        if (privacyLevel != null) {
            body.put("privacy_level", privacyLevel.getId());
        }
        if (startTime != null) {
            body.put("start_time", DateTimeFormatter.ISO_INSTANT.format(startTime));
        }
        if (endTime != null) {
            body.put("end_time", DateTimeFormatter.ISO_INSTANT.format(endTime));
        }
        if (description != null) {
            body.put("description", description);
        }
        if (eventType != null) {
            body.put("event_type", eventType.getId());
        }
        if (status != null) {
            body.put("status", status.getId());
        }
        if (image != null) {
            return image.asByteArray(server.getApi()).thenAccept(bytes -> {
                String base64Icon = "data:image/" + image.getFileType() + ";base64,"
                        + Base64.getEncoder().encodeToString(bytes);
                body.put("image", base64Icon);
            }).thenCompose(aVoid -> new RestRequest<ScheduledEvent>(server.getApi(), RestMethod.PATCH, RestEndpoint.EVENT_UPDATE)
                    .setUrlParameters(String.valueOf(event.getServer().getId()), String.valueOf(event.getServer().getId()))
                    .setBody(body)
                    .setAuditLogReason(reason)
                    .execute(result -> new ScheduledEventImpl(server.getApi(), (ServerImpl) server, result.getJsonBody())));
        }
        return new RestRequest<ScheduledEvent>(server.getApi(), RestMethod.PATCH, RestEndpoint.EVENT_UPDATE)
                .setUrlParameters(String.valueOf(event.getServer().getId()), String.valueOf(event.getServer().getId()))
                .setBody(body)
                .setAuditLogReason(reason)
                .execute(result -> new ScheduledEventImpl(server.getApi(), (ServerImpl) server, result.getJsonBody()));
    }
}

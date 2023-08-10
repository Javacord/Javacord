package org.javacord.api.entity.server.scheduledevent;

import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.server.scheduledevent.internal.ServerScheduledEventBuilderDelegate;
import org.javacord.api.util.internal.DelegateFactory;
import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

/**
 * This class is used to create server scheduled events.
 * See <a href="https://discord.com/developers/docs/resources/guild-scheduled-event#create-guild-scheduled-event">discord documentation</a> for required fields to create events.
 */
public class ServerScheduledEventBuilder {

    /**
     * The server scheduled event delegate used by this instance.
     */
    private final ServerScheduledEventBuilderDelegate delegate;

    /**
     * Creates a new server scheduled event builder for the given server.
     *
     * @param server The server for which the server scheduled event should be created.
     */
    public ServerScheduledEventBuilder(Server server) {
        delegate = DelegateFactory.createServerScheduledEventBuilderDelegate(server);
    }

    /**
     * Sets the reason for the creation. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     * @return The current instance in order to chain call methods.
     */
    public ServerScheduledEventBuilder setAuditLogReason(String reason) {
        delegate.setAuditLogReason(reason);
        return this;
    }

    /**
     * Sets the name of the server scheduled event.
     *
     * @param name The name of the server scheduled event.
     * @return The current instance in order to chain call methods.
     */
    public ServerScheduledEventBuilder setName(String name) {
        delegate.setName(name);
        return this;
    }

    /**
     * Sets the description of the server scheduled event.
     *
     * @param description The description of the server scheduled event.
     * @return The current instance in order to chain call methods.
     */
    public ServerScheduledEventBuilder setDescription(String description) {
        delegate.setDescription(description);
        return this;
    }

    /**
     * Sets the scheduled start time of the server scheduled event.
     *
     * @param scheduledStartTime The scheduled start time of the server scheduled event.
     * @return The current instance in order to chain call methods.
     */
    public ServerScheduledEventBuilder setScheduledStartTime(Instant scheduledStartTime) {
        delegate.setScheduledStartTime(scheduledStartTime);
        return this;
    }

    /**
     * Sets the scheduled end time of the server scheduled event.
     *
     * @param scheduledEndTime The scheduled end time of the server scheduled event.
     * @return The current instance in order to chain call methods.
     */
    public ServerScheduledEventBuilder setScheduledEndTime(Instant scheduledEndTime) {
        delegate.setScheduledEndTime(scheduledEndTime);
        return this;
    }

    /**
     * Sets the scheduled start and end time of the server scheduled event.
     *
     * @param scheduledStartTime The scheduled start time of the server scheduled event.
     * @param duration The intended duration to calculate the scheduled end time.
     * @return The current instance in order to chain call methods.
     */
    public ServerScheduledEventBuilder setScheduledStartAndEndTime(Instant scheduledStartTime, Duration duration) {
        return setScheduledStartTime(scheduledStartTime).setScheduledEndTime(scheduledStartTime.plus(duration));
    }

    /**
     * Sets the privacy level of the server scheduled event.
     *
     * @param serverScheduledEventPrivacyLevel The privacy level of the server scheduled event.
     * @return The current instance in order to chain call methods.
     */
    public ServerScheduledEventBuilder setPrivacyLevel(
            ServerScheduledEventPrivacyLevel serverScheduledEventPrivacyLevel) {
        delegate.setPrivacyLevel(serverScheduledEventPrivacyLevel);
        return this;
    }

    /**
     * Sets the channel of the server scheduled event.
     *
     * @param channel The channel of the server scheduled event.
     * @return The current instance in order to chain call methods.
     */
    public ServerScheduledEventBuilder setChannel(ServerVoiceChannel channel) {
        delegate.setChannelId(channel.getId());
        return this;
    }

    /**
     * Sets the channel id of the server scheduled event.
     *
     * @param channelId The channel id of the server scheduled event.
     * @return The current instance in order to chain call methods.
     */
    public ServerScheduledEventBuilder setChannelId(Long channelId) {
        delegate.setChannelId(channelId);
        return this;
    }

    /**
     * Sets the entity type of the server scheduled event.
     *
     * @param serverScheduledEventType The entity type of the server scheduled event.
     * @return The current instance in order to chain call methods.
     */
    public ServerScheduledEventBuilder setEntityType(ServerScheduledEventType serverScheduledEventType) {
        delegate.setEntityType(serverScheduledEventType);
        return this;
    }

    /**
     * Sets the entity metadata location of the server scheduled event.
     *
     * @param entityMetadataLocation The entity metadata location of the server scheduled event.
     * @return The current instance in order to chain call methods.
     */
    public ServerScheduledEventBuilder setEntityMetadataLocation(String entityMetadataLocation) {
        delegate.setEntityMetadataLocation(entityMetadataLocation);
        return this;
    }

    /**
     * Sets the image of the server scheduled event.
     *
     * @param image The image of the server scheduled event.
     * @return The current instance in order to chain call methods.
     */
    public ServerScheduledEventBuilder setImage(File image) {
        delegate.setImage(image);
        return this;
    }

    /**
     * Creates the server scheduled event.
     *
     * @return The created server scheduled event.
     */
    public CompletableFuture<ServerScheduledEvent> create() {
        return delegate.create();
    }

}

package org.javacord.api.entity.server.scheduledevent;

import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.server.scheduledevent.internal.ServerScheduledEventUpdaterDelegate;
import org.javacord.api.util.internal.DelegateFactory;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

/**
 * This class can be used to update the settings of a role.
 */
public class ServerScheduledEventUpdater {

    /**
     * The role delegate used by this instance.
     */
    private final ServerScheduledEventUpdaterDelegate delegate;

    /**
     * Creates a new scheduledEvent updater.
     *
     * @param scheduledEvent The scheduledEvent to update.
     */
    public ServerScheduledEventUpdater(ServerScheduledEvent scheduledEvent) {
        delegate = DelegateFactory.createServerScheduledEventUpdaterDelegate(scheduledEvent);
    }

    /**
     * Sets the reason for this update. This reason will be visible in the audit log entry(s).
     *
     * @param reason The reason for this update.
     * @return The current instance in order to chain call methods.
     */
    public ServerScheduledEventUpdater setAuditLogReason(String reason) {
        delegate.setAuditLogReason(reason);
        return this;
    }

    /**
     * Sets the name of the server scheduled event.
     *
     * @param name The name of the server scheduled event.
     * @return The current instance in order to chain call methods.
     */
    public ServerScheduledEventUpdater setName(String name) {
        delegate.setName(name);
        return this;
    }

    /**
     * Sets the description of the server scheduled event.
     *
     * @param description The description of the server scheduled event.
     * @return The current instance in order to chain call methods.
     */
    public ServerScheduledEventUpdater setDescription(String description) {
        delegate.setDescription(description);
        return this;
    }

    /**
     * Sets the scheduled start time of the server scheduled event.
     *
     * @param scheduledStartTime The scheduled start time of the server scheduled event.
     * @return The current instance in order to chain call methods.
     */
    public ServerScheduledEventUpdater setScheduledStartTime(Instant scheduledStartTime) {
        delegate.setScheduledStartTime(scheduledStartTime);
        return this;
    }

    /**
     * Sets the scheduled end time of the server scheduled event.
     *
     * @param scheduledEndTime The scheduled end time of the server scheduled event.
     * @return The current instance in order to chain call methods.
     */
    public ServerScheduledEventUpdater setScheduledEndTime(Instant scheduledEndTime) {
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
    public ServerScheduledEventUpdater setScheduledStartAndEndTime(Instant scheduledStartTime, Duration duration) {
        return setScheduledStartTime(scheduledStartTime).setScheduledEndTime(scheduledStartTime.plus(duration));
    }

    /**
     * Sets the privacy level of the server scheduled event.
     *
     * @param serverScheduledEventPrivacyLevel The privacy level of the server scheduled event.
     * @return The current instance in order to chain call methods.
     */
    public ServerScheduledEventUpdater setPrivacyLevel(
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
    public ServerScheduledEventUpdater setChannel(ServerVoiceChannel channel) {
        delegate.setChannelId(channel.getId());
        return this;
    }

    /**
     * Sets the channel id of the server scheduled event.
     *
     * @param channelId The channel id of the server scheduled event.
     * @return The current instance in order to chain call methods.
     */
    public ServerScheduledEventUpdater setChannelId(Long channelId) {
        delegate.setChannelId(channelId);
        return this;
    }

    /**
     * Sets the entity type of the server scheduled event.
     *
     * @param serverScheduledEventType The entity type of the server scheduled event.
     * @return The current instance in order to chain call methods.
     */
    public ServerScheduledEventUpdater setEntityType(ServerScheduledEventType serverScheduledEventType) {
        delegate.setEntityType(serverScheduledEventType);
        return this;
    }

    /**
     * Sets the entity metadata location of the server scheduled event.
     *
     * @param entityMetadataLocation The entity metadata location of the server scheduled event.
     * @return The current instance in order to chain call methods.
     */
    public ServerScheduledEventUpdater setEntityMetadataLocation(String entityMetadataLocation) {
        delegate.setEntityMetadataLocation(entityMetadataLocation);
        return this;
    }

    /**
     * Sets the image of the server scheduled event.
     *
     * @param image The image of the server scheduled event.
     * @return The current instance in order to chain call methods.
     */
    public ServerScheduledEventUpdater setImage(String image) {
        delegate.setImage(image);
        return this;
    }

    /**
     * Sets the event status of the server scheduled event.
     *
     * @param serverScheduledEventStatus The event status of the server scheduled event.
     * @return The current instance in order to chain call methods.
     */
    public ServerScheduledEventUpdater setEventStatus(ServerScheduledEventStatus serverScheduledEventStatus) {
        delegate.setEventStatus(serverScheduledEventStatus);
        return this;
    }

    /**
     * Performs the queued updates.
     *
     * @return A future to check if the update was successful.
     */
    public CompletableFuture<Void> update() {
        return delegate.update();
    }

}

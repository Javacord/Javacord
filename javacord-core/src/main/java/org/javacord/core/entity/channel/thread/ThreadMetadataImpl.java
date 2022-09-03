package org.javacord.core.entity.channel.thread;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.channel.thread.ThreadMetadata;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Optional;

public class ThreadMetadataImpl implements ThreadMetadata {

    /**
     * Whether the thread is archived.
     */
    private final boolean isArchived;
    /**
     * The auto archive duration.
     */
    private final int autoArchiveDuration;
    /**
     * Whether the thread is locked.
     */
    private final boolean isLocked;
    /**
     * The timestamp when the thread's archive status was last changed.
     */
    private final Instant archiveTimestamp;
    /**
     * The timestamp of when the thread was created.
     */
    private final Instant creationTimestamp;
    /**
     * Informs you weather someone who is not a moderator can add non-moderator users to the thread.
     */
    private final Boolean isInvitable;

    /**
     * Creates a new thread metadata instance.
     *
     * @param data The json data of the thread metadata.
     */
    public ThreadMetadataImpl(final JsonNode data) {
        autoArchiveDuration = data.get("auto_archive_duration").asInt();
        isArchived = data.get("archived").asBoolean();
        isLocked = data.get("locked").asBoolean();
        archiveTimestamp = OffsetDateTime.parse(data.get("archive_timestamp").asText()).toInstant();
        creationTimestamp = data.hasNonNull("creation_timestamp") ? OffsetDateTime.parse(
                data.get("creation_timestamp").asText()).toInstant() : null;
        isInvitable = data.hasNonNull("invitable") ? data.get("invitable").asBoolean() : null;
    }

    @Override
    public boolean isArchived() {
        return isArchived;
    }

    @Override
    public int getAutoArchiveDuration() {
        return autoArchiveDuration;
    }

    @Override
    public boolean isLocked() {
        return isLocked;
    }

    @Override
    public Instant getArchiveTimestamp() {
        return archiveTimestamp;
    }

    @Override
    public Optional<Boolean> isInvitable() {
        return Optional.ofNullable(isInvitable);
    }

    @Override
    public Optional<Instant> getCreationTimestamp() {
        return Optional.ofNullable(creationTimestamp);
    }
}

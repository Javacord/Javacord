package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.channel.ChannelThread;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.server.ServerImpl;

import java.time.Instant;
import java.time.OffsetDateTime;

public class ChannelThreadImpl extends ServerTextChannelImpl implements ChannelThread {
    /*
    The id of the parent channel.
     */
    long parentId;

    /*
    The count of messages in the thread.
     */
    int messageCount;

    /*
    The count of members in the thread.
     */
    int memberCount;

    /*
    The default auto archive duration.
     */
    int defaultAutoArchiveDuration;

    /*
    The auto archive duration.
     */
    int autoArchiveDuration;

    /*
    Whether the thread is archived.
     */
    boolean isArchived;

    /*
    Whether the thread is locked.
     */
    boolean isLocked;

    /*
    The id of the creator of the channel.
     */
    long ownerId;

    /*
    The timestamp when the thread's archive status was last changed.
     */
    Instant archiveTimestamp;

    /**
     * Creates a new server text channel object.
     *
     * @param api The discord api instance.
     * @param server The server of the channel.
     * @param data The json data of the channel.
     */
    public ChannelThreadImpl(DiscordApiImpl api, ServerImpl server, JsonNode data) {
        super(api, server, data);

        parentId = data.get("parent_id").asLong();
        ownerId = Long.parseLong(data.hasNonNull("owner_id") ? data.get("owner_id").asText("-1") : "-1");
        messageCount = data.hasNonNull("message_count") ? data.get("message_count").asInt(0) : 0;
        memberCount = data.hasNonNull("member_count") ? data.get("member_count").asInt(0) : 0;
        defaultAutoArchiveDuration = data.has("default_auto_archive_duration")
                ? data.get("default_auto_archive_duration").asInt(60)
                : 60;

        JsonNode threadMetadata = data.get("thread_metadata");
        autoArchiveDuration = threadMetadata.hasNonNull("auto_archive_duration")
                ? threadMetadata.get("auto_archive_duration").asInt(defaultAutoArchiveDuration)
                : defaultAutoArchiveDuration;
        isArchived = threadMetadata.hasNonNull("archived") && threadMetadata.get("archived").asBoolean(false);
        isLocked = threadMetadata.hasNonNull("locked") && threadMetadata.get("locked").asBoolean(false);
        archiveTimestamp = threadMetadata.has("archive_timestamp")
                ? OffsetDateTime.parse(threadMetadata.get("archive_timestamp").asText()).toInstant() : null;


    }

    @Override
    public ServerTextChannel getParent() {
        return getServer().getTextChannelById(parentId)
                .orElseThrow(() -> new AssertionError("Thread has no parent channel."));
    }

    @Override
    public int getMessageCount() {
        return messageCount;
    }

    @Override
    public int getMemberCount() {
        return memberCount;
    }

    @Override
    public int getDefaultAutoArchiveDuration() {
        return defaultAutoArchiveDuration;
    }

    @Override
    public int getAutoArchiveDuration() {
        return autoArchiveDuration;
    }

    @Override
    public boolean isArchived() {
        return isArchived;
    }

    @Override
    public boolean isLocked() {
        return isLocked;
    }

    @Override
    public long getOwnerId() {
        return ownerId;
    }

    @Override
    public Instant getArchiveTimestamp() {
        return archiveTimestamp;
    }
}

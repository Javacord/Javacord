package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.channel.ThreadMember;

import java.time.Instant;
import java.time.OffsetDateTime;

public class ThreadMemberImpl implements ThreadMember {

    /**
     * The id of the thread. Omitted on members sent within the thread in a GUILD_CREATE event.
     */
    private final Long id;

    /**
     * The id of the user. Omitted on members sent within the thread in a GUILD_CREATE event.
     */
    private final Long userId;

    /**
     * The time the current user last joined the thread.
     */
    private final Instant joinTimestamp;

    /**
     * Any user-thread settings, currently only used for notifications.
     */
    private final int flags;

    @Override
    public Long getId() {return id;}

    @Override
    public Long getUserId() {return userId;}

    @Override
    public Instant getJoinTimestamp() {return joinTimestamp;}

    @Override
    public int getFlags() {return flags;}

    /**
     * Creates a new Thread Member object.
     *
     * @param data The JSON data from which to parse this object.
     */
    public ThreadMemberImpl (JsonNode data){
        id = Long.parseLong(data.hasNonNull("id") ? data.get("id").asText("-1") : "-1");
        userId = Long.parseLong(data.hasNonNull("user_id") ? data.get("id").asText("-1") : "-1");
        joinTimestamp = data.has("join_timestamp")
                ? OffsetDateTime.parse(data.get("join_timestamp").asText()).toInstant() : null;
        flags = data.hasNonNull("flags") ? data.get("flags").asInt(0) : 0;
    }
}

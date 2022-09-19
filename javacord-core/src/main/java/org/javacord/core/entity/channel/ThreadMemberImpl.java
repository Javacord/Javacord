package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ThreadMember;
import org.javacord.api.entity.server.Server;
import java.time.Instant;
import java.time.OffsetDateTime;

public class ThreadMemberImpl implements ThreadMember {

    /**
     * The id of the thread. Omitted on members sent within the thread in a GUILD_CREATE event.
     */
    private final long id;

    /**
     * The id of the user. Omitted on members sent within the thread in a GUILD_CREATE event.
     */
    private final long userId;

    /**
     * The time the current user last joined the thread.
     */
    private final Instant joinTimestamp;

    /**
     * Any user-thread settings, currently only used for notifications.
     */
    private final int flags;

    /**
     * The discord api.
     */
    private final DiscordApi api;

    /**
     * The Server related to this thread member.
     */
    private final Server server;

    /**
     * Creates a new Thread Member object.
     *
     * @param api    The discord api.
     * @param server The server.
     * @param data   The JSON data from which to parse this object.
     */
    public ThreadMemberImpl(final DiscordApi api, final Server server, final JsonNode data) {
        this(api, server, data, data.get("id").asLong(), data.get("user_id").asLong());
    }
    
    /**
     * Creates a new Thread Member object.
     *
     * @param api    The discord api.
     * @param server The server.
     * @param data   The JSON data from which to parse this object.
     * @param id     The id of the thread
     * @param userId The id of the user
     */
    public ThreadMemberImpl(final DiscordApi api, final Server server, final JsonNode data,
            final long id, final long userId) {
        this.api = api;
        this.server = server;
        this.id = id;
        this.userId = userId;
        joinTimestamp = OffsetDateTime.parse(data.get("join_timestamp").asText()).toInstant();
        flags = data.get("flags").asInt();
    }

    @Override
    public DiscordApi getApi() {
        return api;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public Instant getJoinTimestamp() {
        return joinTimestamp;
    }

    @Override
    public int getFlags() {
        return flags;
    }

}

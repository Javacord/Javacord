package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.listener.channel.server.text.InternalServerTextChannelAttachableListenerManager;

import java.util.Objects;

/**
 * The implementation of {@link ServerTextChannel}.
 */
public class ServerTextChannelImpl extends ServerMessageChannelImpl implements ServerTextChannel,
        InternalServerTextChannelAttachableListenerManager {

    /**
     * The slowmode delay of the channel.
     */
    private volatile int delay;

    /**
     * Creates a new server text channel object.
     *
     * @param api    The discord api instance.
     * @param server The server of the channel.
     * @param data   The json data of the channel.
     */
    public ServerTextChannelImpl(DiscordApiImpl api, ServerImpl server, JsonNode data) {
        super(api, server, data);
        delay = data.has("rate_limit_per_user") ? data.get("rate_limit_per_user").asInt(0) : 0;
    }


    /**
     * Sets the slowmode delay of the channel.
     *
     * @param delay The delay in seconds.
     */
    public void setSlowmodeDelayInSeconds(int delay) {
        this.delay = delay;
    }

    @Override
    public int getSlowmodeDelayInSeconds() {
        return delay;
    }

    @Override
    public boolean equals(Object o) {
        return (this == o)
                || !((o == null)
                || (getClass() != o.getClass())
                || (getId() != ((DiscordEntity) o).getId()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return String.format("ServerTextChannel (id: %s, name: %s)", getIdAsString(), getName());
    }
}

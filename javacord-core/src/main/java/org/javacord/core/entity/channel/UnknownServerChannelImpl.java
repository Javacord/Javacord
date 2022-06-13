package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.channel.UnknownServerChannel;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.server.ServerImpl;
import java.util.Objects;

/**
 * The implementation of {@link UnknownServerChannel}.
 */
public class UnknownServerChannelImpl extends ServerChannelImpl implements UnknownServerChannel {

    /**
     * Creates a new unknown server channel object.
     *
     * @param api    The discord api instance.
     * @param server The server of the channel.
     * @param data   The json data of the channel.
     */
    public UnknownServerChannelImpl(final DiscordApiImpl api, final ServerImpl server, final JsonNode data) {
        super(api, server, data);
    }

    @Override
    public String getMentionTag() {
        return "<#" + getIdAsString() + ">";
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
        return String.format("UnknownServerChannel (id: %s, name: %s)", getIdAsString(), getName());
    }
}

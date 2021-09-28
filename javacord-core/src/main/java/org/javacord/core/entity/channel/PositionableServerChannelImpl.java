package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.channel.PositionableServerChannel;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.server.ServerImpl;
import java.util.Comparator;

public class PositionableServerChannelImpl extends ServerChannelImpl implements PositionableServerChannel {

    /**
     * Compares channels according to their "position" field and, if those are the same, their id.
     */
    public static final Comparator<PositionableServerChannel> COMPARE_BY_RAW_POSITION = Comparator
            .comparingInt(PositionableServerChannel::getRawPosition)
            .thenComparingLong(PositionableServerChannel::getId);

    /**
     * The rawPosition of the channel.
     */
    private volatile int rawPosition;

    /**
     * Creates a new server channel object.
     *
     * @param api    The discord api instance.
     * @param server The server of the channel.
     * @param data   The json data of the channel.
     */
    public PositionableServerChannelImpl(final DiscordApiImpl api, final ServerImpl server, final JsonNode data) {
        super(api, server, data);
        rawPosition = data.get("position").asInt();
    }

    @Override
    public int getRawPosition() {
        return rawPosition;
    }

    /**
     * Sets the raw position of the channel.
     *
     * @param position The new raw position of the channel.
     */
    public void setRawPosition(int position) {
        this.rawPosition = position;
    }
}

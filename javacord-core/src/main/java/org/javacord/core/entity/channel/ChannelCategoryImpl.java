package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.channel.Categorizable;
import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.listener.channel.server.InternalChannelCategoryAttachableListenerManager;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The implementation of {@link ChannelCategory}.
 */
public class ChannelCategoryImpl extends ServerChannelImpl
        implements ChannelCategory, InternalChannelCategoryAttachableListenerManager {

    /**
     * Whether the category is "not safe for work" or not.
     */
    private volatile boolean nsfw;

    /**
     * Creates a new server channel category object.
     *
     * @param api The discord api instance.
     * @param server The server of the channel.
     * @param data The json data of the channel.
     */
    public ChannelCategoryImpl(DiscordApiImpl api, ServerImpl server, JsonNode data) {
        super(api, server, data);
        nsfw = data.has("nsfw") && data.get("nsfw").asBoolean();
    }

    /**
     * Sets the nsfw flag.
     *
     * @param nsfw The nsfw flag.
     */
    public void setNsfwFlag(boolean nsfw) {
        this.nsfw = nsfw;
    }

    @Override
    public List<ServerChannel> getChannels() {
        return Collections.unmodifiableList(
                ((ServerImpl) getServer()).getUnorderedChannels().stream()
                        .filter(channel -> channel.asCategorizable()
                                .flatMap(Categorizable::getCategory)
                                .map(this::equals)
                                .orElse(false))
                        .sorted(Comparator
                                        .<ServerChannel>comparingInt(channel -> channel.getType().getId())
                                        .thenComparing(ServerChannelImpl.COMPARE_BY_RAW_POSITION))
                        .collect(Collectors.toList()));
    }

    @Override
    public boolean isNsfw() {
        return nsfw;
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
        return String.format("ChannelCategory (id: %s, name: %s)", getIdAsString(), getName());
    }

}

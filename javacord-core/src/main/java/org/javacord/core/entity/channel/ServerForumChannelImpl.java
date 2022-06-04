package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.ServerForumChannel;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.listener.channel.server.forum.InternalServerForumChannelAttachableListenerManager;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link ServerForumChannel}.
 */
public class ServerForumChannelImpl extends RegularServerChannelImpl
        implements ServerForumChannel, InternalServerForumChannelAttachableListenerManager {


    /**
     * The parent id of the channel.
     */
    private volatile long parentId;

    /**
     * Creates a new server forum channel object.
     *
     * @param api    The discord api instance.
     * @param server The server of the channel.
     * @param data   The json data of the channel.
     */
    public ServerForumChannelImpl(DiscordApiImpl api, ServerImpl server, JsonNode data) {
        super(api, server, data);
        parentId = Long.parseLong(data.has("parent_id") ? data.get("parent_id").asText("-1") : "-1");
    }


    /**
     * Sets the parent id of the channel.
     *
     * @param parentId The parent id to set.
     */
    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    @Override
    public Optional<ChannelCategory> getCategory() {
        return getServer().getChannelCategoryById(parentId);
    }

    @Override
    public CompletableFuture<Void> updateCategory(ChannelCategory category) {
        return null;
    }

    @Override
    public CompletableFuture<Void> removeCategory() {
        return null;
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
        return String.format("ServerForumChannel (id: %s, name: %s)", getIdAsString(), getName());
    }

}

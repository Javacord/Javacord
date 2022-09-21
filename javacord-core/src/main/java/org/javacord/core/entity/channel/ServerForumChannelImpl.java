package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.ChannelFlag;
import org.javacord.api.entity.channel.ServerForumChannel;
import org.javacord.api.entity.channel.forum.DefaultReaction;
import org.javacord.api.entity.channel.forum.ForumTag;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.channel.forum.DefaultReactionImpl;
import org.javacord.core.entity.channel.forum.ForumTagImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.listener.channel.server.forum.InternalServerForumChannelAttachableListenerManager;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
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
     * The channel's flags.
     */
    private volatile EnumSet<ChannelFlag> flags = EnumSet.noneOf(ChannelFlag.class);

    /**
     * The set of tags that can be used.
     */
    private volatile List<ForumTag> tags = new ArrayList<>();

    /**
     * The list of applied tags ids.
     */
    private volatile List<Long> appliedTags = new ArrayList<>();

    /**
     * The default emoji shown in the add reaction button.
     */
    private volatile DefaultReaction defaultReaction;

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

        if (data.hasNonNull("flags")) {
            for (JsonNode flag : data.get("flags")) {
                flags.add(ChannelFlag.fromInt(flag.asInt()));
            }
        }

        if (data.hasNonNull("tags")) {
            for (JsonNode tag : data.get("tags")) {
                tags.add(new ForumTagImpl(api, tag.get("id").asLong(), tag));
            }
        }

        if (data.hasNonNull("applied_tags")) {
            for (JsonNode tag : data.get("applied_tags")) {
                appliedTags.add(tag.asLong());
            }
        }

        defaultReaction = data.hasNonNull("default_reaction_emoji")
                ? new DefaultReactionImpl(data.get("default_reaction_emoji")) : null;
    }


    /**
     * Sets the parent id of the channel.
     *
     * @param parentId The parent id to set.
     */
    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    /**
     * Sets the channel's flags.
     *
     * @param flags The channel's flags.
     */
    public void setFlags(EnumSet<ChannelFlag> flags) {
        this.flags = flags;
    }

    /**
     * Sets the set of tags that can be used.
     *
     * @param tags The set of tags that can be used.
     */
    public void setTags(List<ForumTag> tags) {
        this.tags = tags;
    }

    /**
     * Sets the list of applied tags ids.
     *
     * @param appliedTags The list of applied tags ids.
     */
    public void setAppliedTags(List<Long> appliedTags) {
        this.appliedTags = appliedTags;
    }

    /**
     * Sets the default emoji shown in the add reaction button.
     *
     * @param defaultReaction The default emoji shown in the add reaction button.
     */
    public void setDefaultReaction(DefaultReaction defaultReaction) {
        this.defaultReaction = defaultReaction;
    }

    @Override
    public Optional<ChannelCategory> getCategory() {
        return getServer().getChannelCategoryById(parentId);
    }

    @Override
    public EnumSet<ChannelFlag> getFlags() {
        return flags;
    }

    @Override
    public List<ForumTag> getTags() {
        return tags;
    }

    @Override
    public List<Long> getAppliedTags() {
        return appliedTags;
    }

    @Override
    public Optional<DefaultReaction> getDefaultReaction() {
        return Optional.ofNullable(defaultReaction);
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

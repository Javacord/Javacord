package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.ChannelFlag;
import org.javacord.api.entity.channel.ServerForumChannel;
import org.javacord.api.entity.channel.forum.DefaultReaction;
import org.javacord.api.entity.channel.forum.ForumTag;
import org.javacord.api.entity.channel.forum.PermissionOverwrite;
import org.javacord.api.entity.channel.forum.SortOrderType;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.channel.forum.DefaultReactionImpl;
import org.javacord.core.entity.channel.forum.ForumTagImpl;
import org.javacord.core.entity.channel.forum.PermissionOverwriteImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.listener.channel.server.forum.InternalServerForumChannelAttachableListenerManager;

import java.util.Collections;
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
    private long parentId;

    /**
     * The topic of the channel.
     */
    private String topic;

    /**
     * The rate limit per user of the channel.
     */
    private int rateLimitPerUser;

    /**
     * The position of the channel.
     */
    private int position;

    /**
     * The permission overwrites of the channel.
     */
    private List<PermissionOverwrite> permissionOverwrites;

    /**
     * Whether the channel is nsfw.
     */
    private boolean nsfw;

    /**
     * The last message id of the channel.
     */
    private Long lastMessageId;

    /**
     * The default auto archive duration of the channel.
     */
    private int defaultAutoArchiveDuration;

    /**
     * The flags of the channel.
     */
    private EnumSet<ChannelFlag> flags;

    /**
     * The available tags of the channel.
     */
    private List<ForumTag> availableTags;

    /**
     * The template for the channel.
     */
    private String template;

    /**
     * The default sort order of the channel.
     */
    private SortOrderType defaultSortOrder;

    /**
     * The default reaction of the channel.
     */
    private DefaultReaction defaultReaction;

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
        topic = data.get("topic").asText();
        rateLimitPerUser = data.get("rate_limit_per_user").asInt();
        position = data.get("position").asInt();
        nsfw = data.get("nsfw").asBoolean();
        lastMessageId = data.has("last_message_id") ? data.get("last_message_id").asLong() : null;
        defaultAutoArchiveDuration = data.get("default_auto_archive_duration").asInt();
        if (data.has("permission_overwrites")) {
            for (JsonNode permissionOverwrite : data.get("permission_overwrites")) {
                permissionOverwrites.add(new PermissionOverwriteImpl(api, permissionOverwrite));
            }
        }
        if (data.has("flags")) {
            for (JsonNode flag : data.get("flags")) {
                flags.add(ChannelFlag.getByValue(flag.asInt()));
            }
        }
        if (data.has("available_tags")) {
            for (JsonNode tag : data.get("available_tags")) {
                availableTags.add(new ForumTagImpl(api, tag));
            }
        }
        template = data.has("template") ? data.get("template").asText() : null;
        defaultSortOrder = data.has("default_sort_order") ? SortOrderType.getByValue(data.get("default_sort_order").asInt()) : null;
        defaultReaction = data.has("default_reaction_emoji") ? new DefaultReactionImpl(data.get("default_reaction_emoji")) : null;
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
     * Sets the topic of the channel.
     *
     * @param topic The topic to set.
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
     * Sets the rate limit per user of the channel.
     *
     * @param rateLimitPerUser The rate limit per user to set.
     */
    public void setRateLimitPerUser(int rateLimitPerUser) {
        this.rateLimitPerUser = rateLimitPerUser;
    }

    /**
     * Sets the position of the channel.
     *
     * @param position The position to set.
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Sets the permission overwrites of the channel.
     *
     * @param permissionOverwrites The permission overwrites to set.
     */
    public void setPermissionOverwrites(List<PermissionOverwrite> permissionOverwrites) {
        this.permissionOverwrites = permissionOverwrites;
    }

    /**
     * Sets whether the channel is nsfw.
     *
     * @param nsfw Whether the channel is nsfw or not.
     */
    public void setNsfw(boolean nsfw) {
        this.nsfw = nsfw;
    }

    /**
     * Sets the last message id of the channel.
     *
     * @param lastMessageId The last message id to set.
     */
    public void setLastMessageId(Long lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    /**
     * Sets the default auto archive duration of the channel.
     *
     * @param defaultAutoArchiveDuration The default auto archive duration to set.
     */
    public void setDefaultAutoArchiveDuration(int defaultAutoArchiveDuration) {
        this.defaultAutoArchiveDuration = defaultAutoArchiveDuration;
    }

    /**
     * Sets the flags of the channel.
     *
     * @param flags The flags to set.
     */
    public void setFlags(EnumSet<ChannelFlag> flags) {
        this.flags = flags;
    }

    /**
     * Sets the available tags of the channel.
     *
     * @param availableTags The available tags to set.
     */
    public void setAvailableTags(List<ForumTag> availableTags) {
        this.availableTags = availableTags;
    }

    /**
     * Sets the template of the channel.
     *
     * @param template The template to set.
     */
    public void setTemplate(String template) {
        this.template = template;
    }

    /**
     * Sets the default sort order of the channel.
     *
     * @param defaultSortOrder The default sort order to set.
     */
    public void setDefaultSortOrder(SortOrderType defaultSortOrder) {
        this.defaultSortOrder = defaultSortOrder;
    }

    /**
     * Sets the default reaction of the channel.
     *
     * @param defaultReaction The default reaction to set.
     */
    public void setDefaultReaction(DefaultReaction defaultReaction) {
        this.defaultReaction = defaultReaction;
    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public int getRateLimitPerUser() {
        return rateLimitPerUser;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public List<PermissionOverwrite> getPermissionOverwrites() {
        return Collections.unmodifiableList(permissionOverwrites);
    }

    @Override
    public boolean isNsfw() {
        return nsfw;
    }

    @Override
    public Optional<Long> getLastMessageId() {
        return Optional.ofNullable(lastMessageId);
    }

    @Override
    public int getDefaultRateLimitPerUser() {
        return defaultAutoArchiveDuration;
    }

    @Override
    public EnumSet<ChannelFlag> getFlags() {
        return flags;
    }

    @Override
    public List<ForumTag> getAvailableTags() {
        return Collections.unmodifiableList(availableTags);
    }

    @Override
    public Optional<String> getTemplate() {
        return Optional.ofNullable(template);
    }

    @Override
    public Optional<SortOrderType> getDefaultSortType() {
        return Optional.ofNullable(defaultSortOrder);
    }

    @Override
    public Optional<DefaultReaction> getDefaultReaction() {
        return Optional.ofNullable(defaultReaction);
    }

    @Override
    public Optional<ChannelCategory> getCategory() {
        return getApi().getChannelCategoryById(parentId);
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

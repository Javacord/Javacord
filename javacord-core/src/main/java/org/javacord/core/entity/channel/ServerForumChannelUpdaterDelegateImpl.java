package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.ChannelFlag;
import org.javacord.api.entity.channel.ServerForumChannel;
import org.javacord.api.entity.channel.forum.PermissionOverwrite;
import org.javacord.api.entity.channel.forum.SortOrderType;
import org.javacord.api.entity.channel.internal.ServerForumChannelUpdaterDelegate;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * The implementation of {@link ServerForumChannelUpdaterDelegate}.
 */
public class ServerForumChannelUpdaterDelegateImpl extends RegularServerChannelUpdaterDelegateImpl
        implements ServerForumChannelUpdaterDelegate {

    /**
     * The category to update.
     */
    protected ChannelCategory category = null;

    /**
     * Whether the category should be modified or not.
     */
    protected boolean modifyCategory = false;

    /**
     * The topic to update.
     */
    protected String topic = null;

    /**
     * Whether the topic should be modified or not.
     */
    protected boolean modifyTopic = false;

    /**
     * The rate limit per user to update.
     */
    protected int rateLimitPerUser = -1;

    /**
     * Whether the rate limit per user should be modified or not.
     */
    protected boolean modifyRateLimitPerUser = false;

    /**
     * The position to update.
     */
    protected int position = -1;

    /**
     * Whether the position should be modified or not.
     */
    protected boolean modifyPosition = false;

    /**
     * The permission overwrites to update.
     */
    protected List<PermissionOverwrite> permissionOverwrites = null;

    /**
     * Whether the permission overwrites should be modified or not.
     */
    protected boolean modifyPermissionOverwrites = false;

    /**
     * The nsfw flag to update.
     */
    protected boolean nsfw = false;

    /**
     * Whether the nsfw flag should be modified or not.
     */
    protected boolean modifyNsfw = false;


    /**
     * The flags to update.
     */
    protected EnumSet<ChannelFlag> flags = null;

    /**
     * Whether the flags should be modified or not.
     */
    protected boolean modifyFlags = false;

    /**
     * The template to update.
     */
    protected String template = null;

    /**
     * Whether the template should be modified or not.
     */
    protected boolean modifyTemplate = false;

    /**
     * The sort order to update.
     */
    protected SortOrderType sortOrder = null;

    /**
     * Whether the sort order should be modified or not.
     */
    protected boolean modifySortOrder = false;

    /**
     * The default reaction to update.
     */
    protected long defaultReaction = -1;

    /**
     * Whether the default reaction should be modified or not.
     */
    protected boolean modifyDefaultReaction = false;


    /**
     * Creates a new server forum channel updater delegate.
     *
     * @param channel The channel to update.
     */
    public ServerForumChannelUpdaterDelegateImpl(ServerForumChannel channel) {
        super(channel);
    }

    @Override
    public void setTopic(String topic) {
        this.topic = topic;
        this.modifyTopic = true;
    }

    @Override
    public void setRateLimitPerUser(int rateLimitPerUser) {
        this.rateLimitPerUser = rateLimitPerUser;
        this.modifyRateLimitPerUser = true;
    }

    @Override
    public void setPosition(int position) {
        this.position = position;
        this.modifyPosition = true;
    }

    @Override
    public void setPermissionOverwrites(List<PermissionOverwrite> permissionOverwrites) {
        this.permissionOverwrites = permissionOverwrites;
        this.modifyPermissionOverwrites = true;
    }

    @Override
    public void setNsfw(boolean nsfw) {
        this.nsfw = nsfw;
        this.modifyNsfw = true;
    }

    @Override
    public void setFlags(EnumSet<ChannelFlag> flags) {
        this.flags = flags;
        this.modifyFlags = true;
    }

    @Override
    public void setTemplate(String template) {
        this.template = template;
        this.modifyTemplate = true;
    }

    @Override
    public void setDefaultSortType(SortOrderType defaultType) {
        this.sortOrder = defaultType;
        this.modifySortOrder = true;
    }

    @Override
    public void setDefaultReaction(long defaultReactionId) {
        this.defaultReaction = defaultReactionId;
        this.modifyDefaultReaction = true;
    }

    @Override
    public void setCategory(ChannelCategory category) {
        this.category = category;
        this.modifyCategory = true;
    }

    @Override
    public void removeCategory() {
        setCategory(null);
    }

    @Override
    protected boolean prepareUpdateBody(ObjectNode body) {
        boolean patchChannel = super.prepareUpdateBody(body);

        if (modifyCategory) {
            body.put("parent_id", category == null ? null : category.getIdAsString());
            patchChannel = true;
        }

        if (modifyTopic) {
            body.put("topic", topic);
            patchChannel = true;
        }

        if (modifyRateLimitPerUser) {
            body.put("rate_limit_per_user", rateLimitPerUser);
            patchChannel = true;
        }

        if (modifyPosition) {
            body.put("position", position);
            patchChannel = true;
        }

        if (modifyNsfw) {
            body.put("nsfw", nsfw);
            patchChannel = true;
        }

        if (modifyFlags) {
            ArrayNode flagsArray = body.putArray("flags");
            for (ChannelFlag flag : flags) {
                flagsArray.add(flag.asInt());
            }
            body.set("flags", flagsArray);
            patchChannel = true;
        }

        if (modifyTemplate) {
            body.put("template", template);
            patchChannel = true;
        }

        if (modifySortOrder) {
            body.put("default_sort_order", sortOrder.getValue());
            patchChannel = true;
        }

        if (modifyDefaultReaction) {
            body.put("default_reaction_emoji", defaultReaction);
            patchChannel = true;
        }

        return patchChannel;
    }
}

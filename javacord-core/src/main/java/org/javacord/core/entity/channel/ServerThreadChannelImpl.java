package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.channel.RegularServerChannel;
import org.javacord.api.entity.channel.ServerThreadChannel;
import org.javacord.api.entity.channel.ThreadMember;
import org.javacord.api.entity.channel.forum.ForumTag;
import org.javacord.api.entity.channel.thread.ThreadMetadata;
import org.javacord.api.util.cache.MessageCache;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.channel.thread.ThreadMetadataImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.listener.channel.server.text.InternalServerTextChannelAttachableListenerManager;
import org.javacord.core.util.Cleanupable;
import org.javacord.core.util.cache.MessageCacheImpl;
import org.javacord.core.util.rest.RestEndpoint;
import org.javacord.core.util.rest.RestMethod;
import org.javacord.core.util.rest.RestRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * The implementation of {@link ServerThreadChannel}.
 */
public class ServerThreadChannelImpl extends ServerChannelImpl implements ServerThreadChannel, Cleanupable,
        InternalTextChannel, InternalServerTextChannelAttachableListenerManager {

    /**
     * The message cache of the server text channel.
     */
    private final MessageCacheImpl messageCache;

    /**
     * The id of the parent channel.
     */
    private final long parentId;

    /**
     * The count of messages in the thread.
     */
    private int messageCount;

    /**
     * The count of members in the thread.
     */
    private int memberCount;

    /**
     * The id of the last message sent in the thread.
     */
    private long lastMessageId;

    /**
     * The rate limit per user.
     */
    private int rateLimitPerUser;

    /**
     * The id of the creator of the channel.
     */
    private final long ownerId;

    /**
     * The thread's users.
     */
    private final Set<ThreadMember> members;

    /**
     * The thread's metadata.
     */
    private final ThreadMetadata metadata;

    /**
     * The total number of messages that are sent.
     */
    private int totalNumberOfMessagesSent;

    /**
     * The forum applied tags.
     */
    private Set<ForumTag> forumTags = new HashSet<>();

    /**
     * Creates a new server text channel object.
     *
     * @param api    The discord api instance.
     * @param server The server of the channel.
     * @param data   The json data of the channel.
     */
    public ServerThreadChannelImpl(final DiscordApiImpl api, final ServerImpl server, final JsonNode data) {
        super(api, server, data);

        parentId = data.get("parent_id").asLong();
        ownerId = data.get("owner_id").asLong();
        messageCount = data.get("message_count").asInt(0);
        memberCount = data.get("member_count").asInt(0);
        lastMessageId = data.hasNonNull("last_message_id") ? data.get("last_message_id").asLong() : 0;
        rateLimitPerUser = data.get("rate_limit_per_user").asInt(0);

        members = new HashSet<>();
        if (data.hasNonNull("member")) {
            // If userId is not included, that means this came from a GUILD_CREATE event
            // This means the userId is the bot's and the thread id is from this thread
            // See https://github.com/Javacord/Javacord/issues/898
            if (data.get("member").hasNonNull("user_id")) {
                members.add(new ThreadMemberImpl(api, server, data.get("member")));
            } else {
                members.add(new ThreadMemberImpl(api, server, data.get("member"),
                        getId(), api.getYourself().getId()));
            }
        }

        this.metadata = new ThreadMetadataImpl(data.get("thread_metadata"));

        messageCache = new MessageCacheImpl(
                api, api.getDefaultMessageCacheCapacity(), api.getDefaultMessageCacheStorageTimeInSeconds(),
                api.isDefaultAutomaticMessageCacheCleanupEnabled());

        totalNumberOfMessagesSent = data.path("total_message_sent").asInt(0);

        getParent().asServerForumChannel().ifPresent(forumChannel -> {
            Set<ForumTag> allForumTags = forumChannel.getForumTags();

            List<Long> appliedTagIds = new ArrayList<>();
            if (data.hasNonNull("applied_tags")) {
                for (JsonNode appliedTag : data.get("applied_tags")) {
                    appliedTagIds.add(appliedTag.asLong());
                }
            }

            for (ForumTag forumTag : allForumTags) {
                if (appliedTagIds.contains(forumTag.getId())) {
                    forumTags.add(forumTag);
                }
            }
        });
    }

    /**
     * Used to set a new message count.
     *
     * @param messageCount The new message count.
     */
    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    /**
     * Used to set a new member count.
     *
     * @param memberCount The new member count.
     */
    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    /**
     * Used to set a new last message id.
     *
     * @param lastMessageId The new last message id.
     */
    public void setLastMessageId(long lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    /**
     * Used to set a new rate limit per user.
     *
     * @param rateLimitPerUser The new rate limit per user.
     */
    public void setRateLimitPerUser(int rateLimitPerUser) {
        this.rateLimitPerUser = rateLimitPerUser;
    }

    /**
     * Used to set a new total for the number of messages sent.
     *
     * @param totalNumberOfMessagesSent The new total for the number of messages sent.
     */
    public void setTotalNumberOfMessagesSent(int totalNumberOfMessagesSent) {
        this.totalNumberOfMessagesSent = totalNumberOfMessagesSent;
    }

    @Override
    public RegularServerChannel getParent() {
        return getServer().getRegularChannelById(parentId)
                .orElseThrow(() -> new AssertionError("Thread has no parent channel."));
    }

    @Override
    public int getMessageCount() {
        return messageCount;
    }

    @Override
    public int getMemberCount() {
        return memberCount;
    }

    @Override
    public long getLastMessageId() {
        return lastMessageId;
    }

    @Override
    public ThreadMetadata getMetadata() {
        return metadata;
    }

    @Override
    public long getOwnerId() {
        return ownerId;
    }

    @Override
    public Set<ThreadMember> getMembers() {
        return Collections.unmodifiableSet(members);
    }

    @Override
    public Set<ForumTag> getForumTags() {
        return forumTags;
    }

    /**
     * Sets the forum tags of this ServerThreadChannel.
     *
     * @param newForumTags The new forum tags.
     */
    public void setForumTags(Set<ForumTag> newForumTags) {
        forumTags = newForumTags;
    }

    @Override
    public CompletableFuture<Void> addThreadMember(final long userId) {
        return new RestRequest<Void>(getApi(), RestMethod.PUT, RestEndpoint.ADD_REMOVE_THREAD_MEMBER)
                .setUrlParameters(getIdAsString(), String.valueOf(userId))
                .execute(result -> null);
    }

    @Override
    public CompletableFuture<Void> removeThreadMember(final long userId) {
        return new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.ADD_REMOVE_THREAD_MEMBER)
                .setUrlParameters(getIdAsString(), String.valueOf(userId))
                .execute(result -> null);
    }

    @Override
    public CompletableFuture<Void> leaveThread() {
        return new RestRequest<Void>(getApi(), RestMethod.DELETE, RestEndpoint.ADD_REMOVE_THREAD_MEMBER)
                .setUrlParameters(getIdAsString())
                .execute(result -> null);
    }

    @Override
    public CompletableFuture<ThreadMember> requestThreadMemberById(long userId) {
        return new RestRequest<ThreadMember>(getApi(), RestMethod.GET, RestEndpoint.THREAD_MEMBER)
                .setUrlParameters(getIdAsString(), String.valueOf(userId))
                .execute(result -> new ThreadMemberImpl(getApi(), getServer(), result.getJsonBody()));
    }

    @Override
    public CompletableFuture<Set<ThreadMember>> requestThreadMembers() {
        return new RestRequest<Set<ThreadMember>>(getApi(), RestMethod.GET, RestEndpoint.LIST_THREAD_MEMBERS)
                .setUrlParameters(getIdAsString())
                .execute(result -> {
                    final Set<ThreadMember> threadMembers = new HashSet<>();
                    final JsonNode jsonNode = result.getJsonBody();
                    for (final JsonNode node : jsonNode) {
                        threadMembers.add(new ThreadMemberImpl(getApi(), getServer(), node));
                    }
                    return Collections.unmodifiableSet(threadMembers);
                });
    }

    @Override
    public int getTotalNumberOfMessagesSent() {
        return totalNumberOfMessagesSent;
    }

    @Override
    public int getRateLimitPerUser() {
        return rateLimitPerUser;
    }

    /**
     * Sets the members of this ServerThreadChannel.
     *
     * @param members The new members.
     */
    public void setMembers(final Set<ThreadMember> members) {
        this.members.clear();
        this.members.addAll(members);
    }

    @Override
    public MessageCache getMessageCache() {
        return messageCache;
    }

    @Override
    public String getMentionTag() {
        return "<#" + getIdAsString() + ">";
    }

    @Override
    public void cleanup() {
        messageCache.cleanup();

        ((DiscordApiImpl) getApi()).forEachCachedMessageWhere(
                msg -> msg.getChannel().getId() == getId(),
                msg -> ((DiscordApiImpl) getApi()).removeMessageFromCache(msg.getId())
        );
    }
}

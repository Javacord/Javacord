package org.javacord.core.entity.channel;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.channel.RegularServerChannel;
import org.javacord.api.entity.channel.ServerThreadChannel;
import org.javacord.api.entity.channel.ThreadMember;
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
import java.util.Collections;
import java.util.HashSet;
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
    private final int messageCount;

    /**
     * The count of members in the thread.
     */
    private final int memberCount;

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
    public ThreadMetadata getMetadata() {
        return metadata;
    }

    @Override
    public long getOwnerId() {
        return ownerId;
    }

    @Override
    public Set<ThreadMember> getMembers() {
        return members;
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
    public CompletableFuture<Set<ThreadMember>> getThreadMembers() {
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

package org.javacord.core.util.handler.channel.thread;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.entity.channel.ThreadMember;
import org.javacord.api.entity.channel.forum.ForumTag;
import org.javacord.api.event.channel.server.ServerChannelChangeNameEvent;
import org.javacord.api.event.channel.server.thread.ServerPrivateThreadJoinEvent;
import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeArchiveTimestampEvent;
import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeArchivedEvent;
import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeAutoArchiveDurationEvent;
import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeForumTagsEvent;
import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeInvitableEvent;
import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeLastMessageIdEvent;
import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeLockedEvent;
import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeMemberCountEvent;
import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeMessageCountEvent;
import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeRateLimitPerUserEvent;
import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeTotalMessageSentEvent;
import org.javacord.core.entity.channel.ServerThreadChannelImpl;
import org.javacord.core.entity.channel.ThreadMemberImpl;
import org.javacord.core.entity.channel.thread.ThreadMetadataImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.event.channel.server.ServerChannelChangeNameEventImpl;
import org.javacord.core.event.channel.server.thread.ServerPrivateThreadJoinEventImpl;
import org.javacord.core.event.channel.server.thread.ServerThreadChannelChangeArchiveTimestampEventImpl;
import org.javacord.core.event.channel.server.thread.ServerThreadChannelChangeArchivedEventImpl;
import org.javacord.core.event.channel.server.thread.ServerThreadChannelChangeAutoArchiveDurationEventImpl;
import org.javacord.core.event.channel.server.thread.ServerThreadChannelChangeForumTagsEventImpl;
import org.javacord.core.event.channel.server.thread.ServerThreadChannelChangeInvitableEventImpl;
import org.javacord.core.event.channel.server.thread.ServerThreadChannelChangeLastMessageIdEventImpl;
import org.javacord.core.event.channel.server.thread.ServerThreadChannelChangeLockedEventImpl;
import org.javacord.core.event.channel.server.thread.ServerThreadChannelChangeMemberCountEventImpl;
import org.javacord.core.event.channel.server.thread.ServerThreadChannelChangeMessageCountEventImpl;
import org.javacord.core.event.channel.server.thread.ServerThreadChannelChangeRateLimitPerUserEventImpl;
import org.javacord.core.event.channel.server.thread.ServerThreadChannelChangeTotalMessageSentEventImpl;
import org.javacord.core.util.event.DispatchQueueSelector;
import org.javacord.core.util.gateway.PacketHandler;
import org.javacord.core.util.logging.LoggerUtil;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ThreadUpdateHandler extends PacketHandler {

    private static final Logger logger = LoggerUtil.getLogger(ThreadUpdateHandler.class);

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public ThreadUpdateHandler(final DiscordApi api) {
        super(api, true, "THREAD_UPDATE");
    }

    @Override
    public void handle(final JsonNode packet) {
        final ChannelType type = ChannelType.fromId(packet.get("type").asInt());
        switch (type) {
            case SERVER_PUBLIC_THREAD:
            case SERVER_PRIVATE_THREAD:
            case SERVER_NEWS_THREAD:
                handleThread(packet);
                break;
            default:
                logger.warn("Unknown or unexpected channel type. Your Javacord version might be out of date!");
        }
    }

    private void handleThread(final JsonNode jsonChannel) {
        final long channelId = jsonChannel.get("id").asLong();
        final long serverId = jsonChannel.get("guild_id").asLong();
        final ServerImpl server = api.getServerById(serverId)
                .map(ServerImpl.class::cast)
                .orElse(null);

        if (server == null) {
            logger.warn("Unable to find server with id {}", serverId);
            return;
        }

        final ServerThreadChannelImpl thread =
                (ServerThreadChannelImpl) server.getOrCreateServerThreadChannel(jsonChannel);

        //Handling whether the name has changed
        final String oldName = thread.getName();
        final String newName = jsonChannel.get("name").asText();
        if (!Objects.deepEquals(oldName, newName)) {
            thread.setName(newName);

            final ServerChannelChangeNameEvent event =
                    new ServerChannelChangeNameEventImpl(thread, newName, oldName);

            api.getEventDispatcher().dispatchServerChannelChangeNameEvent(
                    (DispatchQueueSelector) thread.getServer(), thread.getServer(), thread, event);
        }

        final int oldMessageCount = thread.getMessageCount();
        final int newMessageCount = jsonChannel.get("message_count").asInt();
        if (oldMessageCount != newMessageCount) {
            thread.setMessageCount(newMessageCount);

            final ServerThreadChannelChangeMessageCountEvent event =
                    new ServerThreadChannelChangeMessageCountEventImpl(thread, newMessageCount, oldMessageCount);

            api.getEventDispatcher().dispatchServerThreadChannelChangeMessageCountEvent(
                    (DispatchQueueSelector) thread.getServer(), thread.getServer(), thread, event);
        }

        final int oldMemberCount = thread.getMemberCount();
        final int newMemberCount = jsonChannel.get("member_count").asInt();
        if (oldMemberCount != newMemberCount) {
            thread.setMemberCount(newMemberCount);

            final ServerThreadChannelChangeMemberCountEvent event =
                    new ServerThreadChannelChangeMemberCountEventImpl(thread, newMemberCount, oldMemberCount);

            api.getEventDispatcher().dispatchServerThreadChannelChangeMemberCountEvent(
                    (DispatchQueueSelector) thread.getServer(), thread.getServer(), thread, event);
        }

        final int oldNumberOfMessages = thread.getTotalNumberOfMessagesSent();
        final int newNumberOfMessages = jsonChannel.get("total_message_sent").asInt(0);
        if (oldNumberOfMessages != newNumberOfMessages) {
            thread.setTotalNumberOfMessagesSent(newNumberOfMessages);

            final ServerThreadChannelChangeTotalMessageSentEvent event =
                    new ServerThreadChannelChangeTotalMessageSentEventImpl(thread, newNumberOfMessages,
                            oldNumberOfMessages);

            api.getEventDispatcher().dispatchServerThreadChannelChangeTotalMessageSentEvent(
                    (DispatchQueueSelector) thread.getServer(), thread.getServer(), thread, event);
        }

        final long oldLastMessageId = thread.getLastMessageId();
        final long newLastMessageId = jsonChannel.get("last_message_id").asLong();
        if (oldLastMessageId != newLastMessageId) {
            thread.setLastMessageId(newLastMessageId);

            final ServerThreadChannelChangeLastMessageIdEvent event =
                    new ServerThreadChannelChangeLastMessageIdEventImpl(thread, newLastMessageId, oldLastMessageId);

            api.getEventDispatcher().dispatchServerThreadChannelChangeLastMessageIdEvent(
                    (DispatchQueueSelector) thread.getServer(), thread.getServer(), thread, event);
        }

        final int oldRateLimitPerUser = thread.getRateLimitPerUser();
        final int newRateLimitPerUser = jsonChannel.get("rate_limit_per_user").asInt();
        if (oldRateLimitPerUser != newRateLimitPerUser) {
            thread.setRateLimitPerUser(newRateLimitPerUser);

            final ServerThreadChannelChangeRateLimitPerUserEvent event =
                    new ServerThreadChannelChangeRateLimitPerUserEventImpl(thread, newRateLimitPerUser,
                            oldRateLimitPerUser);

            api.getEventDispatcher().dispatchServerThreadChannelChangeRateLimitPerUserEvent(
                    (DispatchQueueSelector) thread.getServer(), thread.getServer(), thread, event);
        }

        final ThreadMetadataImpl metadata = (ThreadMetadataImpl) thread.getMetadata();
        final JsonNode metadataJson = jsonChannel.get("thread_metadata");

        final int oldAutoArchiveDuration = metadata.getAutoArchiveDuration();
        final int newAutoArchiveDuration = metadataJson.get("auto_archive_duration").asInt();
        if (oldAutoArchiveDuration != newAutoArchiveDuration) {
            metadata.setAutoArchiveDuration(newAutoArchiveDuration);

            final ServerThreadChannelChangeAutoArchiveDurationEvent event =
                    new ServerThreadChannelChangeAutoArchiveDurationEventImpl(thread, newAutoArchiveDuration,
                            oldAutoArchiveDuration);

            api.getEventDispatcher().dispatchServerThreadChannelChangeAutoArchiveDurationEvent(
                    (DispatchQueueSelector) thread.getServer(), thread.getServer(), thread, event);
        }

        final boolean wasArchived = metadata.isArchived();
        final boolean isArchived = metadataJson.get("archived").asBoolean();
        if (wasArchived != isArchived) {
            metadata.setArchived(isArchived);

            final ServerThreadChannelChangeArchivedEvent event =
                    new ServerThreadChannelChangeArchivedEventImpl(thread, isArchived, wasArchived);

            api.getEventDispatcher().dispatchServerThreadChannelChangeArchivedEvent(
                    (DispatchQueueSelector) thread.getServer(), thread.getServer(), thread, event);
        }

        final boolean wasLocked = metadata.isLocked();
        final boolean isLocked = metadataJson.get("locked").asBoolean();
        if (wasLocked != isLocked) {
            metadata.setLocked(isLocked);

            final ServerThreadChannelChangeLockedEvent event =
                    new ServerThreadChannelChangeLockedEventImpl(thread, isLocked, wasLocked);

            api.getEventDispatcher().dispatchServerThreadChannelChangeLockedEvent(
                    (DispatchQueueSelector) thread.getServer(), thread.getServer(), thread, event);
        }

        final Instant oldArchiveTimestamp = metadata.getArchiveTimestamp();
        final Instant newArchiveTimestamp = OffsetDateTime.parse(metadataJson.get("archive_timestamp")
                .asText()).toInstant();
        if (!Objects.deepEquals(oldArchiveTimestamp, newArchiveTimestamp)) {
            metadata.setArchiveTimestamp(newArchiveTimestamp);

            final ServerThreadChannelChangeArchiveTimestampEvent event =
                    new ServerThreadChannelChangeArchiveTimestampEventImpl(thread,
                            newArchiveTimestamp, oldArchiveTimestamp);

            api.getEventDispatcher().dispatchServerThreadChannelChangeArchiveTimestampEvent(
                    (DispatchQueueSelector) thread.getServer(), thread.getServer(), thread, event);
        }

        final Boolean wasInvitable = metadata.isInvitable().orElse(null);
        final Boolean isInvitable = metadataJson.hasNonNull("invitable")
                ? metadataJson.get("invitable").asBoolean() : null;

        if (thread.isPrivate() && wasInvitable != isInvitable) {
            metadata.setInvitable(isInvitable);

            final ServerThreadChannelChangeInvitableEvent event =
                    new ServerThreadChannelChangeInvitableEventImpl(thread, isInvitable, wasInvitable);

            api.getEventDispatcher().dispatchServerThreadChannelChangeInvitableEvent(
                    (DispatchQueueSelector) thread.getServer(), thread.getServer(), thread, event);
        }

        if (jsonChannel.hasNonNull("member")) {
            final Set<ThreadMember> members = new HashSet<>(thread.getMembers());
            final ThreadMember member = new ThreadMemberImpl(api, server, jsonChannel.get("member"));
            members.add(member);
            thread.setMembers(members);

            final ServerPrivateThreadJoinEvent event =
                    new ServerPrivateThreadJoinEventImpl(thread, member);

            api.getEventDispatcher().dispatchServerPrivateThreadJoinEvent(
                    (DispatchQueueSelector) thread.getServer(), thread.getServer(), thread, event);
        }

        thread.getParent().asServerForumChannel().ifPresent(forumChannel -> {
            Set<ForumTag> allForumTags = forumChannel.getForumTags();

            Set<ForumTag> oldForumTags = thread.getForumTags();
            Set<ForumTag> newForumTags = new HashSet<>();

            List<Long> newAppliedTagIds = new ArrayList<>();
            if (jsonChannel.hasNonNull("applied_tags")) {
                for (JsonNode appliedTag : jsonChannel.get("applied_tags")) {
                    newAppliedTagIds.add(appliedTag.asLong());
                }
            }

            for (ForumTag forumTag : allForumTags) {
                if (newAppliedTagIds.contains(forumTag.getId())) {
                    newForumTags.add(forumTag);
                }
            }


            if (!Objects.deepEquals(oldForumTags, newForumTags)) {
                thread.setForumTags(newForumTags);

                final ServerThreadChannelChangeForumTagsEvent event =
                        new ServerThreadChannelChangeForumTagsEventImpl(thread, newForumTags, oldForumTags);

                api.getEventDispatcher().dispatchServerThreadChannelChangeForumTagsEvent(
                        (DispatchQueueSelector) thread.getServer(), thread.getServer(), thread, event);
            }
        });
    }
}

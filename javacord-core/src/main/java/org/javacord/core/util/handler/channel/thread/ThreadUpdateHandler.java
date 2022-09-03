package org.javacord.core.util.handler.channel.thread;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.entity.channel.ThreadMember;
import org.javacord.api.event.channel.server.ServerChannelChangeNameEvent;
import org.javacord.api.event.channel.server.thread.ServerPrivateThreadJoinEvent;
import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeArchiveTimestampEvent;
import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeArchivedEvent;
import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeAutoArchiveDurationEvent;
import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeLockedEvent;
import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeMemberCountEvent;
import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeMessageCountEvent;
import org.javacord.api.event.channel.server.thread.ServerThreadChannelChangeTotalMessageSentEvent;
import org.javacord.core.entity.channel.ServerThreadChannelImpl;
import org.javacord.core.entity.channel.ThreadMemberImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.event.channel.server.ServerChannelChangeNameEventImpl;
import org.javacord.core.event.channel.server.thread.ServerPrivateThreadJoinEventImpl;
import org.javacord.core.event.channel.server.thread.ServerThreadChannelChangeArchiveTimestampEventImpl;
import org.javacord.core.event.channel.server.thread.ServerThreadChannelChangeArchivedEventImpl;
import org.javacord.core.event.channel.server.thread.ServerThreadChannelChangeAutoArchiveDurationEventImpl;
import org.javacord.core.event.channel.server.thread.ServerThreadChannelChangeLockedEventImpl;
import org.javacord.core.event.channel.server.thread.ServerThreadChannelChangeMemberCountEventImpl;
import org.javacord.core.event.channel.server.thread.ServerThreadChannelChangeMessageCountEventImpl;
import org.javacord.core.event.channel.server.thread.ServerThreadChannelChangeTotalMessageSentEventImpl;
import org.javacord.core.util.event.DispatchQueueSelector;
import org.javacord.core.util.gateway.PacketHandler;
import org.javacord.core.util.logging.LoggerUtil;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.HashSet;
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

        final int oldAutoArchiveDuration = thread.getAutoArchiveDuration();
        final int newAutoArchiveDuration = jsonChannel.get("thread_metadata").get("auto_archive_duration")
                .asInt();
        if (oldAutoArchiveDuration != newAutoArchiveDuration) {
            thread.setAutoArchiveDuration(newAutoArchiveDuration);

            final ServerThreadChannelChangeAutoArchiveDurationEvent event =
                    new ServerThreadChannelChangeAutoArchiveDurationEventImpl(thread,
                            newAutoArchiveDuration, oldAutoArchiveDuration);

            api.getEventDispatcher()
                    .dispatchServerThreadChannelChangeAutoArchiveDurationEvent(
                            (DispatchQueueSelector) thread.getServer(), thread.getServer(), thread, event);
        }

        final boolean wasArchived = thread.isArchived();
        final boolean isArchived = jsonChannel.get("thread_metadata").get("archived").asBoolean();

        if (wasArchived != isArchived) {
            thread.setArchived(isArchived);

            final ServerThreadChannelChangeArchivedEvent event =
                    new ServerThreadChannelChangeArchivedEventImpl(thread, isArchived, wasArchived);

            api.getEventDispatcher().dispatchServerThreadChannelChangeArchivedEvent(
                    (DispatchQueueSelector) thread.getServer(), thread.getServer(), thread, event);
        }

        final boolean wasLocked = thread.isLocked();
        final boolean isLocked = jsonChannel.get("thread_metadata").get("locked").asBoolean();

        if (wasLocked != isLocked) {
            thread.setLocked(isLocked);

            final ServerThreadChannelChangeLockedEvent event =
                    new ServerThreadChannelChangeLockedEventImpl(thread, isLocked, wasLocked);

            api.getEventDispatcher().dispatchServerThreadChannelChangeLockedEvent(
                    (DispatchQueueSelector) thread.getServer(), thread.getServer(), thread, event);
        }

        final Instant oldArchiveTimestamp = thread.getArchiveTimestamp();
        final Instant newArchiveTimestamp = OffsetDateTime.parse(jsonChannel.get("thread_metadata")
                .get("archive_timestamp").asText()).toInstant();

        if (!Objects.deepEquals(oldArchiveTimestamp, newArchiveTimestamp)) {
            thread.setArchiveTimestamp(newArchiveTimestamp);

            final ServerThreadChannelChangeArchiveTimestampEvent event =
                    new ServerThreadChannelChangeArchiveTimestampEventImpl(thread,
                            newArchiveTimestamp, oldArchiveTimestamp);

            api.getEventDispatcher().dispatchServerThreadChannelChangeArchiveTimestampEvent(
                    (DispatchQueueSelector) thread.getServer(), thread.getServer(), thread, event);
        }

        final Set<ThreadMember> oldMembers = thread.getMembers();
        final Set<ThreadMember> newMembers = new HashSet<>();

        if (jsonChannel.hasNonNull("member")) {
            for (final JsonNode jsonMember : jsonChannel.get("member")) {
                final ThreadMember member = new ThreadMemberImpl(api, server, jsonMember);
                newMembers.add(member);
            }
        }

        if (!Objects.deepEquals(oldMembers, newMembers)) {
            thread.setMembers(newMembers);

            final ServerPrivateThreadJoinEvent event =
                    new ServerPrivateThreadJoinEventImpl(thread, newMembers,
                            oldMembers);

            api.getEventDispatcher().dispatchServerPrivateThreadJoinEvent(
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
    }
}

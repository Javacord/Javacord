package org.javacord.core.util.handler.message;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.CachedMessagePinEvent;
import org.javacord.api.event.message.CachedMessageUnpinEvent;
import org.javacord.api.event.message.MessageEditEvent;
import org.javacord.core.entity.message.MessageImpl;
import org.javacord.core.event.message.CachedMessagePinEventImpl;
import org.javacord.core.event.message.CachedMessageUnpinEventImpl;
import org.javacord.core.event.message.MessageEditEventImpl;
import org.javacord.core.util.event.DispatchQueueSelector;
import org.javacord.core.util.gateway.PacketHandler;
import org.javacord.core.util.logging.LoggerUtil;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Handles the message update packet.
 */
public class MessageUpdateHandler extends PacketHandler {
    private static final int MAX_EDIT_TIMEOUT = 30000;

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(MessageUpdateHandler.class);

    /**
     * A map with the last known edit timestamps.
     */
    private final ConcurrentHashMap<Long, Long> lastKnownEditTimestamps = new ConcurrentHashMap<>();

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public MessageUpdateHandler(DiscordApi api) {
        super(api, true, "MESSAGE_UPDATE");
        long offset = this.api.getTimeOffset() == null ? 0 : this.api.getTimeOffset();
        api.getThreadPool().getScheduler().scheduleAtFixedRate(() -> {
            try {
                lastKnownEditTimestamps.entrySet().removeIf(
                        entry -> System.currentTimeMillis() + offset - entry.getValue() > MAX_EDIT_TIMEOUT);
            } catch (Throwable t) {
                logger.error("Failed to clean last known edit timestamps cache!", t);
            }
        }, 1, 1, TimeUnit.MINUTES);
    }

    @Override
    public void handle(JsonNode packet) {
        long messageId = packet.get("id").asLong();
        long channelId = packet.get("channel_id").asLong();

        Optional<TextChannel> optionalChannel = api.getTextChannelById(channelId);
        if (!optionalChannel.isPresent()) {
            LoggerUtil.logMissingChannel(logger, channelId, packet);
            return;
        }

        TextChannel channel = optionalChannel.get();
        Optional<MessageImpl> cachedMessage = api.getCachedMessageById(messageId).map(msg -> (MessageImpl) msg);

        // We need a message to dispatch any event so we either need a cached message to update
        // or else we must be able to construct a new message from this event
        if (!cachedMessage.isPresent() && !isCompleteMessagePacket(packet)) {
            logger.debug("Received a message update event for an uncached message that contains not enough "
                    + "data to construct a new message in channel {}. Packet: {}", channelId, packet);
            return;
        }

        MessageImpl oldMessage = cachedMessage.map(MessageImpl::copyMessage).orElse(null);
        MessageImpl newMessage = cachedMessage.orElseGet(() -> (MessageImpl) api.getOrCreateMessage(channel, packet));

        // Figure out whether this was an intentional content edit by a user
        boolean isMostLikelyAnEdit = packet.has("edited_timestamp") && !packet.get("edited_timestamp").isNull();
        if (isMostLikelyAnEdit) {
            long editTimestamp =
                    OffsetDateTime.parse(packet.get("edited_timestamp").asText()).toInstant().toEpochMilli();
            long lastKnownEditTimestamp = lastKnownEditTimestamps.getOrDefault(messageId, 0L);
            lastKnownEditTimestamps.put(messageId, editTimestamp);

            long offset = api.getTimeOffset() == null ? 0 : api.getTimeOffset();
            if (editTimestamp == lastKnownEditTimestamp) {
                isMostLikelyAnEdit = false;
            } else if (System.currentTimeMillis() + offset - editTimestamp > MAX_EDIT_TIMEOUT) {
                isMostLikelyAnEdit = false;
            }
        }

        newMessage.setUpdatableFields(packet);

        if (cachedMessage.isPresent() && packet.hasNonNull("pinned")) {
            boolean newPinnedFlag = newMessage.isPinned();
            boolean oldPinnedFlag = oldMessage.isPinned();

            if (newPinnedFlag != oldPinnedFlag) {
                if (newPinnedFlag) {
                    CachedMessagePinEvent event = new CachedMessagePinEventImpl(newMessage);

                    Optional<Server> optionalServer =
                            newMessage.getChannel().asServerChannel().map(ServerChannel::getServer);
                    api.getEventDispatcher().dispatchCachedMessagePinEvent(
                            optionalServer.map(DispatchQueueSelector.class::cast).orElse(api),
                            newMessage,
                            optionalServer.orElse(null),
                            newMessage.getChannel(),
                            event);
                } else {
                    CachedMessageUnpinEvent event = new CachedMessageUnpinEventImpl(newMessage);

                    Optional<Server> optionalServer =
                            newMessage.getChannel().asServerChannel().map(ServerChannel::getServer);
                    api.getEventDispatcher().dispatchCachedMessageUnpinEvent(
                            optionalServer.map(DispatchQueueSelector.class::cast).orElse(api),
                            newMessage,
                            optionalServer.orElse(null),
                            newMessage.getChannel(),
                            event);
                }
            }
        }

        MessageEditEventImpl editEvent = new MessageEditEventImpl(api, messageId, channel, newMessage,
                oldMessage, isMostLikelyAnEdit);
        dispatchEditEvent(editEvent);
    }

    /**
     * Dispatches an edit event.
     *
     * @param event The event to dispatch.
     */
    private void dispatchEditEvent(MessageEditEvent event) {
        Optional<Server> optionalServer =
                event.getChannel().asServerChannel().map(ServerChannel::getServer);
        api.getEventDispatcher().dispatchMessageEditEvent(
                optionalServer.map(DispatchQueueSelector.class::cast).orElse(api),
                event.getMessageId(),
                optionalServer.orElse(null),
                event.getChannel(),
                event);
    }

    /**
     * Check the message update packet for some core fields whose presence indicates
     * that this packet contains the entire message.
     *
     * @param packet the message update event
     * @return true if this event contains a full message; false otherwise
     */
    private static boolean isCompleteMessagePacket(JsonNode packet) {
        return packet.hasNonNull("type")
                && packet.hasNonNull("author")
                && packet.hasNonNull("content")
                && packet.hasNonNull("timestamp")
                && packet.hasNonNull("embeds");
    }

}

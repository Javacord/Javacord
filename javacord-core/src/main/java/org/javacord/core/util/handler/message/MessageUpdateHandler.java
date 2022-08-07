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
                        entry -> System.currentTimeMillis() + offset - entry.getValue() > 5000);
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
            LoggerUtil.logMissingChannel(logger, channelId);
            return;
        }

        TextChannel channel = optionalChannel.get();
        Optional<MessageImpl> message = api.getCachedMessageById(messageId).map(msg -> (MessageImpl) msg);

        MessageImpl oldMessage = message.map(MessageImpl::copyMessage).orElse(null);
        MessageImpl newMessage;

        MessageEditEvent editEvent = null;
        if (packet.has("edited_timestamp") && !packet.get("edited_timestamp").isNull()) {
            long editTimestamp =
                    OffsetDateTime.parse(packet.get("edited_timestamp").asText()).toInstant().toEpochMilli();
            long lastKnownEditTimestamp = lastKnownEditTimestamps.getOrDefault(messageId, 0L);
            lastKnownEditTimestamps.put(messageId, editTimestamp);

            boolean isMostLikelyAnEdit = true;
            long offset = api.getTimeOffset() == null ? 0 : api.getTimeOffset();
            if (editTimestamp == lastKnownEditTimestamp) {
                isMostLikelyAnEdit = false;
            } else if (System.currentTimeMillis() + offset - editTimestamp > 5000) {
                isMostLikelyAnEdit = false;
            }

            if (message.isPresent()) {
                newMessage = message.get();
                boolean newPinnedFlag = packet.hasNonNull("pinned")
                        ? packet.get("pinned").asBoolean()
                        : newMessage.isPinned();
                boolean oldPinnedFlag = newMessage.isPinned();
                newMessage.setUpdatableFields(packet);

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
            } else {
                newMessage = (MessageImpl) api.getOrCreateMessage(channel, packet);
            }

            if (isMostLikelyAnEdit) {
                editEvent = new MessageEditEventImpl(api, messageId, channel, newMessage, oldMessage, false);
            }
        } else {
            //Handle special cases where the edited_timestamp is not present or null like embedding links
            if (message.isPresent()) {
                newMessage = message.get();
                if (!newMessage.setSpecialUpdatableFields(packet)) {
                    logger.warn("Received a not handled special message update packet {}", packet.toString());
                }
            } else {
                if (packet.path("edited_timestamp").isNull()) {
                    newMessage = (MessageImpl) api.getOrCreateMessage(channel, packet);
                    return;
                } else {
                    //Cases that are unknown or not meaningful
                    return;
                }
            }
            editEvent = new MessageEditEventImpl(api, messageId, channel, newMessage, oldMessage, true);
        }

        if (editEvent != null) {
            dispatchEditEvent(editEvent);
        }
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

}
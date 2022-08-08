package org.javacord.core.util.handler.message;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.Embed;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.CachedMessagePinEvent;
import org.javacord.api.event.message.CachedMessageUnpinEvent;
import org.javacord.api.event.message.MessageEditEvent;
import org.javacord.core.entity.message.MessageImpl;
import org.javacord.core.entity.message.embed.EmbedBuilderDelegateImpl;
import org.javacord.core.entity.message.embed.EmbedImpl;
import org.javacord.core.event.message.CachedMessagePinEventImpl;
import org.javacord.core.event.message.CachedMessageUnpinEventImpl;
import org.javacord.core.event.message.MessageEditEventImpl;
import org.javacord.core.util.event.DispatchQueueSelector;
import org.javacord.core.util.gateway.PacketHandler;
import org.javacord.core.util.logging.LoggerUtil;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
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

        message.ifPresent(msg -> {
            boolean newPinnedFlag = packet.hasNonNull("pinned") ? packet.get("pinned").asBoolean() : msg.isPinned();
            boolean oldPinnedFlag = msg.isPinned();
            if (newPinnedFlag != oldPinnedFlag) {
                msg.setPinned(newPinnedFlag);

                if (newPinnedFlag) {
                    CachedMessagePinEvent event = new CachedMessagePinEventImpl(msg);

                    Optional<Server> optionalServer =
                            msg.getChannel().asServerChannel().map(ServerChannel::getServer);
                    api.getEventDispatcher().dispatchCachedMessagePinEvent(
                            optionalServer.map(DispatchQueueSelector.class::cast).orElse(api),
                            msg,
                            optionalServer.orElse(null),
                            msg.getChannel(),
                            event);
                } else {
                    CachedMessageUnpinEvent event = new CachedMessageUnpinEventImpl(msg);

                    Optional<Server> optionalServer =
                            msg.getChannel().asServerChannel().map(ServerChannel::getServer);
                    api.getEventDispatcher().dispatchCachedMessageUnpinEvent(
                            optionalServer.map(DispatchQueueSelector.class::cast).orElse(api),
                            msg,
                            optionalServer.orElse(null),
                            msg.getChannel(),
                            event);
                }
            }
        });

        MessageEditEvent editEvent = null;
        if (packet.has("edited_timestamp") && !packet.get("edited_timestamp").isNull()) {
            message.ifPresent(msg -> {
                msg.setLastEditTime(OffsetDateTime.parse(packet.get("edited_timestamp").asText()).toInstant());
                msg.setMentionsEveryone(packet.get("mention_everyone").asBoolean());
            });

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

            String oldContent = message.map(Message::getContent).orElse(null);
            List<Embed> oldEmbeds = message.map(Message::getEmbeds).orElse(null);

            String newContent = null;
            if (packet.has("content")) {
                newContent = packet.get("content").asText();
                String finalNewContent = newContent;
                message.ifPresent(msg -> msg.setContent(finalNewContent));
            }
            List<Embed> newEmbeds = null;
            if (packet.has("embeds")) {
                newEmbeds = new ArrayList<>();
                for (JsonNode embedJson : packet.get("embeds")) {
                    Embed embed = new EmbedImpl(embedJson);
                    newEmbeds.add(embed);
                }
                List<Embed> finalNewEmbeds = newEmbeds;
                message.ifPresent(msg -> msg.setEmbeds(finalNewEmbeds));
            }

            if (oldContent != null && newContent != null && !oldContent.equals(newContent)) {
                // If the old content doesn't match the new content it's for sure an edit
                isMostLikelyAnEdit = true;
            }

            if (oldEmbeds != null && newEmbeds != null) {
                if (newEmbeds.size() != oldEmbeds.size()) {
                    isMostLikelyAnEdit = true;
                } else {
                    for (int i = 0; i < newEmbeds.size(); i++) {
                        if (!((EmbedBuilderDelegateImpl) newEmbeds.get(i)
                                .toBuilder().getDelegate()).toJsonNode().toString()
                                .equals(((EmbedBuilderDelegateImpl) oldEmbeds.get(i)
                                        .toBuilder().getDelegate()).toJsonNode().toString())) {
                            isMostLikelyAnEdit = true;
                        }
                    }
                }
            }

            if (isMostLikelyAnEdit) {
                editEvent = new MessageEditEventImpl(
                        api, messageId, channel, newContent, newEmbeds, oldContent, oldEmbeds);
            }
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

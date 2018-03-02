package de.btobastian.javacord.util.handler.message;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entity.channel.ServerChannel;
import de.btobastian.javacord.entity.channel.ServerTextChannel;
import de.btobastian.javacord.entity.message.Message;
import de.btobastian.javacord.entity.message.embed.Embed;
import de.btobastian.javacord.entity.message.embed.impl.ImplEmbed;
import de.btobastian.javacord.entity.message.embed.impl.ImplEmbedFactory;
import de.btobastian.javacord.entity.message.impl.ImplMessage;
import de.btobastian.javacord.entity.server.Server;
import de.btobastian.javacord.event.message.CachedMessagePinEvent;
import de.btobastian.javacord.event.message.CachedMessageUnpinEvent;
import de.btobastian.javacord.event.message.MessageEditEvent;
import de.btobastian.javacord.listener.message.CachedMessagePinListener;
import de.btobastian.javacord.listener.message.CachedMessageUnpinListener;
import de.btobastian.javacord.listener.message.MessageEditListener;
import de.btobastian.javacord.util.gateway.PacketHandler;

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
        api.getThreadPool().getScheduler().scheduleAtFixedRate(
                () -> lastKnownEditTimestamps.entrySet().removeIf(
                        entry -> System.currentTimeMillis() + offset - entry.getValue() > 5000)
                , 1, 1, TimeUnit.MINUTES);
    }

    @Override
    public void handle(JsonNode packet) {
        long messageId = packet.get("id").asLong();
        long channelId = packet.get("channel_id").asLong();

        api.getTextChannelById(channelId).ifPresent(channel -> {
            Optional<ImplMessage> message = api.getCachedMessageById(messageId).map(msg -> (ImplMessage) msg);

            message.ifPresent(msg -> {
                boolean newPinnedFlag = packet.hasNonNull("pinned") ? packet.get("pinned").asBoolean() : msg.isPinned();
                boolean oldPinnedFlag = msg.isPinned();
                if (newPinnedFlag != oldPinnedFlag) {
                    msg.setPinned(newPinnedFlag);

                    if (newPinnedFlag) {
                        CachedMessagePinEvent event = new CachedMessagePinEvent(msg);

                        List<CachedMessagePinListener> listeners = new ArrayList<>();
                        listeners.addAll(msg.getCachedMessagePinListeners());
                        listeners.addAll(msg.getChannel().getCachedMessagePinListeners());
                        msg.getChannel().asServerChannel()
                                .map(ServerChannel::getServer)
                                .map(Server::getCachedMessagePinListeners)
                                .ifPresent(listeners::addAll);
                        listeners.addAll(api.getCachedMessagePinListeners());

                        if (msg.getChannel() instanceof ServerChannel) {
                            api.getEventDispatcher().dispatchEvent(((ServerChannel) msg.getChannel()).getServer(),
                                    listeners, listener -> listener.onCachedMessagePin(event));
                        } else {
                            api.getEventDispatcher().dispatchEvent(
                                    api, listeners, listener -> listener.onCachedMessagePin(event));
                        }
                    } else {
                        CachedMessageUnpinEvent event = new CachedMessageUnpinEvent(msg);

                        List<CachedMessageUnpinListener> listeners = new ArrayList<>();
                        listeners.addAll(msg.getCachedMessageUnpinListeners());
                        listeners.addAll(msg.getChannel().getCachedMessageUnpinListeners());
                        msg.getChannel().asServerChannel()
                                .map(ServerChannel::getServer)
                                .map(Server::getCachedMessageUnpinListeners)
                                .ifPresent(listeners::addAll);
                        listeners.addAll(api.getCachedMessageUnpinListeners());

                        if (msg.getChannel() instanceof ServerChannel) {
                            api.getEventDispatcher().dispatchEvent(((ServerChannel) msg.getChannel()).getServer(),
                                    listeners, listener -> listener.onCachedMessageUnpin(event));
                        } else {
                            api.getEventDispatcher().dispatchEvent(
                                    api, listeners, listener -> listener.onCachedMessageUnpin(event));
                        }
                    }
                }
            });

            MessageEditEvent editEvent = null;
            if (packet.has("edited_timestamp") && !packet.get("edited_timestamp").isNull()) {
                message.ifPresent(msg ->
                        msg.setLastEditTime(OffsetDateTime.parse(packet.get("edited_timestamp").asText()).toInstant()));

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
                        Embed embed = new ImplEmbed(embedJson);
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
                            if (!((ImplEmbedFactory) newEmbeds.get(i)
                                    .toBuilder().getFactory()).toJsonNode().toString()
                                    .equals(((ImplEmbedFactory) oldEmbeds.get(i)
                                            .toBuilder().getFactory()).toJsonNode().toString())) {
                                isMostLikelyAnEdit = true;
                            }
                        }
                    }
                }

                if (isMostLikelyAnEdit) {
                    editEvent =
                            new MessageEditEvent(api, messageId, channel, newContent, newEmbeds, oldContent, oldEmbeds);
                }
            }

            if (editEvent != null) {
                dispatchEditEvent(editEvent);
            }
        });
    }

    /**
     * Dispatches an edit event.
     *
     * @param event The event to dispatch.
     */
    private void dispatchEditEvent(MessageEditEvent event) {
        List<MessageEditListener> listeners = new ArrayList<>();
        listeners.addAll(Message.getMessageEditListeners(api, event.getMessageId()));
        listeners.addAll(event.getChannel().getMessageEditListeners());
        if (event.getChannel() instanceof ServerTextChannel) {
            listeners.addAll(((ServerTextChannel) event.getChannel()).getServer().getMessageEditListeners());
        }
        listeners.addAll(api.getMessageEditListeners());

        if (event.getChannel() instanceof ServerTextChannel) {
            api.getEventDispatcher().dispatchEvent(((ServerTextChannel) event.getChannel()).getServer(),
                    listeners, listener -> listener.onMessageEdit(event));
        } else {
            api.getEventDispatcher().dispatchEvent(api, listeners, listener -> listener.onMessageEdit(event));
        }
    }

}
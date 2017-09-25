package de.btobastian.javacord.utils.handler.message;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.Embed;
import de.btobastian.javacord.entities.message.embed.impl.ImplEmbed;
import de.btobastian.javacord.entities.message.impl.ImplMessage;
import de.btobastian.javacord.events.message.MessageEditEvent;
import de.btobastian.javacord.listeners.message.MessageEditListener;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONArray;
import org.json.JSONObject;

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
    public void handle(JSONObject packet) {
        long messageId = Long.parseLong(packet.getString("id"));
        long channelId = Long.parseLong(packet.getString("channel_id"));

        api.getTextChannelById(channelId).ifPresent(channel -> {
            Optional<ImplMessage> message = api.getCachedMessageById(messageId).map(msg -> (ImplMessage) msg);

            MessageEditEvent editEvent = null;
            if (packet.has("edited_timestamp") && !packet.isNull("edited_timestamp")) {
                long editTimestamp =
                        OffsetDateTime.parse(packet.getString("edited_timestamp")).toInstant().toEpochMilli();
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
                    newContent = packet.getString("content");
                    String finalNewContent = newContent;
                    message.ifPresent(msg -> msg.setContent(finalNewContent));
                }
                List<Embed> newEmbeds = null;
                if (packet.has("embeds")) {
                    newEmbeds = new ArrayList<>();
                    JSONArray embedsJson = packet.getJSONArray("embeds");
                    for (int i = 0; i < embedsJson.length(); i++) {
                        Embed embed = new ImplEmbed(embedsJson.getJSONObject(i));
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
                            if (!newEmbeds.get(i).toBuilder().toJSONObject().toString()
                                    .equals(oldEmbeds.get(i).toBuilder().toJSONObject().toString())) {
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
        listeners.addAll(api.getMessageEditListeners(event.getMessageId()));
        listeners.addAll(event.getChannel().getMessageEditListeners());
        if (event.getChannel() instanceof ServerTextChannel) {
            listeners.addAll(((ServerTextChannel) event.getChannel()).getServer().getMessageEditListeners());
        }
        listeners.addAll(api.getMessageEditListeners());

        dispatchEvent(listeners, listener -> listener.onMessageEdit(event));
    }

}
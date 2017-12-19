package de.btobastian.javacord.utils.handler.message;

import com.fasterxml.jackson.databind.JsonNode;
import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.message.impl.ImplMessage;
import de.btobastian.javacord.events.message.MessageDeleteEvent;
import de.btobastian.javacord.listeners.message.MessageDeleteListener;
import de.btobastian.javacord.utils.PacketHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the message delete bulk packet.
 */
public class MessageDeleteBulkHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public MessageDeleteBulkHandler(DiscordApi api) {
        super(api, true, "MESSAGE_DELETE_BULK");
    }

    @Override
    public void handle(JsonNode packet) {
        long channelId = Long.parseLong(packet.get("channel_id").asText());

        api.getTextChannelById(channelId).ifPresent(channel -> {
            for (JsonNode messageIdJson : packet.get("ids")) {
                long messageId = messageIdJson.asLong();
                MessageDeleteEvent event = new MessageDeleteEvent(api, messageId, channel);

                List<MessageDeleteListener> listeners = new ArrayList<>();
                api.getCachedMessageById(messageId)
                        .ifPresent(message -> ((ImplMessage) message).setDeleted(true));
                listeners.addAll(api.getMessageDeleteListeners(messageId));
                listeners.addAll(channel.getMessageDeleteListeners());
                if (channel instanceof ServerTextChannel) {
                    listeners.addAll(((ServerTextChannel) channel).getServer().getMessageDeleteListeners());
                }
                listeners.addAll(api.getMessageDeleteListeners());

                dispatchEvent(listeners, listener -> listener.onMessageDelete(event));
            }
        });
    }
}
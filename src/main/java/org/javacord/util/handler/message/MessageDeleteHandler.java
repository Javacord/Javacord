package org.javacord.util.handler.message;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.DiscordApi;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.message.Message;
import org.javacord.event.message.MessageDeleteEvent;
import org.javacord.listener.message.MessageDeleteListener;
import org.javacord.util.cache.ImplMessageCache;
import org.javacord.util.gateway.PacketHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the message delete packet.
 */
public class MessageDeleteHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public MessageDeleteHandler(DiscordApi api) {
        super(api, true, "MESSAGE_DELETE");
    }

    @Override
    public void handle(JsonNode packet) {
        long messageId = packet.get("id").asLong();
        long channelId = packet.get("channel_id").asLong();

        api.getTextChannelById(channelId).ifPresent(channel -> {
            MessageDeleteEvent event = new MessageDeleteEvent(api, messageId, channel);

            List<MessageDeleteListener> listeners = new ArrayList<>();
            api.getCachedMessageById(messageId)
                    .ifPresent(((ImplMessageCache) channel.getMessageCache())::removeMessage);
            api.removeMessageFromCache(messageId);
            listeners.addAll(Message.getMessageDeleteListeners(api, messageId));
            listeners.addAll(channel.getMessageDeleteListeners());
            if (channel instanceof ServerTextChannel) {
                listeners.addAll(((ServerTextChannel) channel).getServer().getMessageDeleteListeners());
            }
            listeners.addAll(api.getMessageDeleteListeners());

            if (channel instanceof ServerTextChannel) {
                api.getEventDispatcher().dispatchEvent(((ServerTextChannel) channel).getServer(),
                        listeners, listener -> listener.onMessageDelete(event));
            } else {
                api.getEventDispatcher().dispatchEvent(api, listeners, listener -> listener.onMessageDelete(event));
            }
        });
    }
}
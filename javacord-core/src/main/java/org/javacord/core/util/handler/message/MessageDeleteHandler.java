package org.javacord.core.util.handler.message;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.event.message.MessageDeleteEvent;
import org.javacord.api.listener.message.MessageAttachableListenerManager;
import org.javacord.api.listener.message.MessageDeleteListener;
import org.javacord.core.event.message.MessageDeleteEventImpl;
import org.javacord.core.util.cache.MessageCacheImpl;
import org.javacord.core.util.gateway.PacketHandler;

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
            MessageDeleteEvent event = new MessageDeleteEventImpl(api, messageId, channel);

            List<MessageDeleteListener> listeners = new ArrayList<>();
            api.getCachedMessageById(messageId)
                    .ifPresent(((MessageCacheImpl) channel.getMessageCache())::removeMessage);
            api.removeMessageFromCache(messageId);
            listeners.addAll(MessageAttachableListenerManager.getMessageDeleteListeners(api, messageId));
            listeners.addAll(channel.getMessageDeleteListeners());
            if (channel instanceof ServerChannel) {
                listeners.addAll(((ServerChannel) channel).getServer().getMessageDeleteListeners());
            }
            listeners.addAll(api.getMessageDeleteListeners());

            if (channel instanceof ServerChannel) {
                api.getEventDispatcher().dispatchEvent(((ServerChannel) channel).getServer(),
                        listeners, listener -> listener.onMessageDelete(event));
            } else {
                api.getEventDispatcher().dispatchEvent(api, listeners, listener -> listener.onMessageDelete(event));
            }
        });
    }
}
package org.javacord.core.util.handler.message.reaction;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.reaction.ReactionRemoveAllEvent;
import org.javacord.api.listener.message.MessageAttachableListenerManager;
import org.javacord.api.listener.message.reaction.ReactionRemoveAllListener;
import org.javacord.core.entity.message.MessageImpl;
import org.javacord.core.event.message.reaction.ReactionRemoveAllEventImpl;
import org.javacord.core.util.gateway.PacketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Handles the message reaction remove all packet.
 */
public class MessageReactionRemoveAllHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public MessageReactionRemoveAllHandler(DiscordApi api) {
        super(api, true, "MESSAGE_REACTION_REMOVE_ALL");
    }

    @Override
    public void handle(JsonNode packet) {
        api.getTextChannelById(packet.get("channel_id").asText()).ifPresent(channel -> {
            long messageId = packet.get("message_id").asLong();
            Optional<Message> message = api.getCachedMessageById(messageId);

            message.ifPresent(msg -> ((MessageImpl) msg).removeAllReactionsFromCache());

            ReactionRemoveAllEvent event = new ReactionRemoveAllEventImpl(api, messageId, channel);

            List<ReactionRemoveAllListener> listeners = new ArrayList<>();
            listeners.addAll(MessageAttachableListenerManager.getReactionRemoveAllListeners(api, messageId));
            listeners.addAll(channel.getReactionRemoveAllListeners());
            if (channel instanceof ServerChannel) {
                listeners.addAll(((ServerChannel) channel).getServer().getReactionRemoveAllListeners());
            }
            listeners.addAll(api.getReactionRemoveAllListeners());

            if (channel instanceof ServerChannel) {
                api.getEventDispatcher().dispatchEvent(((ServerChannel) channel).getServer(),
                        listeners, listener -> listener.onReactionRemoveAll(event));
            } else {
                api.getEventDispatcher().dispatchEvent(api, listeners, listener -> listener.onReactionRemoveAll(event));
            }
        });
    }

}
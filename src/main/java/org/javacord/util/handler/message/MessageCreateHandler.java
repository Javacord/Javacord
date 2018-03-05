package org.javacord.util.handler.message;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.DiscordApi;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.message.Message;
import org.javacord.event.message.MessageCreateEvent;
import org.javacord.listener.message.MessageCreateListener;
import org.javacord.util.gateway.PacketHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the message create packet.
 */
public class MessageCreateHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public MessageCreateHandler(DiscordApi api) {
        super(api, true, "MESSAGE_CREATE");
    }

    @Override
    public void handle(JsonNode packet) {
        api.getTextChannelById(packet.get("channel_id").asText()).ifPresent(channel -> {
            Message message = api.getOrCreateMessage(channel, packet);
            MessageCreateEvent event = new MessageCreateEvent(api, message);

            List<MessageCreateListener> listeners = new ArrayList<>();
            listeners.addAll(channel.getMessageCreateListeners());
            if (channel instanceof ServerTextChannel) {
                listeners.addAll(((ServerTextChannel) channel).getServer().getMessageCreateListeners());
            }
            message.getUserAuthor().ifPresent(user -> listeners.addAll(user.getMessageCreateListeners()));
            listeners.addAll(api.getMessageCreateListeners());

            if (channel instanceof ServerTextChannel) {
                api.getEventDispatcher().dispatchEvent(((ServerTextChannel) channel).getServer(),
                        listeners, listener -> listener.onMessageCreate(event));
            } else {
                api.getEventDispatcher().dispatchEvent(api, listeners, listener -> listener.onMessageCreate(event));
            }
        });
    }

}
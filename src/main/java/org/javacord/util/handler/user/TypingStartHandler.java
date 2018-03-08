package org.javacord.util.handler.user;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.DiscordApi;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.event.user.UserStartTypingEvent;
import org.javacord.event.user.impl.ImplUserStartTypingEvent;
import org.javacord.listener.user.UserStartTypingListener;
import org.javacord.util.gateway.PacketHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the typing start packet.
 */
public class TypingStartHandler extends PacketHandler {

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public TypingStartHandler(DiscordApi api) {
        super(api, true, "TYPING_START");
    }

    @Override
    public void handle(JsonNode packet) {
        long userId = packet.get("user_id").asLong();
        long channelId = packet.get("channel_id").asLong();
        api.getTextChannelById(channelId).ifPresent(channel -> api.getCachedUserById(userId).ifPresent(user -> {
            UserStartTypingEvent event = new ImplUserStartTypingEvent(user, channel);

            List<UserStartTypingListener> listeners = new ArrayList<>();
            listeners.addAll(channel.getUserStartTypingListeners());
            if (channel instanceof ServerTextChannel) {
                listeners.addAll(((ServerTextChannel) channel).getServer().getUserStartTypingListeners());
            }
            listeners.addAll(user.getUserStartTypingListeners());
            listeners.addAll(api.getUserStartTypingListeners());

            if (channel instanceof ServerTextChannel) {
                api.getEventDispatcher().dispatchEvent(((ServerTextChannel) channel).getServer(),
                        listeners, listener -> listener.onUserStartTyping(event));
            } else {
                api.getEventDispatcher().dispatchEvent(api, listeners, listener -> listener.onUserStartTyping(event));
            }
        }));
    }

}
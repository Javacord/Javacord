package org.javacord.core.util.handler.user;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.user.UserStartTypingEvent;
import org.javacord.core.event.user.UserStartTypingEventImpl;
import org.javacord.core.util.event.DispatchQueueSelector;
import org.javacord.core.util.gateway.PacketHandler;

import java.util.Optional;

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
            UserStartTypingEvent event = new UserStartTypingEventImpl(user, channel);

            Optional<Server> optionalServer = channel.asServerChannel().map(ServerChannel::getServer);
            api.getEventDispatcher().dispatchUserStartTypingEvent(
                    optionalServer.map(DispatchQueueSelector.class::cast).orElse(api),
                    optionalServer.orElse(null),
                    channel,
                    user,
                    event);
        }));
    }

}

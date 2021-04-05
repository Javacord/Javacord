package org.javacord.core.util.handler.message;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.core.entity.channel.PrivateChannelImpl;
import org.javacord.core.entity.user.MemberImpl;
import org.javacord.core.entity.user.UserImpl;
import org.javacord.core.event.message.MessageCreateEventImpl;
import org.javacord.core.util.event.DispatchQueueSelector;
import org.javacord.core.util.gateway.PacketHandler;
import org.javacord.core.util.logging.LoggerUtil;

import java.util.Optional;

/**
 * Handles the message create packet.
 */
public class MessageCreateHandler extends PacketHandler {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(MessageCreateHandler.class);

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
        long channelId = packet.get("channel_id").asLong();

        // if the message isn't from a server
        // See https://github.com/discord/discord-api-docs/issues/2248
        if (!packet.hasNonNull("guild_id")) {
            UserImpl author = new UserImpl(api, packet.get("author"), (MemberImpl) null, null);

            PrivateChannelImpl privateChannel = PrivateChannelImpl
                    .getOrCreatePrivateChannel(api, channelId, author.getId(), author);

            handle(privateChannel, packet);
            return;
        }

        Optional<TextChannel> optionalChannel = api.getTextChannelById(channelId);
        if (optionalChannel.isPresent()) {
            handle(optionalChannel.get(), packet);
        } else {
            LoggerUtil.logMissingChannel(logger, channelId);
        }
    }

    private void handle(TextChannel channel, JsonNode packet) {
        Message message = api.getOrCreateMessage(channel, packet);
        MessageCreateEvent event = new MessageCreateEventImpl(message);

        Optional<Server> optionalServer = channel.asServerChannel().map(ServerChannel::getServer);
        MessageAuthor author = message.getAuthor();
        api.getEventDispatcher().dispatchMessageCreateEvent(
                optionalServer.map(DispatchQueueSelector.class::cast).orElse(api),
                optionalServer.orElse(null),
                channel,
                author.asUser().orElse(null),
                author.isWebhook() ? author.getId() : null,
                event);
    }

}

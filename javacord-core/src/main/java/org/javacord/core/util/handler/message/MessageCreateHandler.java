package org.javacord.core.util.handler.message;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerThreadChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.event.message.MessageReplyEvent;
import org.javacord.core.entity.channel.PrivateChannelImpl;
import org.javacord.core.entity.channel.ServerThreadChannelImpl;
import org.javacord.core.entity.user.MemberImpl;
import org.javacord.core.entity.user.UserImpl;
import org.javacord.core.event.message.MessageCreateEventImpl;
import org.javacord.core.event.message.MessageReplyEventImpl;
import org.javacord.core.util.event.DispatchQueueSelector;
import org.javacord.core.util.gateway.PacketHandler;
import org.javacord.core.util.logging.LoggerUtil;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

        // if the message isn't from a server (or ephemeral)
        // See https://github.com/discord/discord-api-docs/issues/2248
        if (!packet.hasNonNull("guild_id")) {
            // Check for EPHEMERAL messages as they do NOT include a guild_id when the EPHEMERAL flag is set.
            if (packet.hasNonNull("flags") && (packet.get("flags").asInt() & MessageFlag.EPHEMERAL.getId()) > 0) {
                Optional<ServerTextChannel> serverTextChannel = api.getServerTextChannelById(channelId);
                if (serverTextChannel.isPresent()) {
                    handle(serverTextChannel.get(), packet);
                    return;
                }
                Optional<ServerThreadChannel> serverThreadChannel = api.getServerThreadChannelById(channelId);
                if (serverThreadChannel.isPresent()) {
                    handle(serverThreadChannel.get(), packet);
                    return;
                }
            }

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

        message.getServerThreadChannel().ifPresent(stc -> {
            ((ServerThreadChannelImpl) stc).setTotalNumberOfMessagesSent(stc.getTotalNumberOfMessagesSent() + 1);
        });

        api.getEventDispatcher().dispatchMessageCreateEvent(
                optionalServer.map(DispatchQueueSelector.class::cast).orElse(api),
                optionalServer.orElse(null),
                channel,
                author.asUser().orElse(null),
                author.isWebhook() ? author.getId() : null,
                event);

        message.getReferencedMessage().ifPresent(referencedMessage -> {
            MessageReplyEvent replyEvent = new MessageReplyEventImpl(message, referencedMessage);
            Set<User> users = new HashSet<>();
            referencedMessage.getUserAuthor().ifPresent(users::add);
            message.getUserAuthor().ifPresent(users::add);

            Set<Long> webhookIds =  new HashSet<>();
            message.getAuthor().getWebhookId().ifPresent(webhookIds::add);
            referencedMessage.getAuthor().getWebhookId().ifPresent(webhookIds::add);

            api.getEventDispatcher().dispatchMessageReplyEvent(
                    optionalServer.map(DispatchQueueSelector.class::cast).orElse(api),
                    Collections.singleton(referencedMessage),
                    optionalServer.map(Collections::singleton).orElse(null),
                    Collections.singleton(message.getChannel()),
                    users,
                    webhookIds,
                    replyEvent
            );
        });
    }

}

package org.javacord.core.util.handler.message.reaction;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.reaction.ReactionRemoveEvent;
import org.javacord.core.entity.channel.PrivateChannelImpl;
import org.javacord.core.entity.emoji.UnicodeEmojiImpl;
import org.javacord.core.entity.message.MessageImpl;
import org.javacord.core.event.message.reaction.ReactionRemoveEventImpl;
import org.javacord.core.util.event.DispatchQueueSelector;
import org.javacord.core.util.gateway.PacketHandler;
import org.javacord.core.util.logging.LoggerUtil;

import java.util.Optional;

/**
 * Handles the message reaction remove packet.
 */
public class MessageReactionRemoveHandler extends PacketHandler {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(MessageReactionRemoveHandler.class);

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public MessageReactionRemoveHandler(DiscordApi api) {
        super(api, true, "MESSAGE_REACTION_REMOVE");
    }

    @Override
    public void handle(JsonNode packet) {
        long messageId = packet.get("message_id").asLong();
        long userId = packet.get("user_id").asLong();
        Optional<Message> message = api.getCachedMessageById(messageId);

        long channelId = packet.get("channel_id").asLong();
        TextChannel channel;
        if (packet.hasNonNull("guild_id")) {
            channel = api.getTextChannelById(channelId).orElse(null);
        } else { // if private channel:
            channel = PrivateChannelImpl.getOrCreatePrivateChannel(api, channelId, userId, null);
        }

        if (channel == null) {
            LoggerUtil.logMissingChannel(logger, channelId);
            return;
        }

        Emoji emoji;
        JsonNode emojiJson = packet.get("emoji");
        if (!emojiJson.has("id") || emojiJson.get("id").isNull()) {
            emoji = UnicodeEmojiImpl.fromString(emojiJson.get("name").asText());
        } else {
            emoji = api.getKnownCustomEmojiOrCreateCustomEmoji(emojiJson);
        }

        boolean isSuperReaction = packet.get("burst").asBoolean();

        message.ifPresent(msg -> ((MessageImpl) msg).removeReaction(emoji, userId == api.getYourself().getId(),
                isSuperReaction));

        ReactionRemoveEvent event = new ReactionRemoveEventImpl(api, messageId, channel, emoji, userId);

        Optional<Server> optionalServer = channel.asServerChannel().map(ServerChannel::getServer);
        api.getEventDispatcher().dispatchReactionRemoveEvent(
                optionalServer.map(DispatchQueueSelector.class::cast).orElse(api),
                messageId,
                optionalServer.orElse(null),
                channel,
                userId,
                event);
    }

}

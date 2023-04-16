package org.javacord.core.util.handler.message.reaction;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.reaction.ReactionAddEvent;
import org.javacord.core.entity.channel.PrivateChannelImpl;
import org.javacord.core.entity.emoji.UnicodeEmojiImpl;
import org.javacord.core.entity.message.MessageImpl;
import org.javacord.core.entity.server.ServerImpl;
import org.javacord.core.entity.user.Member;
import org.javacord.core.entity.user.MemberImpl;
import org.javacord.core.event.message.reaction.ReactionAddEventImpl;
import org.javacord.core.util.event.DispatchQueueSelector;
import org.javacord.core.util.gateway.PacketHandler;
import org.javacord.core.util.logging.LoggerUtil;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Handles the message reaction add packet.
 */
public class MessageReactionAddHandler extends PacketHandler {

    /**
     * The logger of this class.
     */
    private static final Logger logger = LoggerUtil.getLogger(MessageReactionAddHandler.class);

    /**
     * Creates a new instance of this class.
     *
     * @param api The api.
     */
    public MessageReactionAddHandler(DiscordApi api) {
        super(api, true, "MESSAGE_REACTION_ADD");
    }

    @Override
    public void handle(JsonNode packet) {
        long channelId = packet.get("channel_id").asLong();
        long messageId = packet.get("message_id").asLong();
        long userId = packet.get("user_id").asLong();
        String serverId = packet.hasNonNull("guild_id") ? packet.get("guild_id").asText() : null;

        TextChannel channel;
        if (serverId == null) { // if private channel:
            channel = PrivateChannelImpl.getOrCreatePrivateChannel(api, channelId, userId, null);
        } else {
            channel = api.getTextChannelById(channelId).orElse(null);
        }

        if (channel == null) {
            LoggerUtil.logMissingChannel(logger, channelId);
            return;
        }

        Optional<Server> server = api.getServerById(serverId);

        Member member = null;
        if (packet.hasNonNull("member") && server.isPresent()) {
            member = new MemberImpl(api, (ServerImpl) server.get(), packet.get("member"), null);
        }
        Optional<Message> message = api.getCachedMessageById(messageId);

        Emoji emoji;
        JsonNode emojiJson = packet.get("emoji");
        if (!emojiJson.has("id") || emojiJson.get("id").isNull()) {
            emoji = UnicodeEmojiImpl.fromString(emojiJson.get("name").asText());
        } else {
            emoji = api.getKnownCustomEmojiOrCreateCustomEmoji(emojiJson);
        }

        boolean isSuperReaction = packet.get("burst").asBoolean();
        List<Color> burstColors = new ArrayList<>();

        if (packet.has("burst_colors")) {
            for (JsonNode color : packet.get("burst_colors")) {
                burstColors.add(Color.decode(color.asText()));
            }
        }

        message.ifPresent(msg -> ((MessageImpl) msg).addReaction(emoji, userId == api.getYourself().getId(),
                isSuperReaction, burstColors));

        ReactionAddEvent event = new ReactionAddEventImpl(api, messageId, channel, emoji, userId, member);

        api.getEventDispatcher().dispatchReactionAddEvent(
                server.map(DispatchQueueSelector.class::cast).orElse(api),
                messageId,
                server.orElse(null),
                channel,
                userId,
                event);
    }

}

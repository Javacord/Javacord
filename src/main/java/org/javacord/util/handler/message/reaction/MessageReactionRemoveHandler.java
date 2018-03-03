package org.javacord.util.handler.message.reaction;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.DiscordApi;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.emoji.Emoji;
import org.javacord.entity.emoji.impl.ImplUnicodeEmoji;
import org.javacord.entity.message.Message;
import org.javacord.entity.message.impl.ImplMessage;
import org.javacord.entity.user.User;
import org.javacord.event.message.reaction.ReactionRemoveEvent;
import org.javacord.listener.message.reaction.ReactionRemoveListener;
import org.javacord.util.gateway.PacketHandler;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.emoji.Emoji;
import org.javacord.entity.emoji.impl.ImplUnicodeEmoji;
import org.javacord.entity.message.Message;
import org.javacord.entity.message.impl.ImplMessage;
import org.javacord.entity.user.User;
import org.javacord.event.message.reaction.ReactionRemoveEvent;
import org.javacord.util.gateway.PacketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Handles the message reaction remove packet.
 */
public class MessageReactionRemoveHandler extends PacketHandler {

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
        api.getTextChannelById(packet.get("channel_id").asText()).ifPresent(channel -> {
            long messageId = packet.get("message_id").asLong();
            User user = api.getCachedUserById(packet.get("user_id").asText()).orElse(null);
            Optional<Message> message = api.getCachedMessageById(messageId);

            Emoji emoji;
            JsonNode emojiJson = packet.get("emoji");
            if (!emojiJson.has("id") || emojiJson.get("id").isNull()) {
                emoji = ImplUnicodeEmoji.fromString(emojiJson.get("name").asText());
            } else {
                emoji = api.getKnownCustomEmojiOrCreateCustomEmoji(emojiJson);
            }

            message.ifPresent(msg -> ((ImplMessage) msg).removeReaction(emoji, user.isYourself()));

            ReactionRemoveEvent event = new ReactionRemoveEvent(api, messageId, channel, emoji, user);

            List<ReactionRemoveListener> listeners = new ArrayList<>();
            listeners.addAll(Message.getReactionRemoveListeners(api, messageId));
            listeners.addAll(channel.getReactionRemoveListeners());
            if (channel instanceof ServerTextChannel) {
                listeners.addAll(((ServerTextChannel) channel).getServer().getReactionRemoveListeners());
            }
            listeners.addAll(user.getReactionRemoveListeners());
            listeners.addAll(api.getReactionRemoveListeners());

            if (channel instanceof ServerTextChannel) {
                api.getEventDispatcher().dispatchEvent(((ServerTextChannel) channel).getServer(),
                        listeners, listener -> listener.onReactionRemove(event));
            } else {
                api.getEventDispatcher().dispatchEvent(api, listeners, listener -> listener.onReactionRemove(event));
            }
        });
    }

}
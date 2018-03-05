package org.javacord.util.handler.message.reaction;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.DiscordApi;
import org.javacord.entity.channel.ServerTextChannel;
import org.javacord.entity.emoji.Emoji;
import org.javacord.entity.emoji.impl.ImplUnicodeEmoji;
import org.javacord.entity.message.Message;
import org.javacord.entity.message.impl.ImplMessage;
import org.javacord.entity.user.User;
import org.javacord.event.message.reaction.ReactionAddEvent;
import org.javacord.listener.message.reaction.ReactionAddListener;
import org.javacord.util.gateway.PacketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Handles the message reaction add packet.
 */
public class MessageReactionAddHandler extends PacketHandler {

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

            message.ifPresent(msg -> ((ImplMessage) msg).addReaction(emoji, user.isYourself()));

            ReactionAddEvent event = new ReactionAddEvent(api, messageId, channel, emoji, user);

            List<ReactionAddListener> listeners = new ArrayList<>();
            listeners.addAll(Message.getReactionAddListeners(api, messageId));
            listeners.addAll(channel.getReactionAddListeners());
            if (channel instanceof ServerTextChannel) {
                listeners.addAll(((ServerTextChannel) channel).getServer().getReactionAddListeners());
            }
            listeners.addAll(user.getReactionAddListeners());
            listeners.addAll(api.getReactionAddListeners());
            if (channel instanceof ServerTextChannel) {
                api.getEventDispatcher().dispatchEvent(((ServerTextChannel) channel).getServer(),
                        listeners, listener -> listener.onReactionAdd(event));
            } else {
                api.getEventDispatcher().dispatchEvent(api, listeners, listener -> listener.onReactionAdd(event));
            }
        });
    }

}
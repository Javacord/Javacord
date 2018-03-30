package org.javacord.core.util.handler.message.reaction;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.reaction.ReactionAddEvent;
import org.javacord.api.listener.message.reaction.ReactionAddListener;
import org.javacord.core.entity.emoji.UnicodeEmojiImpl;
import org.javacord.core.entity.message.MessageImpl;
import org.javacord.core.event.message.reaction.ReactionAddEventImpl;
import org.javacord.core.util.gateway.PacketHandler;

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
            User user = api.getCachedUserById(packet.get("user_id").asText()).orElseThrow(AssertionError::new);
            Optional<Message> message = api.getCachedMessageById(messageId);

            Emoji emoji;
            JsonNode emojiJson = packet.get("emoji");
            if (!emojiJson.has("id") || emojiJson.get("id").isNull()) {
                emoji = UnicodeEmojiImpl.fromString(emojiJson.get("name").asText());
            } else {
                emoji = api.getKnownCustomEmojiOrCreateCustomEmoji(emojiJson);
            }

            message.ifPresent(msg -> ((MessageImpl) msg).addReaction(emoji, user.isYourself()));

            ReactionAddEvent event = new ReactionAddEventImpl(api, messageId, channel, emoji, user);

            List<ReactionAddListener> listeners = new ArrayList<>();
            listeners.addAll(Message.getReactionAddListeners(api, messageId));
            listeners.addAll(channel.getReactionAddListeners());
            if (channel instanceof ServerChannel) {
                listeners.addAll(((ServerChannel) channel).getServer().getReactionAddListeners());
            }
            listeners.addAll(user.getReactionAddListeners());
            listeners.addAll(api.getReactionAddListeners());
            if (channel instanceof ServerChannel) {
                api.getEventDispatcher().dispatchEvent(((ServerChannel) channel).getServer(),
                        listeners, listener -> listener.onReactionAdd(event));
            } else {
                api.getEventDispatcher().dispatchEvent(api, listeners, listener -> listener.onReactionAdd(event));
            }
        });
    }

}
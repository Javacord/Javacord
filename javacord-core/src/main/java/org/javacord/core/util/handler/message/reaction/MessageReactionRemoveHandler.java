package org.javacord.core.util.handler.message.reaction;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.reaction.ReactionRemoveEvent;
import org.javacord.api.listener.message.MessageAttachableListenerManager;
import org.javacord.api.listener.message.reaction.ReactionRemoveListener;
import org.javacord.core.entity.emoji.UnicodeEmojiImpl;
import org.javacord.core.entity.message.MessageImpl;
import org.javacord.core.event.message.reaction.ReactionRemoveEventImpl;
import org.javacord.core.util.gateway.PacketHandler;

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
            User user = api.getCachedUserById(packet.get("user_id").asText()).orElseThrow(AssertionError::new);
            Optional<Message> message = api.getCachedMessageById(messageId);

            Emoji emoji;
            JsonNode emojiJson = packet.get("emoji");
            if (!emojiJson.has("id") || emojiJson.get("id").isNull()) {
                emoji = UnicodeEmojiImpl.fromString(emojiJson.get("name").asText());
            } else {
                emoji = api.getKnownCustomEmojiOrCreateCustomEmoji(emojiJson);
            }

            message.ifPresent(msg -> ((MessageImpl) msg).removeReaction(emoji, user.isYourself()));

            ReactionRemoveEvent event = new ReactionRemoveEventImpl(api, messageId, channel, emoji, user);

            List<ReactionRemoveListener> listeners = new ArrayList<>();
            listeners.addAll(MessageAttachableListenerManager.getReactionRemoveListeners(api, messageId));
            listeners.addAll(channel.getReactionRemoveListeners());
            if (channel instanceof ServerChannel) {
                listeners.addAll(((ServerChannel) channel).getServer().getReactionRemoveListeners());
            }
            listeners.addAll(user.getReactionRemoveListeners());
            listeners.addAll(api.getReactionRemoveListeners());

            if (channel instanceof ServerChannel) {
                api.getEventDispatcher().dispatchEvent(((ServerChannel) channel).getServer(),
                        listeners, listener -> listener.onReactionRemove(event));
            } else {
                api.getEventDispatcher().dispatchEvent(api, listeners, listener -> listener.onReactionRemove(event));
            }
        });
    }

}
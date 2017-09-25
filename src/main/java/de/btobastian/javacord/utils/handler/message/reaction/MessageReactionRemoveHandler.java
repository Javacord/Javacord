package de.btobastian.javacord.utils.handler.message.reaction;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.emoji.Emoji;
import de.btobastian.javacord.entities.message.emoji.impl.ImplUnicodeEmoji;
import de.btobastian.javacord.entities.message.impl.ImplMessage;
import de.btobastian.javacord.events.message.reaction.ReactionRemoveEvent;
import de.btobastian.javacord.listeners.message.reaction.ReactionRemoveListener;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONObject;

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
    public void handle(JSONObject packet) {
        api.getTextChannelById(packet.getString("channel_id")).ifPresent(channel -> {
            long messageId = Long.parseLong(packet.getString("message_id"));
            User user = api.getUserById(packet.getString("user_id")).orElse(null);
            Optional<Message> message = api.getCachedMessageById(messageId);

            Emoji emoji;
            JSONObject emojiJson = packet.getJSONObject("emoji");
            if (!emojiJson.has("id") || emojiJson.isNull("id")) {
                emoji = ImplUnicodeEmoji.fromString(emojiJson.getString("name"));
            } else {
                emoji = api.getOrCreateCustomEmoji(null, emojiJson);
            }

            message.ifPresent(msg -> ((ImplMessage) msg).removeReaction(emoji, user.isYourself()));

            ReactionRemoveEvent event = new ReactionRemoveEvent(api, messageId, channel, emoji, user);

            List<ReactionRemoveListener> listeners = new ArrayList<>();
            listeners.addAll(api.getReactionRemoveListeners(messageId));
            listeners.addAll(channel.getReactionRemoveListeners());
            if (channel instanceof ServerTextChannel) {
                listeners.addAll(((ServerTextChannel) channel).getServer().getReactionRemoveListeners());
            }
            listeners.addAll(user.getReactionRemoveListeners());
            listeners.addAll(api.getReactionRemoveListeners());

            dispatchEvent(listeners, listener -> listener.onReactionRemove(event));
        });
    }

}
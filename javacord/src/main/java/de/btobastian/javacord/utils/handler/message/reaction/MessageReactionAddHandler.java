package de.btobastian.javacord.utils.handler.message.reaction;

import de.btobastian.javacord.DiscordApi;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.channels.ServerTextChannel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.emoji.Emoji;
import de.btobastian.javacord.entities.message.emoji.impl.ImplUnicodeEmoji;
import de.btobastian.javacord.entities.message.impl.ImplMessage;
import de.btobastian.javacord.events.message.reaction.ReactionAddEvent;
import de.btobastian.javacord.listeners.message.reaction.ReactionAddListener;
import de.btobastian.javacord.utils.PacketHandler;
import org.json.JSONObject;

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

            message.ifPresent(msg -> ((ImplMessage) msg).addReaction(emoji, user.isYourself()));

            ReactionAddEvent event = new ReactionAddEvent(api, messageId, channel, emoji, user);

            List<ReactionAddListener> listeners = new ArrayList<>();
            listeners.addAll(api.getReactionAddListeners(messageId));
            listeners.addAll(channel.getReactionAddListeners());
            if (channel instanceof ServerTextChannel) {
                listeners.addAll(((ServerTextChannel) channel).getServer().getReactionAddListeners());
            }
            listeners.addAll(user.getReactionAddListeners());
            listeners.addAll(api.getReactionAddListeners());

            dispatchEvent(listeners, listener -> listener.onReactionAdd(event));
        });
    }

}
package de.btobastian.javacord.entities.message.impl;

import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.Reaction;
import de.btobastian.javacord.entities.message.emoji.Emoji;
import de.btobastian.javacord.entities.message.emoji.impl.ImplUnicodeEmoji;
import org.json.JSONObject;

/**
 * The implementation of {@link Reaction}.
 */
public class ImplReaction implements Reaction {

    /**
     * The message of the reaction.
     */
    private final Message message;

    /**
     * The emoji of the reaction.
     */
    private final Emoji emoji;

    /**
     * The amount of users who used this reaction.
     */
    private int count;

    /**
     * Whether this reaction is used by you or not.
     */
    private boolean containsYou;

    /**
     * Creates a new reaction.
     *
     * @param message The message, the reaction belongs to.
     * @param data The json data of the reaction.
     */
    public ImplReaction(Message message, JSONObject data) {
        this.message = message;
        this.count = data.getInt("count");
        this.containsYou = data.getBoolean("me");

        JSONObject emojiJson = data.getJSONObject("emoji");
        if (!emojiJson.has("id") || emojiJson.isNull("id")) {
            emoji = ImplUnicodeEmoji.fromString(emojiJson.getString("name"));
        } else {
            emoji = ((ImplDiscordApi) message.getApi()).getOrCreateCustomEmoji(null, emojiJson);
        }
    }

    /**
     * Creates a new reaction.
     *
     * @param message The message, the reaction belongs to.
     * @param emoji The emoji of the reaction.
     * @param count The amount of users who used this reaction.
     * @param you Whether this reaction is used by you or not.
     */
    public ImplReaction(Message message, Emoji emoji, int count, boolean you) {
        this.message = message;
        this.emoji = emoji;
        this.count = count;
        this.containsYou = you;
    }

    /**
     * Increments the count of the reaction.
     *
     * @param you If you added the reaction.
     */
    public void incrementCount(boolean you) {
        count++;
        if (you) {
            containsYou = true;
        }
    }

    /**
     * Decrements the count of the reaction.
     *
     * @param you If you removed the reaction.
     */
    public void decrementCount(boolean you) {
        count--;
        if (you) {
            containsYou = false;
        }
    }

    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public Emoji getEmoji() {
        return emoji;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public boolean containsYou() {
        return containsYou;
    }

    @Override
    public String toString() {
        return String.format(
                "Reaction (message id: %s, emoji: %s, count: %s)", getMessage().getId(), getEmoji(), getCount());
    }
}

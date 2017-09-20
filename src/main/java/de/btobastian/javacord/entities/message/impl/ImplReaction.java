package de.btobastian.javacord.entities.message.impl;

import de.btobastian.javacord.ImplDiscordApi;
import de.btobastian.javacord.entities.message.Reaction;
import de.btobastian.javacord.entities.message.emoji.Emoji;
import de.btobastian.javacord.entities.message.emoji.impl.ImplUnicodeEmoji;
import org.json.JSONObject;

/**
 * The implementation of {@link Reaction}.
 */
public class ImplReaction implements Reaction {

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
     * @param api The discord api instance.
     * @param data The json data of the reaction.
     */
    public ImplReaction(ImplDiscordApi api, JSONObject data) {
        this.count = data.getInt("count");
        this.containsYou = data.getBoolean("me");

        JSONObject emojiJson = data.getJSONObject("emoji");
        if (!emojiJson.has("id") || emojiJson.isNull("id")) {
            emoji = ImplUnicodeEmoji.fromString(emojiJson.getString("name"));
        } else {
            emoji = api.getOrCreateCustomEmoji(null, emojiJson);
        }
    }

    /**
     * Creates a new reaction.
     *
     * @param api The discord api instance.
     * @param emoji The emoji of the reaction.
     * @param count The amount of users who used this reaction.
     * @param you Whether this reaction is used by you or not.
     */
    public ImplReaction(ImplDiscordApi api, Emoji emoji, int count, boolean you) {
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
}

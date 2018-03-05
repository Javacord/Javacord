package org.javacord.entity.message.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.ImplDiscordApi;
import org.javacord.entity.DiscordEntity;
import org.javacord.entity.emoji.Emoji;
import org.javacord.entity.emoji.impl.ImplUnicodeEmoji;
import org.javacord.entity.message.Message;
import org.javacord.entity.message.Reaction;

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
    public ImplReaction(Message message, JsonNode data) {
        this.message = message;
        this.count = data.get("count").asInt();
        this.containsYou = data.get("me").asBoolean();

        JsonNode emojiJson = data.get("emoji");
        if (!emojiJson.has("id") || emojiJson.get("id").isNull()) {
            emoji = ImplUnicodeEmoji.fromString(emojiJson.get("name").asText());
        } else {
            emoji = ((ImplDiscordApi) message.getApi()).getKnownCustomEmojiOrCreateCustomEmoji(emojiJson);
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
        // Make sure to always return the known custom emoji, if it is known
        return emoji.asCustomEmoji()
                .map(DiscordEntity::getId)
                .flatMap(id -> message.getApi().getCustomEmojiById(id))
                .map(Emoji.class::cast)
                .orElse(emoji);
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
        return String.format("Reaction (message id: %s, emoji: %s, count: %s)",
                             getMessage().getIdAsString(), getEmoji(), getCount());
    }
}

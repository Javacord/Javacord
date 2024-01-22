package org.javacord.core.entity.message;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.message.CountDetails;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.Reaction;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.emoji.UnicodeEmojiImpl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The implementation of {@link Reaction}.
 */
public class ReactionImpl implements Reaction {

    /**
     * The message of the reaction.
     */
    private final Message message;

    /**
     * The emoji of the reaction.
     */
    private final Emoji emoji;

    /**
     * The number of users who used this reaction.
     */
    private final AtomicInteger count = new AtomicInteger();

    /**
     * Whether this reaction is used by you or not.
     */
    private volatile boolean containsYou;

    /**
     * Whether this super reaction is used by you or not.
     */
    private volatile boolean containsYouSuper;

    /**
     * The reaction count details.
     */
    private volatile CountDetails countDetails;

    /**
     * The HEX colors used for super reactions.
     */
    private final List<Color> burstColors = new ArrayList<>();

    /**
     * Creates a new reaction.
     *
     * @param message The message, the reaction belongs to.
     * @param data The json data of the reaction.
     */
    public ReactionImpl(Message message, JsonNode data) {
        this.message = message;
        this.count.set(data.get("count").asInt());
        this.containsYou = data.get("me").asBoolean();
        this.containsYouSuper = data.get("me_burst").asBoolean();
        this.countDetails = new CountDetailsImpl(data.get("count_details"));

        for (JsonNode color : data.get("burst_colors")) {
            burstColors.add(Color.decode(color.asText()));
        }

        JsonNode emojiJson = data.get("emoji");
        if (!emojiJson.has("id") || emojiJson.get("id").isNull()) {
            emoji = UnicodeEmojiImpl.fromString(emojiJson.get("name").asText());
        } else {
            emoji = ((DiscordApiImpl) message.getApi()).getKnownCustomEmojiOrCreateCustomEmoji(emojiJson);
        }
    }

    /**
     * Creates a new reaction.
     *
     * @param message The message, the reaction belongs to.
     * @param emoji The emoji of the reaction.
     * @param count The number of users who used this reaction.
     * @param you Whether this reaction is used by you or not.
     * @param burstColors The HEX colors used for super reactions.
     * @param countDetails The details about the count of reactions.
     */
    public ReactionImpl(Message message, Emoji emoji, int count, boolean you,
                        CountDetails countDetails, List<Color> burstColors) {
        this.message = message;
        this.emoji = emoji;
        this.count.set(count);
        this.containsYou = you;
        this.countDetails = countDetails;

        if (burstColors != null && !burstColors.isEmpty()) {
            this.burstColors.addAll(burstColors);
        }
    }

    /**
     * Increments the count of the reaction.
     *
     * @param you If you added the reaction.
     */
    public void incrementCount(boolean you) {
        count.getAndIncrement();
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
        count.decrementAndGet();
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
        return count.get();
    }

    @Override
    public boolean containsYou() {
        return containsYou;
    }

    @Override
    public boolean containsYouSuper() {
        return containsYouSuper;
    }

    @Override
    public CountDetails getCountDetails() {
        return countDetails;
    }

    @Override
    public List<Color> getBurstColors() {
        return Collections.unmodifiableList(burstColors);
    }

    /**
     * Sets the new count details.
     * @param countDetails The new count details.
     */
    public void setCountDetails(CountDetails countDetails) {
        this.countDetails = countDetails;
    }

    @Override
    public String toString() {
        return String.format("Reaction (message id: %s, emoji: %s, count: %s)",
                             getMessage().getIdAsString(), getEmoji(), getCount());
    }
}

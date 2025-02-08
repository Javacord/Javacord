package org.javacord.core.entity.channel.forum;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.channel.forum.DefaultReaction;

import java.util.Optional;

public class DefaultReactionImpl implements DefaultReaction {

    /**
     * The id of a guild's custom emoji.
     */
    private final Long emojiId;

    /**
     * The name of a guild's custom emoji.
     */
    private String emojiName;

    /**
     * Creates a new default reaction.
     *
     * @param data The json data of the default reaction.
     */
    public DefaultReactionImpl(JsonNode data) {
        emojiId = data.has("emoji_id") ? data.get("emoji_id").asLong() : null;
        emojiName = data.has("emoji_name") ? data.get("emoji_name").asText() : null;
    }

    /**
     * Used to set the name of a guild's custom emoji.
     *
     * @param emojiName The name of a guild's custom emoji.
     * @return The current instance in order to chain call methods.
     */
    public DefaultReactionImpl setEmojiName(String emojiName) {
        this.emojiName = emojiName;
        return this;
    }

    @Override
    public Optional<Long> getEmojiId() {
        return Optional.ofNullable(emojiId);
    }

    @Override
    public Optional<String> getEmojiName() {
        return Optional.ofNullable(emojiName);
    }
}

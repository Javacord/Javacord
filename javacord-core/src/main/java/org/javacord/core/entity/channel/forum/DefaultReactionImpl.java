package org.javacord.core.entity.channel.forum;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.channel.forum.DefaultReaction;

public class DefaultReactionImpl implements DefaultReaction {

    /**
     * The id of a guild's custom emoji.
     */
    private final long emojiId;

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
        emojiId = data.get("emoji_id").asLong();
        emojiName = data.get("emoji_name").asText();
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
    public long getEmojiId() {
        return emojiId;
    }

    @Override
    public String getEmojiName() {
        return emojiName;
    }
}

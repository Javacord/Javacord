package org.javacord.core.entity.server.invite;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.server.invite.WelcomeScreenChannel;

import java.util.Optional;

/**
 * The implementation of {@link WelcomeScreenChannel}.
 */
public class WelcomeScreenChannelImpl implements WelcomeScreenChannel {

    /**
     * The channel's id.
     */
    private final long channelId;
    
    /**
     * The description shown for the channel.
     */
    private final String description;

    /**
     * The emoji id, if the emoji is custom.
     */
    private final Long emojiId;

    /**
     * The emoji name if custom, the unicode character if standard, or null if no emoji is set.
     */
    private final String emojiName;

    /**
     * Creates an instance of Welcome Screen Channel Object.
     * 
     * @param data JSON data for welcome screen channel.
     */
    public WelcomeScreenChannelImpl(JsonNode data) {
        this.channelId = data.get("channel_id").asLong();
        this.description = data.get("description").asText();
        this.emojiId = data.hasNonNull("emoji_id") ? data.get("emoji_id").asLong() : null;
        this.emojiName = data.hasNonNull("emoji_name") ? data.get("emoji_name").asText() : null;
    }

    @Override
    public long getChannelId() {
        return channelId;
    }
    
    @Override
    public String getDescription() {
        return description;
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

package org.javacord.core.entity.channel.forum;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.forum.AvailableTag;

import java.util.Optional;

public class AvailableTagImpl implements AvailableTag {
    /**
     * The api instance.
     */
    private final DiscordApi api;

    /**
     * The id of the tag.
     */
    private final long id;

    /**
     * The name of the tag.
     */
    private String name;

    /**
     * Whether this tag can only be added to or removed from a thread by a moderator.
     */
    private boolean moderated;

    /**
     * The id of a guild's custom emoji.
     */
    private final long emojiId;

    /**
     * The name of a guild's custom emoji.
     */
    private String emojiName;

    /**
     * Creates a new forum tag.
     *
     * @param api The discord api instance.
     * @param data The json data of the tag.
     */
    public AvailableTagImpl(DiscordApi api, JsonNode data) {
        this.api = api;
        this.id = data.get("id").asLong();

        name = data.get("name").asText();
        moderated = data.get("moderated").asBoolean();
        emojiId = data.get("emoji_id").asLong();
        emojiName = data.has("emoji_name") ? data.get("emoji_name").asText() : null;
    }

    /**
     * Used to set the name of the tag.
     *
     * @param name The name of the tag.
     * @return The current instance in order to chain call methods.
     */
    public AvailableTagImpl setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Used to set whether this tag can only be added to or removed from a
     * thread by a moderator.
     *
     * @param moderated Whether this tag is locked to MANAGE_THREADS members
     * @return The current instance in order to chain call methods.
     */
    public AvailableTagImpl setModerated(boolean moderated) {
        this.moderated = moderated;
        return this;
    }

    /**
     * Used to set the name of a guild's custom emoji.
     *
     * @param emojiName The name of a guild's custom emoji.
     * @return The current instance in order to chain call methods.
     */
    public AvailableTagImpl setEmojiName(String emojiName) {
        this.emojiName = emojiName;
        return this;
    }

    @Override
    public DiscordApi getApi() {
        return api;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isModerated() {
        return moderated;
    }

    @Override
    public long getEmojiId() {
        return emojiId;
    }

    @Override
    public Optional<String> getEmojiName() {
        return Optional.ofNullable(emojiName);
    }
}

package org.javacord.core.entity.message.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.message.component.SelectMenuOption;
import org.javacord.core.entity.emoji.CustomEmojiImpl;
import org.javacord.core.entity.emoji.UnicodeEmojiImpl;

import java.util.Optional;

public class SelectMenuOptionImpl implements SelectMenuOption {

    private final String label;
    private final String value;
    private final String description;
    private final Emoji emoji;
    private final boolean isDefault;

    /**
     * Creates a new select menu option.
     *
     * @param data The json data of the select menu option.
     */
    public SelectMenuOptionImpl(JsonNode data) {
        if (data.has("emoji")) {
            JsonNode emojiObj = data.get("emoji");
            if (emojiObj.has("id")) {
                long id = emojiObj.get("id").asLong();
                String name = emojiObj.get("name").asText();
                boolean isAnimated = emojiObj.has("animated");

                this.emoji = new CustomEmojiImpl(null, id, name, isAnimated);
            } else {
                String name = emojiObj.get("name").asText();
                this.emoji = UnicodeEmojiImpl.fromString(name);
            }
        } else {
            this.emoji = null;
        }
        description = data.has("description") ? data.get("description").asText() : null;
        isDefault = data.has("default") && data.get("default").asBoolean();
        value = data.get("value").asText();
        label = data.get("label").asText();
    }

    /**
     * Create a new select menu option.
     *
     * @param label The label for the option.
     * @param value The value for the option.
     * @param isDefault If the option is the default option.
     * @param description The description for the option.
     * @param emoji The emoji for the option.
     */
    public SelectMenuOptionImpl(String label, String value, boolean isDefault, String description, Emoji emoji) {
        this.label = label;
        this.value = value;
        this.isDefault = isDefault;
        this.description = description;
        this.emoji = emoji;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    @Override
    public Optional<Emoji> getEmoji() {
        return Optional.ofNullable(emoji);
    }

    @Override
    public boolean isDefault() {
        return isDefault;
    }

    /**
     * Gets the select menu option as a {@link ObjectNode}. This is what is sent to Discord.
     *
     * @return The select menu option as a ObjectNode.
     */
    public ObjectNode toJson() {
        ObjectNode object = JsonNodeFactory.instance.objectNode();

        if (emoji != null) {
            ObjectNode emojiObj = JsonNodeFactory.instance.objectNode();
            if (emoji instanceof CustomEmojiImpl) {
                emojiObj.put("id", ((CustomEmojiImpl) emoji).getId());
                emojiObj.put("name", ((CustomEmojiImpl) emoji).getName());
            } else {
                emojiObj.put("name", emoji.asUnicodeEmoji().get());
            }

            object.set("emoji", emojiObj);
        }

        object.put("label", label);
        object.put("value", value);
        object.put("default", isDefault);

        if (description != null) {
            object.put("description", description);
        }

        return object;
    }
}

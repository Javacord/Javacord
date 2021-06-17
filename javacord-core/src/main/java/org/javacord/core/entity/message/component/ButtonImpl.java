package org.javacord.core.entity.message.component;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.message.component.ComponentType;
import org.javacord.core.entity.emoji.CustomEmojiImpl;
import org.javacord.core.entity.emoji.UnicodeEmojiImpl;

import java.util.Optional;

public class ButtonImpl extends ComponentImpl implements Button {
    private final ButtonStyle style;

    private final String label;

    private final String customId;

    private final String url;

    private final Boolean disabled;

    private final Emoji emoji;

    /**
     * Creates a new button.
     *
     * @param data The json data of the button.
     */
    public ButtonImpl(JsonNode data) {
        super(ComponentType.BUTTON);
        this.style = ButtonStyle.fromId(data.get("style").asInt());
        this.label = data.has("label") ? data.get("label").asText() : null;
        this.customId = data.has("custom_id") ? data.get("custom_id").asText() : null;
        this.url = data.has("url") ? data.get("url").asText() : null;
        this.disabled = data.has("disabled") ? data.get("disabled").asBoolean() : null;

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
    }

    @Override
    public ButtonStyle getStyle() {
        return style;
    }

    @Override
    public Optional<String> getCustomId() {
        return Optional.ofNullable(customId);
    }

    @Override
    public Optional<String> getLabel() {
        return Optional.ofNullable(label);
    }

    @Override
    public Optional<String> getUrl() {
        return Optional.ofNullable(url);
    }

    @Override
    public Optional<Boolean> isDisabled() {
        return Optional.ofNullable(disabled);
    }

    @Override
    public Optional<Emoji> getEmoji() {
        return Optional.ofNullable(emoji);
    }
}

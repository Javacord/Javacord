package org.javacord.core.entity.message.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.emoji.CustomEmoji;
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

    /**
     * Creates a new button.
     *
     * @param style The button's style.
     * @param label The button's label.
     * @param customId The button's custom ID.
     * @param url The button's url.
     * @param disabled Whether the button is disabled.
     * @param emoji The button's emoji.
     */
    public ButtonImpl(ButtonStyle style, String label, String customId, String url,
                      Boolean disabled, Emoji emoji) {
        super(ComponentType.BUTTON);
        this.style = style;
        this.label = label;
        this.customId = customId;
        this.url = url;
        this.disabled = disabled;
        this.emoji = emoji;
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

    /**
     * Gets the button as a {@link ObjectNode}. This is what is sent to Discord.
     *
     * @return The button as a ObjectNode.
     */
    public ObjectNode toJsonNode() {
        ObjectNode object = JsonNodeFactory.instance.objectNode();
        return toJsonNode(object);
    }

    /**
     * Gets the button as a {@link ObjectNode}. This is what is sent to Discord.
     *
     * @param object The object, the data should be added to.
     * @return The button as a ObjectNode.
     */
    public ObjectNode toJsonNode(ObjectNode object) {
        object.put("type", ComponentType.BUTTON.value());

        // 1. Style is not optional; Buttons without a style are not accepted
        if (style == null) {
            throw new IllegalStateException("Button style is null.");
        }
        object.put("style", style.getValue());

        if (label != null && !label.equals("")) {
            object.put("label", label);
        }

        // 2. Non-link buttons must have a custom_id, and cannot have a url
        if (style != ButtonStyle.LINK) {
            if (customId == null || customId.equals("")) {
                throw new IllegalStateException("Button is missing a custom identifier.");
            } else if (url != null) {
                throw new IllegalStateException("A non-button link must not have a URL.");
            }
            object.put("custom_id", customId);
        }

        // 3. Link buttons must have a url, and cannot have a custom_id
        if (style == ButtonStyle.LINK) {
            if (url == null || url.equals("")) {
                throw new IllegalStateException("Button link is missing a URL.");
            }
            if (customId != null) {
                throw new IllegalStateException("Button link must not have a custom identifier");
            }
            object.put("url", url);
        }

        if (disabled != null) {
            object.put("disabled", disabled);
        }

        if (emoji != null) {
            ObjectNode emojiObj = JsonNodeFactory.instance.objectNode();
            if (emoji.isUnicodeEmoji()) {
                Optional<String> unicodeEmojiOptional = emoji.asUnicodeEmoji();
                unicodeEmojiOptional.ifPresent(emojiName -> emojiObj.put("name", emojiName));
            } else if (emoji.isCustomEmoji()) {
                Optional<CustomEmoji> customEmojiOptional = emoji.asCustomEmoji();
                customEmojiOptional.ifPresent(customEmoji -> emojiObj.put("id", customEmoji.getId()));
            }
            object.set("emoji", emojiObj);
        }

        return object;
    }
}

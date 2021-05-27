package org.javacord.core.entity.message.component.internal;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.emoji.CustomEmoji;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.message.component.ComponentType;
import org.javacord.api.entity.message.component.internal.ButtonBuilderDelegate;
import org.javacord.core.entity.emoji.UnicodeEmojiImpl;

import java.util.Optional;

public class ButtonBuilderDelegateImpl implements ButtonBuilderDelegate {
    private final ComponentType type = ComponentType.BUTTON;

    private ButtonStyle style = ButtonStyle.SECONDARY;

    private String label = null;

    private String customId = null;

    private String url = null;

    private Boolean disabled = null;

    private Emoji emoji = null;

    @Override
    public ComponentType getType() {
        return type;
    }

    @Override
    public void copy(Button button) {
        Optional<String> customId = button.getCustomId();
        Optional<String> url = button.getUrl();
        Optional<String> label = button.getLabel();
        Optional<Emoji> emoji = button.getEmoji();
        Optional<Boolean> isDisabled = button.isDisabled();
        ButtonStyle style = button.getStyle();

        this.setStyle(style);
        customId.ifPresent(this::setCustomId);
        url.ifPresent(this::setUrl);
        label.ifPresent(this::setLabel);
        emoji.ifPresent(this::setEmoji);
        isDisabled.ifPresent(this::setDisabled);
    }

    @Override
    public void setStyle(ButtonStyle style) {
        this.style = style;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public void setCustomId(String customId) {
        this.customId = customId;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void setDisabled(Boolean isDisabled) {
        this.disabled = isDisabled;
    }

    @Override
    public void setEmoji(Emoji emoji) {
        this.emoji = emoji;
    }

    @Override
    public void setEmoji(CustomEmoji emoji) {
        this.emoji = emoji;
    }

    @Override
    public void setEmoji(String unicode) {
        this.emoji = UnicodeEmojiImpl.fromString(unicode);
    }

    @Override
    public ButtonStyle getStyle() {
        return style;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getCustomId() {
        return customId;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public Boolean isDisabled() {
        return disabled;
    }

    @Override
    public Emoji getEmoji() {
        return emoji;
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
        object.put("type", type.value());

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

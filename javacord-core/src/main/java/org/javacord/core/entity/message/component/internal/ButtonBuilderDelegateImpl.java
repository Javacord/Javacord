package org.javacord.core.entity.message.component.internal;

import org.javacord.api.entity.emoji.CustomEmoji;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.message.component.ComponentType;
import org.javacord.api.entity.message.component.internal.ButtonBuilderDelegate;
import org.javacord.core.entity.emoji.UnicodeEmojiImpl;
import org.javacord.core.entity.message.component.ButtonImpl;

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
    public Button build() {
        return new ButtonImpl(style, label, customId, url, disabled, emoji);
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

}

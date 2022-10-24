package org.javacord.core.entity.message.component.internal;

import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.message.component.SelectMenuOption;
import org.javacord.api.entity.message.component.internal.SelectMenuOptionBuilderDelegate;
import org.javacord.core.entity.emoji.UnicodeEmojiImpl;
import org.javacord.core.entity.message.component.SelectMenuOptionImpl;

public class SelectMenuOptionBuilderDelegateImpl implements SelectMenuOptionBuilderDelegate {
    private String label = null;
    private String value = null;
    private String description = null;
    private boolean isDefault = false;
    private Emoji emoji = null;

    @Override
    public void copy(SelectMenuOption selectMenuOption) {
        label = selectMenuOption.getLabel();
        value = selectMenuOption.getValue();
        selectMenuOption.getDescription().ifPresent(this::setDescription);
        isDefault = selectMenuOption.isDefault();
        selectMenuOption.getEmoji().ifPresent(this::setEmoji);
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public void setEmoji(String unicode) {
        this.emoji = UnicodeEmojiImpl.fromString(unicode);
    }

    @Override
    public void setEmoji(Emoji emoji) {
        this.emoji = emoji;
    }

    @Override
    public SelectMenuOption build() {
        return new SelectMenuOptionImpl(label, value, isDefault, description, emoji);
    }
}

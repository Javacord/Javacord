package org.javacord.core.entity.message.component;

import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.message.component.ComponentType;
import org.javacord.api.entity.message.component.EditableButton;

import java.util.Optional;

/**
 * The implementation of {@link EditableButton}.
 */
public class EditableButtonImpl implements EditableButton {

    private ButtonImpl delegate;

    /**
     * Creates a new editable button.
     *
     * @param button The button to be edited.
     */
    public EditableButtonImpl(ButtonImpl button) {
        delegate = button;
    }

    /**
     * Clears the delegate of this editable button and thus makes this instance unusable.
     */
    public void clearDelegate() {
        delegate = null;
    }

    @Override
    public ComponentType getType() {
        return delegate.getType();
    }

    @Override
    public ButtonStyle getStyle() {
        return delegate.getStyle();
    }

    @Override
    public Optional<String> getCustomId() {
        return delegate.getCustomId();
    }

    @Override
    public Optional<String> getLabel() {
        return delegate.getLabel();
    }

    @Override
    public Optional<String> getUrl() {
        return delegate.getUrl();
    }

    @Override
    public Optional<Boolean> isDisabled() {
        return delegate.isDisabled();
    }

    @Override
    public Optional<Emoji> getEmoji() {
        return delegate.getEmoji();
    }

    @Override
    public void setStyle(ButtonStyle style) {
        delegate.setStyle(style);
    }

    @Override
    public void setCustomId(String customId) {
        delegate.setCustomId(customId);
    }

    @Override
    public void setLabel(String label) {
        delegate.setLabel(label);
    }

    @Override
    public void setUrl(String url) {
        delegate.setUrl(url);
    }

    @Override
    public void setDisabled(boolean disabled) {
        delegate.setDisabled(disabled);
    }

    @Override
    public void setEmoji(Emoji emoji) {
        delegate.setEmoji(emoji);
    }
}

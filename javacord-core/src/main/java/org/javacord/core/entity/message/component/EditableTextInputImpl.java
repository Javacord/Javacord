package org.javacord.core.entity.message.component;

import org.javacord.api.entity.message.component.ComponentType;
import org.javacord.api.entity.message.component.EditableSelectMenu;
import org.javacord.api.entity.message.component.EditableTextInput;
import org.javacord.api.entity.message.component.TextInputStyle;

import java.util.Optional;

/**
 * The implementation of {@link EditableSelectMenu}.
 */
public class EditableTextInputImpl implements EditableTextInput {

    private TextInputImpl delegate;

    /**
     * Creates a new editable text input.
     *
     * @param textInput The text input to be edited.
     */
    public EditableTextInputImpl(TextInputImpl textInput) {
        delegate = textInput;
    }

    /**
     * Clears the delegate of this editable embed field and thus makes this instance unusable.
     */
    public void clearDelegate() {
        delegate = null;
    }

    @Override
    public ComponentType getType() {
        return delegate.getType();
    }

    @Override
    public void setStyle(TextInputStyle style) {
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
    public void setMinimumLength(int minimumLength) {
        delegate.setMinimumLength(minimumLength);
    }

    @Override
    public void setMaximumLength(int maximumLength) {
        delegate.setMaximumLength(maximumLength);
    }

    @Override
    public void setRequired(boolean required) {
        delegate.setRequired(required);
    }

    @Override
    public void setValue(String value) {
        delegate.setValue(value);
    }

    @Override
    public void setPlaceholder(String placeholder) {
        delegate.setPlaceholder(placeholder);
    }

    @Override
    public Optional<TextInputStyle> getStyle() {
        return delegate.getStyle();
    }

    @Override
    public String getCustomId() {
        return delegate.getCustomId();
    }

    @Override
    public Optional<String> getLabel() {
        return delegate.getLabel();
    }

    @Override
    public Optional<Integer> getMinimumLength() {
        return delegate.getMinimumLength();
    }

    @Override
    public Optional<Integer> getMaximumLength() {
        return delegate.getMaximumLength();
    }

    @Override
    public boolean isRequired() {
        return delegate.isRequired();
    }

    @Override
    public String getValue() {
        return delegate.getValue();
    }

    @Override
    public Optional<String> getPlaceholder() {
        return delegate.getPlaceholder();
    }
}

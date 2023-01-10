package org.javacord.core.entity.message.component.internal;

import org.javacord.api.entity.message.component.ComponentType;
import org.javacord.api.entity.message.component.TextInput;
import org.javacord.api.entity.message.component.TextInputStyle;
import org.javacord.api.entity.message.component.internal.TextInputBuilderDelegate;
import org.javacord.core.entity.message.component.TextInputImpl;
import java.util.Optional;

public class TextInputBuilderDelegateImpl implements TextInputBuilderDelegate {
    private final ComponentType type = ComponentType.TEXT_INPUT;

    private TextInputStyle style = null;

    private String customId = null;

    private String label = null;

    private String value = "";

    private String placeholder = null;

    private Integer minimumLength = null;

    private Integer maximumLength = null;

    private boolean required = false;

    @Override
    public ComponentType getType() {
        return type;
    }

    @Override
    public void copy(TextInput textInput) {
        style = textInput.getStyle().orElse(null);
        customId = textInput.getCustomId();
        label = textInput.getLabel().orElse(null);
        required = textInput.isRequired();
        minimumLength = textInput.getMinimumLength().orElse(null);
        maximumLength = textInput.getMaximumLength().orElse(null);
    }

    @Override
    public void setStyle(TextInputStyle style) {
        this.style = style;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public void setValue(String value) {
        this.value = value == null ? "" : value;
    }

    @Override
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    public void setMinimumLength(Integer minimumLength) {
        this.minimumLength = minimumLength;
    }

    @Override
    public void setMaximumLength(Integer maximumLength) {
        this.maximumLength = maximumLength;
    }

    @Override
    public void setCustomId(String customId) {
        this.customId = customId;
    }

    @Override
    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public TextInputStyle getStyle() {
        return style;
    }


    @Override
    public String getCustomId() {
        return customId;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public Optional<String> getValue() {
        return Optional.ofNullable(value);
    }

    @Override
    public Optional<String> getPlaceholder() {
        return Optional.ofNullable(placeholder);
    }

    @Override
    public Optional<Integer> getMinimumLength() {
        return Optional.ofNullable(minimumLength);
    }

    @Override
    public Optional<Integer> getMaximumLength() {
        return Optional.ofNullable(maximumLength);
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public TextInput build() {
        return new TextInputImpl(style, label, customId, value, placeholder, required, minimumLength, maximumLength);
    }
}

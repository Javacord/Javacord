package org.javacord.api.entity.message.component;

import org.javacord.api.entity.message.component.internal.TextInputBuilderDelegate;
import org.javacord.api.util.internal.DelegateFactory;

public class TextInputBuilder implements LowLevelComponentBuilder {
    private final TextInputBuilderDelegate delegate = DelegateFactory.createTextInputBuilderDelegate();

    /**
     * Creates a new text input builder.
     *
     * @param style    The style of the text input.
     * @param customId The custom id of the text input.
     * @param label    The label of the text input.
     */
    public TextInputBuilder(TextInputStyle style, String customId, String label) {
        setStyle(style);
        setCustomId(customId);
        setLabel(label);
    }

    /**
     * Creates a new text input builder.
     *
     * @param style    The style of the text input.
     * @param customId The custom id of the text input.
     * @param label    The label of the text input.
     */
    public TextInputBuilder(int style, String customId, String label) {
        setStyle(style);
        setCustomId(customId);
        setLabel(label);
    }

    /**
     * Copy a Text Input's value into this builder.
     *
     * @param textInput The Text Input to copy.
     * @return The builder instance to chain methods.
     */
    public TextInputBuilder copy(TextInput textInput) {
        delegate.copy(textInput);
        return this;
    }

    /**
     * Get the component's type (always {@link ComponentType#TEXT_INPUT}).
     *
     * @return The component's type.
     */
    public ComponentType getType() {
        return delegate.getType();
    }

    /**
     * Set the Text Input's style.
     *
     * @param style A new Text Input style.
     * @return The builder instance to chain methods.
     */
    public TextInputBuilder setStyle(TextInputStyle style) {
        delegate.setStyle(style);
        return this;
    }

    /**
     * Set the Text Input's style.
     *
     * @param styleValue The style of the Text Input based on Discord's style values.
     * @return The current instance in order to chain call methods.
     */
    public TextInputBuilder setStyle(int styleValue) {
        TextInputStyle parsed = TextInputStyle.fromId(styleValue);
        delegate.setStyle(parsed);
        return this;
    }

    /**
     * Set the Text Input's label.
     *
     * @param label The Text Input's label.
     * @return The current instance in order to chain call methods.
     */
    public TextInputBuilder setLabel(String label) {
        delegate.setLabel(label);
        return this;
    }

    /**
     * Set the Text Input's placeholder.
     *
     * @param placeholder The Text Input's placeholder.
     * @return The current instance in order to chain call methods.
     */
    public TextInputBuilder setPlaceholder(String placeholder) {
        delegate.setPlaceholder(placeholder);
        return this;
    }

    /**
     * Set the Text Input's value.
     *
     * @param value The Text Input's value.
     * @return The current instance in order to chain call methods.
     */
    public TextInputBuilder setValue(String value) {
        delegate.setValue(value);
        return this;
    }

    /**
     * Set the Text Input's minimum length.
     *
     * @param minimumLength The Text Input's minimum length.
     * @return The current instance in order to chain call methods.
     */
    public TextInputBuilder setMinimumLength(Integer minimumLength) {
        delegate.setMinimumLength(minimumLength);
        return this;
    }

    /**
     * Set the Text Input's maximum length.
     *
     * @param maximumLength The Text Input's maximum length.
     * @return The current instance in order to chain call methods.
     */
    public TextInputBuilder setMaximumLength(Integer maximumLength) {
        delegate.setMaximumLength(maximumLength);
        return this;
    }

    /**
     * Set the Text Input's custom ID.
     *
     * @param customId The Text Input's identifier.
     * @return The current instance in order to chain call methods.
     */
    public TextInputBuilder setCustomId(String customId) {
        delegate.setCustomId(customId);
        return this;
    }

    /**
     * Set whether this Text Input is required.
     *
     * @param required Whether the Text Input is required or not.
     * @return The current instance in order to chain call methods.
     */
    public TextInputBuilder setRequired(boolean required) {
        delegate.setRequired(required);
        return this;
    }

    /**
     * Creates a {@link TextInput} instance with the given values.
     *
     * @return The created TextInput instance.
     */
    public TextInput build() {
        return delegate.build();
    }

    /**
     * Gets the delegate used by the component builder internally.
     *
     * @return The delegate used by this component builder internally.
     */
    @Override
    public TextInputBuilderDelegate getDelegate() {
        return delegate;
    }
}

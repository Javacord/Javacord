package org.javacord.api.entity.message.component;

/**
 * This interface represents a text input that can be edited.
 */
public interface EditableTextInput extends TextInput {

    /**
     * Sets the style of the text input.
     *
     * @param style The style of the text input.
     */
    void setStyle(TextInputStyle style);

    /**
     * Sets the custom id of the text input.
     *
     * @param customId The custom id of the text input.
     */
    void setCustomId(String customId);

    /**
     * Sets the label of the text input.
     *
     * @param label The label of the text input.
     */
    void setLabel(String label);

    /**
     * Sets the minimum length of the text input.
     *
     * @param minimumLength The minimum length of the text input.
     */
    void setMinimumLength(int minimumLength);

    /**
     * Sets the maximum length of the text input.
     *
     * @param maximumLength The maximum length of the text input.
     */
    void setMaximumLength(int maximumLength);

    /**
     * Sets whether the text input is required.
     *
     * @param required Whether the text input is required.
     */
    void setRequired(boolean required);

    /**
     * Sets the value of the text input.
     *
     * @param value The value of the text input.
     */
    void setValue(String value);

    /**
     * Sets the placeholder of the text input.
     *
     * @param placeholder The placeholder of the text input.
     */
    void setPlaceholder(String placeholder);
}

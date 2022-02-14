package org.javacord.api.entity.message.component.internal;

import org.javacord.api.entity.message.component.ComponentType;
import org.javacord.api.entity.message.component.TextInput;
import org.javacord.api.entity.message.component.TextInputStyle;
import java.util.Optional;

public interface TextInputBuilderDelegate extends ComponentBuilderDelegate {
    /**
     * Get the TextInput's type.
     *
     * @return Always {@link ComponentType#TEXT_INPUT}
     */
    ComponentType getType();

    /**
     * Copy a TextInput's values into the builder.
     *
     * @param textInput The TextInput to copy.
     */
    void copy(TextInput textInput);

    /**
     * Get the TextInput's style.
     *
     * @return The TextInput's style.
     */
    TextInputStyle getStyle();

    /**
     * Get the TextInput's label.
     *
     * @return The TextInput's label.
     */
    String getLabel();

    /**
     * Get the TextInput's component identifier.
     *
     * @return The TextInput's component identifier.
     */
    String getCustomId();

    /**
     * Get the TextInput's value.
     *
     * @return The TextInput's value.
     */
    Optional<String> getValue();

    /**
     * Get the TextInput's placeholder.
     *
     * @return The TextInput's placeholder.
     */
    Optional<String> getPlaceholder();

    /**
     * Get the TextInput's minimum length.
     *
     * @return The TextInput's minimum length.
     */
    Optional<Integer> getMinimumLength();

    /**
     * Get the TextInput's maximum length.
     *
     * @return The TextInput's maximum length.
     */
    Optional<Integer> getMaximumLength();

    /**
     * Get whether the TextInput is required.
     *
     * @return Whether the TextInput is required.
     */
    boolean isRequired();

    /**
     * Set the TextInput's style.
     *
     * @param style The style of the TextInput.
     */
    void setStyle(TextInputStyle style);

    /**
     * Set the TextInput's label.
     *
     * @param label The TextInput's label.
     */
    void setLabel(String label);

    /**
     * Set the TextInput's value.
     *
     * @param value The TextInput's label.
     */
    void setValue(String value);

    /**
     * Set the TextInput's placeholder.
     *
     * @param placeholder The TextInput's placeholder.
     */
    void setPlaceholder(String placeholder);

    /**
     * Set the TextInput's minimum length.
     *
     * @param minimumLength The TextInput's minimum length.
     */
    void setMinimumLength(Integer minimumLength);

    /**
     * Set the TextInput's maximumLength length.
     *
     * @param maximumLength The TextInput's maximumLength length.
     */
    void setMaximumLength(Integer maximumLength);

    /**
     * Set the TextInput's custom ID.
     *
     * @param customId The TextInput's identifier.
     */
    void setCustomId(String customId);

    /**
     * Set whether the TextInput is required.
     *
     * @param required Whether the TextInput is required or not.
     */
    void setRequired(boolean required);

    /**
     * Creates a {@link TextInput} instance with the given values.
     *
     * @return The created TextInput instance.
     */
    TextInput build();
}

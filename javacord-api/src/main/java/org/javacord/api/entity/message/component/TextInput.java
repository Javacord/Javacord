package org.javacord.api.entity.message.component;

import java.util.Optional;

public interface TextInput extends LowLevelComponent {

    /**
     * Get the text input's style.
     *
     * @return The text input's style.
     */
    Optional<TextInputStyle> getStyle();

    /**
     * Get the text input's identifier.
     *
     * @return The text input's custom identifier.
     */
    String getCustomId();

    /**
     * Get the text input's label.
     *
     * @return The text input's label.
     */
    Optional<String> getLabel();

    /**
     * Get the text input's label.
     *
     * @return The text input's label.
     */
    Optional<Integer> getMinimumLength();

    /**
     * Get the text input's label.
     *
     * @return The text input's label.
     */
    Optional<Integer> getMaximumLength();

    /**
     * Get whether the text input is disabled.
     *
     * @return Whether the text input is disabled.
     */
    boolean isRequired();

    /**
     * Get the text input's label.
     *
     * @return The text input's label.
     */
    String getValue();

    /**
     * Get the text input's label.
     *
     * @return The text input's label.
     */
    Optional<String> getPlaceholder();

    /**
     * Creates a new text input with the given values.
     *
     * @param style    The style of the text input
     * @param customId The custom ID for the text input.
     * @param label    The label of the text Input
     * @return The created text input.
     */
    static TextInput create(TextInputStyle style, String customId, String label) {
        return new TextInputBuilder(style, customId, label).build();
    }

    /**
     * Creates a new text input with the given values.
     *
     * @param style    The style of the text input
     * @param customId The custom ID for the text input.
     * @param label    The label of the text input
     * @param required Whether this text input is required.
     * @return The created text input.
     */
    static TextInput create(TextInputStyle style, String customId, String label, boolean required) {
        return new TextInputBuilder(style, customId, label)
                .setRequired(required)
                .build();
    }

    /**
     * Creates a new text input with the given values.
     *
     * @param style         The style of the text input
     * @param customId      The custom ID for the text input.
     * @param label         The label of the text input
     * @param minimumLength The minimum length of the text input string.
     * @param maximumLength The maximum length of the text input string.
     * @return The created text input.
     */
    static TextInput create(TextInputStyle style, String customId, String label, Integer minimumLength,
                            Integer maximumLength) {
        return new TextInputBuilder(style, customId, label)
                .setMinimumLength(minimumLength)
                .setMaximumLength(maximumLength)
                .build();
    }

    /**
     * Creates a new text input with the given values.
     *
     * @param style         The style of the text input
     * @param customId      The custom ID for the text input.
     * @param label         The label of the text input
     * @param minimumLength The minimum length of the text input string.
     * @param maximumLength The maximum length of the text input string.
     * @param required      Whether this text input is required.
     * @return The created text input.
     */
    static TextInput create(TextInputStyle style, String customId, String label, Integer minimumLength,
                            Integer maximumLength, boolean required) {
        return new TextInputBuilder(style, customId, label)
                .setMinimumLength(minimumLength)
                .setMaximumLength(maximumLength)
                .setRequired(required)
                .build();
    }

    /**
     * Creates a new text input with the given values.
     *
     * @param style       The style of the text input
     * @param customId    The custom ID for the text input.
     * @param label       The label of the text input
     * @param placeholder The placeholder of the text input.
     * @param value       The pre-defined value of the text input.
     * @return The created text input.
     */
    static TextInput create(TextInputStyle style, String customId, String label, String placeholder, String value) {
        return new TextInputBuilder(style, customId, label)
                .setPlaceholder(placeholder)
                .setValue(value)
                .build();
    }

    /**
     * Creates a new text input with the given values.
     *
     * @param style       The style of the text input
     * @param customId    The custom ID for the text input.
     * @param label       The label of the text input
     * @param placeholder The placeholder of the text input.
     * @param value       The pre-defined value of the text input.
     * @param required    Whether this text input is required.
     * @return The created text input.
     */
    static TextInput create(TextInputStyle style, String customId, String label, String placeholder, String value,
                            boolean required) {
        return new TextInputBuilder(style, customId, label)
                .setPlaceholder(placeholder)
                .setValue(value)
                .setRequired(required)
                .build();
    }
}

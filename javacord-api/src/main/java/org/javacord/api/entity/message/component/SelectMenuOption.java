package org.javacord.api.entity.message.component;

import org.javacord.api.entity.emoji.Emoji;

import java.util.Optional;

public interface SelectMenuOption {

    /**
     * Get the label of the select menu option.
     *
     * @return The label of the option.
     */
    String getLabel();

    /**
     * Get the value of the select menu option.
     *
     * @return The value of the option.
     */
    String getValue();

    /**
     * Get the description of the select menu option.
     *
     * @return The description of the option.
     */
    Optional<String> getDescription();

    /**
     * Get the emoji of the select menu option.
     *
     * @return The emoji of the option.
     */
    Optional<Emoji> getEmoji();

    /**
     * If the option is the default for the menu.
     *
     * @return Is default.
     */
    boolean isDefault();

    /**
     * Creates a new select menu option with the given values.
     *
     * @param label The label for the option.
     * @param value The value for the option.
     * @return The created select menu option.
     */
    static SelectMenuOption create(String label, String value) {
        return new SelectMenuOptionBuilder()
                .setLabel(label)
                .setValue(value)
                .build();
    }

    /**
     * Creates a new select menu option with the given values.
     *
     * @param label The label for the option.
     * @param value The value for the option.
     * @param isDefault If the option is the default option.
     * @return The created select menu option.
     */
    static SelectMenuOption create(String label, String value, boolean isDefault) {
        return new SelectMenuOptionBuilder()
                .setLabel(label)
                .setValue(value)
                .setDefault(isDefault)
                .build();
    }

    /**
     * Creates a new select menu option with the given values.
     *
     * @param label The label for the option.
     * @param value The value for the option.
     * @param description The description for the option.
     * @return The created select menu option.
     */
    static SelectMenuOption create(String label, String value, String description) {
        return new SelectMenuOptionBuilder()
                .setLabel(label)
                .setValue(value)
                .setDescription(description)
                .build();
    }

    /**
     * Creates a new select menu option with the given values.
     *
     * @param label The label for the option.
     * @param value The value for the option.
     * @param description The description for the option.
     * @param isDefault If the option is the default option.
     * @return The created select menu option.
     */
    static SelectMenuOption create(String label, String value, String description, boolean isDefault) {
        return new SelectMenuOptionBuilder()
                .setLabel(label)
                .setValue(value)
                .setDescription(description)
                .setDefault(isDefault)
                .build();
    }

    /**
     * Creates a new select menu option with the given values.
     *
     * @param label The label for the option.
     * @param value The value for the option.
     * @param description The description for the option.
     * @param emoji The emoji for the option.
     * @return The created select menu option.
     */
    static SelectMenuOption create(String label, String value, String description, Emoji emoji) {
        return new SelectMenuOptionBuilder()
                .setLabel(label)
                .setValue(value)
                .setDescription(description)
                .setEmoji(emoji)
                .build();
    }

    /**
     * Creates a new select menu option with the given values.
     *
     * @param label The label for the option.
     * @param value The value for the option.
     * @param description The description for the option.
     * @param unicodeEmoji The emoji for the option as a unicode string.
     * @return The created select menu option.
     */
    static SelectMenuOption create(String label, String value, String description, String unicodeEmoji) {
        return new SelectMenuOptionBuilder()
                .setLabel(label)
                .setValue(value)
                .setDescription(description)
                .setEmoji(unicodeEmoji)
                .build();
    }

    /**
     * Creates a new select menu option with the given values.
     *
     * @param label The label for the option.
     * @param value The value for the option.
     * @param description The description for the option.
     * @param emoji The emoji for the option.
     * @param isDefault If the option is the default option.
     * @return The created select menu option.
     */
    static SelectMenuOption create(String label, String value, String description, Emoji emoji, boolean isDefault) {
        return new SelectMenuOptionBuilder()
                .setLabel(label)
                .setValue(value)
                .setDescription(description)
                .setEmoji(emoji)
                .setDefault(isDefault)
                .build();
    }

    /**
     * Creates a new select menu option with the given values.
     *
     * @param label The label for the option.
     * @param value The value for the option.
     * @param description The description for the option.
     * @param unicodeEmoji The emoji for the option as a unicode string.
     * @param isDefault If the option is the default option.
     * @return The created select menu option.
     */
    static SelectMenuOption create(String label, String value, String description, String unicodeEmoji,
                                   boolean isDefault) {
        return new SelectMenuOptionBuilder()
                .setLabel(label)
                .setValue(value)
                .setDescription(description)
                .setEmoji(unicodeEmoji)
                .setDefault(isDefault)
                .build();
    }
}

package org.javacord.api.interaction.internal;

import org.javacord.api.interaction.SlashCommandOptionChoice;
import org.javacord.api.interaction.SlashCommandOptionChoiceBuilder;

/**
 * This class is internally used by the {@link SlashCommandOptionChoiceBuilder}.
 * You usually don't want to interact with this object.
 */
public interface SlashCommandOptionChoiceBuilderDelegate {

    /**
     * Sets the name of the slash command option choice.
     *
     * @param name The name.
     */
    void setName(String name);

    /**
     * Sets the string value of the slash command option choice.
     *
     * @param value The value.
     */
    void setValue(String value);

    /**
     * Sets the long value of the slash command option choice.
     * Can be any long between -2^53 and 2^53.
     *
     * @param value The value.
     */
    void setValue(long value);

    /**
     * Builds the slash command option choice.
     *
     * @return The slash command option choice.
     */
    SlashCommandOptionChoice build();

}

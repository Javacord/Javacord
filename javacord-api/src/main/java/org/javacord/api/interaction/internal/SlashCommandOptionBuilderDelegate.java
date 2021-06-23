package org.javacord.api.interaction.internal;

import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionBuilder;
import org.javacord.api.interaction.SlashCommandOptionChoice;
import org.javacord.api.interaction.SlashCommandOptionType;

import java.util.List;

/**
 * This class is internally used by the {@link SlashCommandOptionBuilder}.
 * You usually don't want to interact with this object.
 */
public interface SlashCommandOptionBuilderDelegate {

    /**
     * Sets the type of the slash command option.
     *
     * @param type The type.
     */
    void setType(SlashCommandOptionType type);

    /**
     * Sets the name of the slash command option.
     *
     * @param name The name.
     */
    void setName(String name);

    /**
     * Sets the description of the slash command option.
     *
     * @param description The description.
     */
    void setDescription(String description);

    /**
     * Sets if the slash command option is required.
     *
     * @param required Whether or not the option is required.
     */
    void setRequired(boolean required);

    /**
     * Adds an choice for the slash command option.
     *
     * @param choice The choice.
     */
    void addChoice(SlashCommandOptionChoice choice);

    /**
     * Sets the choices of the slash command option.
     *
     * @param choices The choices.
     */
    void setChoices(List<SlashCommandOptionChoice> choices);

    /**
     * Adds an slash command option to the slash command option.
     *
     * @param option The option.
     */
    void addOption(SlashCommandOption option);

    /**
     * Sets the slash commands for the slash command option.
     *
     * @param options The options.
     */
    void setOptions(List<SlashCommandOption> options);

    /**
     * Build the slash command option.
     *
     * @return The built option.
     */
    SlashCommandOption build();
}

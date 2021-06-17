package org.javacord.api.interaction.internal;

import org.javacord.api.interaction.ApplicationCommandOption;
import org.javacord.api.interaction.ApplicationCommandOptionChoice;
import org.javacord.api.interaction.ApplicationCommandOptionType;

import java.util.List;

/**
 * This class is internally used by the {@link org.javacord.api.interaction.ApplicationCommandOptionBuilder}.
 * You usually don't want to interact with this object.
 */
public interface ApplicationCommandOptionBuilderDelegate {

    /**
     * Sets the type of the application command option.
     *
     * @param type The type.
     */
    void setType(ApplicationCommandOptionType type);

    /**
     * Sets the name of the application command option.
     *
     * @param name The name.
     */
    void setName(String name);

    /**
     * Sets the description of the application command option.
     *
     * @param description The description.
     */
    void setDescription(String description);

    /**
     * Sets if the application command option is required.
     *
     * @param required Whether or not the option is required.
     */
    void setRequired(boolean required);

    /**
     * Adds an choice for the application command option.
     *
     * @param choice The choice.
     */
    void addChoice(ApplicationCommandOptionChoice choice);

    /**
     * Sets the choices of the application command option.
     *
     * @param choices The choices.
     */
    void setChoices(List<ApplicationCommandOptionChoice> choices);

    /**
     * Adds an application command option to the application command option.
     *
     * @param option The option.
     */
    void addOption(ApplicationCommandOption option);

    /**
     * Sets the application commands for the application command option.
     *
     * @param options The options.
     */
    void setOptions(List<ApplicationCommandOption> options);

    /**
     * Build the application command option.
     *
     * @return The built option.
     */
    ApplicationCommandOption build();
}

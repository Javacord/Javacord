package org.javacord.api.interaction.internal;

import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandOption;

import java.util.List;

/**
 * This class is internally used by the {@link SlashCommandBuilder}.
 * You usually don't want to interact with this object.
 */
public interface SlashCommandBuilderDelegate extends ApplicationCommandBuilderDelegate<SlashCommand> {

    /**
     * Sets the description of the slash command.
     *
     * @param description The name.
     */
    void setDescription(String description);

    /**
     * Adds a slash command option to the slash command.
     *
     * @param option The option.
     */
    void addOption(SlashCommandOption option);

    /**
     * Sets the slash commands for the slash command.
     *
     * @param options The options.
     */
    void setOptions(List<SlashCommandOption> options);

}

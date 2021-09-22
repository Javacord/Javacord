package org.javacord.api.interaction.internal;

import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandOption;

import java.util.List;

public interface SlashCommandUpdaterDelegate extends ApplicationCommandUpdaterDelegate<SlashCommand> {

    /**
     * Sets the new description of the slash command.
     *
     * @param description The description to set.
     */
    void setDescription(String description);

    /**
     * Sets the new slash command options.
     *
     * @param slashCommandOptions The slash command options to set.
     */
    void setOptions(List<SlashCommandOption> slashCommandOptions);

}

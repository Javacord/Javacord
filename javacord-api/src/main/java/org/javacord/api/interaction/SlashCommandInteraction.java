package org.javacord.api.interaction;

public interface SlashCommandInteraction extends ApplicationCommandInteraction, SlashCommandInteractionOptionsProvider {

    /**
     * Gets the full command name of this slash command including the name of the Subcommand and SubcommandGroup.
     *
     * <p>This is a shorthand method to get the complete name without having to check
     * for Subcommands or SubcommandGroups.
     *
     * @return The full command name.
     */
    String getFullCommandName();
}

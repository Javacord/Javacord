package org.javacord.api.interaction;

import java.util.List;

public interface SlashCommandInteraction extends ApplicationCommandInteraction, SlashCommandInteractionOptionsProvider {

    /**
     * Gets the arguments of this slash command if there are any.
     *
     * <p>This is a shorthand method to avoid checking for Subcommmands or SubcommandGroups
     * to get the slash command arguments.
     *
     * @return The argument options.
     */
    List<SlashCommandInteractionOption> getArguments();

}

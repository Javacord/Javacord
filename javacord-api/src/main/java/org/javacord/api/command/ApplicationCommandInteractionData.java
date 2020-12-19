package org.javacord.api.command;

import org.javacord.api.entity.DiscordEntity;

import java.util.List;

public interface ApplicationCommandInteractionData extends DiscordEntity {

    /**
     * Gets the name of the invoked command.
     *
     * @return The name.
     */
    String getName();

    /**
     * Gets a list with the params and values from the user.
     *
     * @return A list with the params and values from the user.
     */
    List<ApplicationCommandInteractionDataOption> getOptions();

}

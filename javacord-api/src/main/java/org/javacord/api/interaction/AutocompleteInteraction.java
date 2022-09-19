package org.javacord.api.interaction;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AutocompleteInteraction extends SlashCommandInteraction {

    /**
     * Respond with a list of choices that the user should be able to pick of based on his input.
     *
     * @param choices A list of {@link SlashCommandOptionChoice} which the user will see as suggestions.
     * @return A CompletableFuture to check if the response was successful.
     */
    CompletableFuture<Void> respondWithChoices(List<SlashCommandOptionChoice> choices);

    /**
     * Gets the focused option that triggered this AutocompleteInteraction.
     *
     * @return The focused option.
     */
    SlashCommandInteractionOption getFocusedOption();

}

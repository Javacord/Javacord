package org.javacord.api.event.interaction;

import org.javacord.api.interaction.AutocompleteInteraction;

/**
 * An autocomplete command create event.
 */
public interface AutocompleteCreateEvent extends ApplicationCommandEvent {

    /**
     * Gets the created interaction as AutocompleteInteraction, if the interaction is of this type.
     *
     * @return The interaction.
     */
    default AutocompleteInteraction getAutocompleteInteraction() {
        return getInteraction().asAutocompleteInteraction().get();
    }

}

package org.javacord.api.interaction;

import org.javacord.api.util.SafeSpecializable;
import java.util.Optional;

public interface Interaction extends InteractionBase, SafeSpecializable<InteractionBase> {
    /**
     * Get this interaction as slash command interaction if the type matches.
     *
     * @return the interaction as slash command interaction if the type matches; an empty optional otherwise
     */
    default Optional<SlashCommandInteraction> asSlashCommandInteraction() {
        return as(SlashCommandInteraction.class);
    }

    /**
     * Get this interaction as slash command interaction if the type and the command id match.
     *
     * @param commandId The command id to match.
     * @return the interaction as slash command interaction if the properties match; an empty optional otherwise
     */
    default Optional<SlashCommandInteraction> asSlashCommandInteractionWithCommandId(long commandId) {
        return asSlashCommandInteraction().filter(interaction -> interaction.getCommandId() == commandId);
    }

    /**
     * Get this interaction as autocomplete interaction if the type matches.
     *
     * @return the interaction as autocomplete interaction if the type matches; an empty optional otherwise
     */
    default Optional<AutocompleteInteraction> asAutocompleteInteraction() {
        return as(AutocompleteInteraction.class);
    }

    /**
     * Get this interaction as autocomplete interaction if the type and the command id match.
     *
     * @param commandId The command id to match.
     * @return the interaction as autocomplete interaction if the properties match; an empty optional otherwise
     */
    default Optional<AutocompleteInteraction> asAutocompleteInteraction(long commandId) {
        return asAutocompleteInteraction()
                .filter(autocompleteInteraction -> autocompleteInteraction.getCommandId() == commandId);
    }

    /**
     * Get this interaction as user context menu interaction if the type matches.
     *
     * @return the interaction as user context menu interaction if the type matches; an empty optional otherwise
     */
    default Optional<UserContextMenuInteraction> asUserContextMenuInteraction() {
        return as(UserContextMenuInteraction.class);
    }

    /**
     * Get this interaction as user context menu interaction if the type and the command id match.
     *
     * @param commandId The command id to match.
     * @return the interaction as user context menu interaction if the properties match; an empty optional otherwise
     */
    default Optional<UserContextMenuInteraction> asUserContextMenuInteractionWithCommandId(long commandId) {
        return asUserContextMenuInteraction().filter(interaction -> interaction.getCommandId() == commandId);
    }

    /**
     * Get this interaction as message context menu interaction if the type matches.
     *
     * @return the interaction as message context menu interaction if the type matches; an empty optional otherwise
     */
    default Optional<MessageContextMenuInteraction> asMessageContextMenuInteraction() {
        return as(MessageContextMenuInteraction.class);
    }

    /**
     * Get this interaction as message context menu interaction if the type and the command id match.
     *
     * @param commandId The command id to match.
     * @return the interaction as message context menu interaction if the properties match; an empty optional otherwise
     */
    default Optional<MessageContextMenuInteraction> asMessageContextMenuInteractionWithCommandId(long commandId) {
        return asMessageContextMenuInteraction().filter(interaction -> interaction.getCommandId() == commandId);
    }

    /**
     * Get this interaction as message component interaction if the type matches.
     *
     * @return the interaction as message component interaction if the type matches; an empty optional otherwise
     */
    default Optional<MessageComponentInteraction> asMessageComponentInteraction() {
        return as(MessageComponentInteraction.class);
    }

    /**
     * Get this interaction as message component interaction if the type and the given custom id match.
     *
     * @param customId The custom id to match.
     * @return the interaction as message component interaction if the properties match; an empty optional otherwise
     */
    default Optional<MessageComponentInteraction> asMessageComponentInteractionWithCustomId(String customId) {
        return asMessageComponentInteraction().filter(interaction -> interaction.getCustomId().equals(customId));
    }
}

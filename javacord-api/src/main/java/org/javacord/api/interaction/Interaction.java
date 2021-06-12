package org.javacord.api.interaction;

import org.javacord.api.util.SafeSpecializable;

import java.util.Optional;

public interface Interaction extends InteractionBase, SafeSpecializable<InteractionBase> {
    /**
     * Get this interaction as slash command interaction if the type matches.
     *
     * @return the interaction as slash command interaction if the type matches; an empty optional otherwise
     */
    default Optional<ApplicationCommandInteraction> asApplicationCommandInteraction() {
        return as(ApplicationCommandInteraction.class);
    }

    /**
     * Get this interaction as slash command interaction if the type and the command id match.
     *
     * @param commandId The command id to match.
     * @return the interaction as slash command interaction if the properties match; an empty optional otherwise
     */
    default Optional<ApplicationCommandInteraction> asApplicationCommandInteractionWithCommandId(long commandId) {
        return asApplicationCommandInteraction().filter(interaction -> interaction.getCommandId() == commandId);
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

package org.javacord.api.event.interaction;

import org.javacord.api.event.Event;
import org.javacord.api.interaction.ApplicationCommandInteraction;
import org.javacord.api.interaction.Interaction;
import org.javacord.api.interaction.InteractionBase;
import org.javacord.api.interaction.MessageComponentInteraction;

import java.util.Optional;

/**
 * An interaction create event.
 */
public interface InteractionCreateEvent extends Event {

    /**
     * Gets the created interaction.
     *
     * @return The interaction.
     */
    Interaction getInteraction();

    /**
     * Gets the created interaction as ApplicationCommandInteraction, if the interaction is of this type.
     *
     * @return The interaction.
     */
    default Optional<ApplicationCommandInteraction> getApplicationCommandInteraction() {
        return getInteraction().asApplicationCommandInteraction();
    }

    /**
     * Gets the created interaction as ApplicationCommandInteraction, if the interaction is of this type and the
     * command id equals the given command id.
     *
     * @param commandId The command it to match.
     * @return The interaction.
     */
    default Optional<ApplicationCommandInteraction> getApplicationCommandInteractionWithCommandId(long commandId) {
        return getInteraction().asApplicationCommandInteractionWithCommandId(commandId);
    }

    /**
     * Gets the created interaction as MessageComponentInteraction, if the interaction is of this type.
     *
     * @return The interaction.
     */
    default Optional<MessageComponentInteraction> getMessageComponentInteraction() {
        return getInteraction().asMessageComponentInteraction();
    }

    /**
     * Gets the created interaction as MessageComponentInteraction, if the interaction is of this type, and the
     * custom id equals the given custom id.
     *
     * @param customId The custom id to match.
     * @return The interaction.
     */
    default Optional<MessageComponentInteraction> getMessageComponentInteractionWithCustomId(String customId) {
        return getInteraction().asMessageComponentInteractionWithCustomId(customId);
    }

    /**
     * For advanced users: Get the interaction as a desired subtype of interaction.
     * Use this as a shortcut if you know which type of deeply nested interaction type you're expecting.
     * For regular users, we recommend to use the different {@code getXXX()} methods to walk down the interaction
     * inheritance tree.
     *
     * <p>If the interaction is not castable to the specified type, the {@code Optional} will be empty.
     *
     * @param type The type as which to obtain this interaction.
     * @param <T>  The desired type.
     * @return Returns an {@code Optional} of this interaction if it could be cast, otherwise an empty result.
     */
    default <T extends InteractionBase> Optional<T> getInteractionAs(Class<T> type) {
        return getInteraction().as(type);
    }
}

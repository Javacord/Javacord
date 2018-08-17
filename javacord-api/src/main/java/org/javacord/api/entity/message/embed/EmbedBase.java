package org.javacord.api.entity.message.embed;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.Messageable;
import org.javacord.api.entity.message.embed.parts.EditableEmbedField;
import org.javacord.api.entity.message.embed.parts.EmbedField;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * This class represents the base for Embeds.
 */
public interface EmbedBase {

    /**
     * Gets a list of all fields in the embed.
     *
     * @return A list of all fields.
     */
    List<EmbedField> getFields();

    /**
     * Updates all fields that satisfy the predicate after the provided updater.
     *
     * @param predicate A predicate to test what embed fields to update.
     * @param updater   The updater to change the fields.
     * @param <T>       Type variable to be returned by the updater.
     * @see EmbedBuilder#updateFields(Predicate, Function)
     */
    <T extends EmbedField> void updateFields(
            Predicate<EmbedField> predicate,
            Function<EditableEmbedField, T> updater);

    /**
     * Updates all fields within this embed after the provided updater.
     *
     * @param updater The updater to change the fields.
     * @param <T>     Type variable to be returned by the updater.
     */
    default <T extends EmbedField> void updateAllFields(Function<EditableEmbedField, T> updater) {
        this.updateFields(field -> true, updater);
    }

    /**
     * Creates an EmbedBuilder, based on the embed.
     * Use this method for modifying a built embed.
     *
     * @return A builder with the values of this embed.
     * @see Message#getEmbeds()
     */
    EmbedBuilder toBuilder();

    /**
     * Creates an EmbedDraft, based on this embed base.
     * EmbedDrafts will be used to send Embeds to a Messageable.
     *
     * <p>The counter method for getting this base as a draft is {@code EmbedBase#asSentEmbed()}.
     *
     * @return An EmbedDraft object to send to a Messageable.
     * @see Messageable#sendMessage(EmbedDraft)
     */
    EmbedDraft asEmbedDraft();

    /**
     * Gets this embed base as a sent embed, if possible.
     * Otherwise returns an empty optional.
     *
     * <p>This method is e.g. useful when you want to get the provider of an unknown type embed.
     * The counter method for getting this base as a draft is {@code EmbedBase#asEmbedDraft()}.
     *
     * @return An optional that may contain this embed base as a SentEmbed.
     */
    default Optional<SentEmbed> asSentEmbed() {
        if (this instanceof SentEmbed) {
            return Optional.of((SentEmbed) this);
        }
        return Optional.empty();
    }

    /**
     * Checks whether two Embeds are the same, by generating their Json Nodes.
     *
     * @param other The other embed to compare to.
     * @return Whether the embeds are identical.
     */
    boolean equals(Object other);
}

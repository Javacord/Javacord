package org.javacord.api.entity.message.embed;

import org.javacord.api.entity.Nameable;
import org.javacord.api.entity.message.embed.draft.EmbedDraftField;
import org.javacord.api.entity.message.embed.sent.SentEmbedField;
import org.javacord.api.util.Specializable;

import java.util.Optional;

/**
 * Basic representation of an embed's field.
 */
public interface BaseEmbedField extends Nameable, Specializable<BaseEmbedField> {

    /**
     * Gets the embed that this field is part of.
     *
     * @return The parent embed.
     */
    BaseEmbed getEmbed();

    /**
     * Gets the value of the field.
     *
     * @return The value of the field.
     */
    String getValue();

    /**
     * Gets whether or not this field should display inline.
     *
     * @return Whether or not this field should display inline.
     */
    boolean isInline();

    /**
     * Gets this field as a {@code EmbedDraftField}.
     * If this instance already is an {@code EmbedDraftField}, this method will return the cast instance.
     *
     * @return A {@code EmbedDraftField}.
     */
    EmbedDraftField toEmbedDraftField();

    /**
     * Gets this field as a {@code SentEmbedField}.
     *
     * @return A {@code SentEmbedField}.
     */
    default Optional<SentEmbedField> asSentEmbedField() {
        return as(SentEmbedField.class);
    }

}

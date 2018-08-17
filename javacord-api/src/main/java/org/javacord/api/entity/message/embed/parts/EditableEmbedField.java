package org.javacord.api.entity.message.embed.parts;

import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * This class represents an editable field for an embed.
 *
 * @see EmbedBuilder#updateFields(Predicate, Function)
 * @see EmbedBuilder#updateAllFields(Function)
 */
public interface EditableEmbedField extends EmbedField {

    /**
     * Sets the name of this field.
     *
     * @param name The name for the field.
     * @return The field for chaining method calls.
     */
    EditableEmbedField setName(String name);

    /**
     * Sets the value of this field.
     *
     * @param value The value for the field.
     * @return The field for chaining method calls.
     */
    EditableEmbedField setValue(String value);

    /**
     * Sets whether this field should be inline.
     *
     * @param inline Whether this field should be inline.
     * @return The field for chaining method calls.
     */
    EditableEmbedField setInline(boolean inline);
}

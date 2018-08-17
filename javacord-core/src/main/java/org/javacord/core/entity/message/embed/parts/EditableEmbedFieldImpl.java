package org.javacord.core.entity.message.embed.parts;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.message.embed.parts.EditableEmbedField;
import org.javacord.api.entity.message.embed.parts.EmbedField;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * The implementation for an editable field for an embed.
 */
public class EditableEmbedFieldImpl extends EmbedFieldImpl implements EditableEmbedField {

    /**
     * Creates a new instance from given information.
     *
     * @param name   The initial name of the field.
     * @param value  The initial value of the field.
     * @param inline The initial value whether the field should be inline.
     */
    public EditableEmbedFieldImpl(
            String name,
            String value,
            boolean inline) {
        super(name, value, inline);
    }

    /**
     * Creates a new instance from a {@link EmbedFieldImpl}.
     *
     * @param field The field to inherit from.
     * @see EmbedBuilder#updateFields(Predicate, java.util.function.Function)
     * @see EmbedBuilder#updateAllFields(java.util.function.Function)
     * @see EmbedField#asEditableEmbedField()
     */
    public EditableEmbedFieldImpl(EmbedField field) {
        super(
                field.getName(),
                field.getValue(),
                field.isInline()
        );
    }

    @Override
    public EditableEmbedField setName(String name) {
        super.name = name;
        return this;
    }

    @Override
    public EditableEmbedField setValue(String value) {
        super.value = value;
        return this;
    }

    @Override
    public EditableEmbedField setInline(boolean inline) {
        super.inline = inline;
        return this;
    }
}

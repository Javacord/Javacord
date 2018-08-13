package org.javacord.api.entity.message.embed.parts;

/**
 * This class represents a field for an embed.
 */
public interface EmbedField {

    /**
     * Gets the name of this field.
     *
     * @return The name of this field.
     */
    String getName();

    /**
     * Gets the value of this field.
     *
     * @return The value of this field.
     */
    String getValue();

    /**
     * Gets whether this field is inline.
     *
     * @return Whether this field is inline.
     */
    boolean isInline();

    /**
     * Returns a new, editable embed field, based on this field.
     * Note that the EditableEmbedField returned is a not directly connected to the given field.
     *
     * @return The new EditableEmbedField.
     */
    EditableEmbedField asEditableEmbedField();
}

package org.javacord.api.entity.message.embed;

/**
 * This interface represents an embed field that can be edited.
 */
public interface EditableEmbedField extends EmbedField {

    /**
     * Sets the name of the field.
     *
     * @param name The name of the field.
     */
    void setName(String name);

    /**
     * Sets the value of the field.
     *
     * @param value The value of the field.
     */
    void setValue(String value);

    /**
     * Sets whether or not this field should display inline.
     *
     * @param inline Whether or not this field should display inline.
     */
    void setInline(boolean inline);

}
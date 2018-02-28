package de.btobastian.javacord.entity.message.embed;

/**
 * This interface represents an embed field.
 */
public interface EmbedField {

    /**
     * Gets the name of the field.
     *
     * @return The name of the field.
     */
    String getName();

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

}
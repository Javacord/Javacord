package org.javacord.api.entity.message.embed;

import org.javacord.api.entity.Nameable;
import org.javacord.api.util.Specializable;

public interface BaseEmbedField extends Nameable, Specializable<BaseEmbedField> {

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

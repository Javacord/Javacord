package org.javacord.api.entity.message.component.internal;

import org.javacord.api.entity.message.component.SelectMenuDefaultValue;
import org.javacord.api.entity.message.component.SelectMenuDefaultValueType;

public interface SelectMenuDefaultValueBuilderDelegate {

    /**
     * Copy the give select menu default value.
     *
     * @param selectMenuDefaultValue The select menu default value.
     */
    void copy(SelectMenuDefaultValue selectMenuDefaultValue);

    /**
     * Set the id of a user, role, or channel.
     *
     * @param id The id.
     */
    void setId(long id);

    /**
     * Set the type of value that id represents.
     *
     * @param type The type.
     */
    void setType(SelectMenuDefaultValueType type);

    /**
     * Build the select menu default value.
     *
     * @return The default value.
     */
    SelectMenuDefaultValue build();
}

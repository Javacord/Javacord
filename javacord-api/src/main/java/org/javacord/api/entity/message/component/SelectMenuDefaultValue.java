package org.javacord.api.entity.message.component;

public interface SelectMenuDefaultValue {

    /**
     * Get the id of a user, role, or channel.
     *
     * @return The id of a user, role, or channel.
     */
    long getId();

    /**
     * Get type of value that id represents.
     *
     * @return Type of value that id represents. Either user, role, or channel.
     */
    SelectMenuDefaultValueType getType();

    /**
     * Creates a new select menu default value with the given values.
     *
     * @param id The id for the default value.
     * @param type The type for the default value.
     * @return The created select menu default value.
     */
    static SelectMenuDefaultValue create(long id, SelectMenuDefaultValueType type) {
        return new SelectMenuDefaultValueBuilder()
                .setId(id)
                .setType(type)
                .build();
    }
}

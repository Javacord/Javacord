package org.javacord.api.interaction.internal;

import org.javacord.api.interaction.ApplicationCommandOptionChoice;

/**
 * This class is internally used by the {@link org.javacord.api.interaction.ApplicationCommandOptionChoiceBuilder}.
 * You usually don't want to interact with this object.
 */
public interface ApplicationCommandOptionChoiceBuilderDelegate {

    /**
     * Sets the name of the application command option choice.
     *
     * @param name The name.
     */
    void setName(String name);

    /**
     * Sets the string value of the application command option choice.
     *
     * @param value The value.
     */
    void setValue(String value);

    /**
     * Sets the int value of the application command option choice.
     *
     * @param value The value.
     */
    void setValue(int value);

    /**
     * Builds the application command option choice.
     *
     * @return The application command option choice.
     */
    ApplicationCommandOptionChoice build();

}

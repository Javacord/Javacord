package org.javacord.api.interaction;

import org.javacord.api.interaction.internal.ApplicationCommandOptionChoiceBuilderDelegate;
import org.javacord.api.util.internal.DelegateFactory;

public class ApplicationCommandOptionChoiceBuilder {

    private final ApplicationCommandOptionChoiceBuilderDelegate delegate =
            DelegateFactory.createApplicationCommandOptionChoiceBuilderDelegate();

    /**
     * Creates a new application command option choice builder.
     */
    public ApplicationCommandOptionChoiceBuilder() { }

    /**
     * Sets the name of the application command option choice.
     *
     * @param name The name.
     * @return The current instance in order to chain call methods.
     */
    public ApplicationCommandOptionChoiceBuilder setName(String name) {
        delegate.setName(name);
        return this;
    }

    /**
     * Sets the string value of the application command option choice.
     *
     * @param value The value.
     * @return The current instance in order to chain call methods.
     */
    public ApplicationCommandOptionChoiceBuilder setValue(String value) {
        delegate.setValue(value);
        return this;
    }

    /**
     * Sets the int value of the application command option choice.
     *
     * @param value The value.
     * @return The current instance in order to chain call methods.
     */
    public ApplicationCommandOptionChoiceBuilder setValue(int value) {
        delegate.setValue(value);
        return this;
    }

    /**
     * Builds the application command option choice.
     *
     * @return The application command option choice.
     */
    public ApplicationCommandOptionChoice build() {
        return delegate.build();
    }
}

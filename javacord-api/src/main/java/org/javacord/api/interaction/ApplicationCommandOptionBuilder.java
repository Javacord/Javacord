package org.javacord.api.interaction;

import org.javacord.api.interaction.internal.ApplicationCommandOptionBuilderDelegate;
import org.javacord.api.util.internal.DelegateFactory;

import java.util.List;

public class ApplicationCommandOptionBuilder {

    private final ApplicationCommandOptionBuilderDelegate delegate =
            DelegateFactory.createApplicationCommandOptionBuilderDelegate();

    /**
     * Creates a new application command option builder.
     */
    public ApplicationCommandOptionBuilder() { }

    /**
     * Sets the type of the application command option.
     *
     * @param type The type.
     * @return The current instance in order to chain call methods.
     */
    public ApplicationCommandOptionBuilder setType(ApplicationCommandOptionType type) {
        delegate.setType(type);
        return this;
    }

    /**
     * Sets the name of the application command option.
     *
     * @param name The name.
     * @return The current instance in order to chain call methods.
     */
    public ApplicationCommandOptionBuilder setName(String name) {
        delegate.setName(name);
        return this;
    }

    /**
     * Sets the description of the application command option.
     *
     * @param description The description.
     * @return The current instance in order to chain call methods.
     */
    public ApplicationCommandOptionBuilder setDescription(String description) {
        delegate.setDescription(description);
        return this;
    }

    /**
     * Sets if the application command option is required.
     *
     * @param required Whether or not the option is required.
     * @return The current instance in order to chain call methods.
     */
    public ApplicationCommandOptionBuilder setRequired(boolean required) {
        delegate.setRequired(required);
        return this;
    }

    /**
     * Adds an choice for the application command option.
     *
     * @param choice The choice.
     * @return The current instance in order to chain call methods.
     */
    public ApplicationCommandOptionBuilder addChoice(ApplicationCommandOptionChoice choice) {
        delegate.addChoice(choice);
        return this;
    }

    /**
     * Adds an string choice for the application command option.
     *
     * @param name The name of the choice.
     * @param value The value of the choice.
     * @return The current instance in order to chain call methods.
     */
    public ApplicationCommandOptionBuilder addChoice(String name, String value) {
        delegate.addChoice(new ApplicationCommandOptionChoiceBuilder().setName(name).setValue(value).build());
        return this;
    }

    /**
     * Adds an int choice for the application command option.
     *
     * @param name The name of the choice.
     * @param value The value of the choice.
     * @return The current instance in order to chain call methods.
     */
    public ApplicationCommandOptionBuilder addChoice(String name, int value) {
        delegate.addChoice(new ApplicationCommandOptionChoiceBuilder().setName(name).setValue(value).build());
        return this;
    }

    /**
     * Sets the choices of the application command option.
     *
     * @param choices The choices.
     * @return The current instance in order to chain call methods.
     */
    public ApplicationCommandOptionBuilder setChoices(List<ApplicationCommandOptionChoice> choices) {
        delegate.setChoices(choices);
        return this;
    }

    /**
     * Adds an application command option to the application command option.
     *
     * @param option The option.
     * @return The current instance in order to chain call methods.
     */
    public ApplicationCommandOptionBuilder addOption(ApplicationCommandOption option) {
        delegate.addOption(option);
        return this;
    }

    /**
     * Sets the application commands for the application command option.
     *
     * @param options The options.
     * @return The current instance in order to chain call methods.
     */
    public ApplicationCommandOptionBuilder setOptions(List<ApplicationCommandOption> options) {
        delegate.setOptions(options);
        return this;
    }

    /**
     * Builds the application command option.
     *
     * @return The built option.
     */
    public ApplicationCommandOption build() {
        return delegate.build();
    }
}

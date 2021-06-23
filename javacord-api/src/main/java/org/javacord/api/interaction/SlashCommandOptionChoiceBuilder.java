package org.javacord.api.interaction;

import org.javacord.api.interaction.internal.SlashCommandOptionChoiceBuilderDelegate;
import org.javacord.api.util.internal.DelegateFactory;

public class SlashCommandOptionChoiceBuilder {

    private final SlashCommandOptionChoiceBuilderDelegate delegate =
            DelegateFactory.createSlashCommandOptionChoiceBuilderDelegate();

    /**
     * Creates a new slash command option choice builder.
     */
    public SlashCommandOptionChoiceBuilder() { }

    /**
     * Sets the name of the slash command option choice.
     *
     * @param name The name.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandOptionChoiceBuilder setName(String name) {
        delegate.setName(name);
        return this;
    }

    /**
     * Sets the string value of the slash command option choice.
     *
     * @param value The value.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandOptionChoiceBuilder setValue(String value) {
        delegate.setValue(value);
        return this;
    }

    /**
     * Sets the int value of the slash command option choice.
     *
     * @param value The value.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandOptionChoiceBuilder setValue(int value) {
        delegate.setValue(value);
        return this;
    }

    /**
     * Builds the slash command option choice.
     *
     * @return The slash command option choice.
     */
    public SlashCommandOptionChoice build() {
        return delegate.build();
    }
}

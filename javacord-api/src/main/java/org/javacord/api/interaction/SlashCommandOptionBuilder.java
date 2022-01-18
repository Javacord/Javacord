package org.javacord.api.interaction;

import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.interaction.internal.SlashCommandOptionBuilderDelegate;
import org.javacord.api.util.internal.DelegateFactory;
import java.util.Collection;
import java.util.List;

public class SlashCommandOptionBuilder {

    private final SlashCommandOptionBuilderDelegate delegate =
            DelegateFactory.createSlashCommandOptionBuilderDelegate();

    /**
     * Creates a new slash command option builder.
     */
    public SlashCommandOptionBuilder() { }

    /**
     * Sets the type of the slash command option.
     *
     * @param type The type.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandOptionBuilder setType(SlashCommandOptionType type) {
        delegate.setType(type);
        return this;
    }

    /**
     * Sets the name of the slash command option.
     *
     * @param name The name.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandOptionBuilder setName(String name) {
        delegate.setName(name);
        return this;
    }

    /**
     * Sets the description of the slash command option.
     *
     * @param description The description.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandOptionBuilder setDescription(String description) {
        delegate.setDescription(description);
        return this;
    }

    /**
     * Sets if the slash command option is required.
     *
     * @param required Whether the option is required.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandOptionBuilder setRequired(boolean required) {
        delegate.setRequired(required);
        return this;
    }

    /**
     * Sets if this option can be autocompleted.
     *
     * @param autocompletable Whether the option can be autocompleted.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandOptionBuilder setAutocompletable(boolean autocompletable) {
        delegate.setAutocompletable(autocompletable);
        return this;
    }

    /**
     * Adds a choice for the slash command option.
     *
     * @param choice The choice.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandOptionBuilder addChoice(SlashCommandOptionChoice choice) {
        delegate.addChoice(choice);
        return this;
    }

    /**
     * Adds a string choice for the slash command option.
     *
     * @param name  The name of the choice.
     * @param value The value of the choice.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandOptionBuilder addChoice(String name, String value) {
        delegate.addChoice(new SlashCommandOptionChoiceBuilder().setName(name).setValue(value).build());
        return this;
    }

    /**
     * Adds an int choice for the slash command option.
     *
     * @param name  The name of the choice.
     * @param value The value of the choice.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandOptionBuilder addChoice(String name, int value) {
        delegate.addChoice(new SlashCommandOptionChoiceBuilder().setName(name).setValue(value).build());
        return this;
    }

    /**
     * Sets the choices of the slash command option.
     *
     * @param choices The choices.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandOptionBuilder setChoices(List<SlashCommandOptionChoice> choices) {
        delegate.setChoices(choices);
        return this;
    }

    /**
     * Adds a slash command option to the slash command option.
     *
     * @param option The option.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandOptionBuilder addOption(SlashCommandOption option) {
        delegate.addOption(option);
        return this;
    }

    /**
     * Sets the slash commands for the slash command option.
     *
     * @param options The options.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandOptionBuilder setOptions(List<SlashCommandOption> options) {
        delegate.setOptions(options);
        return this;
    }

    /**
     * Adds a channel type to the slash command option.
     *
     * @param channelType The channel type.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandOptionBuilder addChannelType(ChannelType channelType) {
        delegate.addChannelType(channelType);
        return this;
    }

    /**
     * Sets the channel types for the slash command option.
     *
     * @param channelTypes The channel types.
     * @return The current instance in order to chain call methods.
     */
    public SlashCommandOptionBuilder setChannelTypes(Collection<ChannelType> channelTypes) {
        delegate.setChannelTypes(channelTypes);
        return this;
    }

    /**
     * Sets the minimum value permitted for the {@link SlashCommandOptionType#LONG} slash command option.
     *
     * @param longMinValue The minimum value permitted.
     * @return The current instance in order to chain call methods
     */
    public SlashCommandOptionBuilder setLongMinValue(long longMinValue) {
        delegate.setLongMinValue(longMinValue);
        return this;
    }

    /**
     * Sets the maximum value permitted for the {@link SlashCommandOptionType#LONG} slash command option.
     *
     * @param longMaxValue The maximum value permitted.
     * @return The current instance in order to chain call methods
     */
    public SlashCommandOptionBuilder setLongMaxValue(long longMaxValue) {
        delegate.setLongMaxValue(longMaxValue);
        return this;
    }

    /**
     * Sets the minimum value permitted for the {@link SlashCommandOptionType#DECIMAL} slash command option.
     *
     * @param decimalMinValue The minimum value permitted.
     * @return The current instance in order to chain call methods
     */
    public SlashCommandOptionBuilder setDecimalMinValue(double decimalMinValue) {
        delegate.setDecimalMinValue(decimalMinValue);
        return this;
    }

    /**
     * Sets the maximum value permitted for the {@link SlashCommandOptionType#DECIMAL} slash command option.
     *
     * @param decimalMaxValue The maximum value permitted.
     * @return The current instance in order to chain call methods
     */
    public SlashCommandOptionBuilder setDecimalMaxValue(double decimalMaxValue) {
        delegate.setDecimalMaxValue(decimalMaxValue);
        return this;
    }

    /**
     * Builds the slash command option.
     *
     * @return The built option.
     */
    public SlashCommandOption build() {
        return delegate.build();
    }
}

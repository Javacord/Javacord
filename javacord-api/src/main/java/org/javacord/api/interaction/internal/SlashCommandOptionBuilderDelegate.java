package org.javacord.api.interaction.internal;

import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionBuilder;
import org.javacord.api.interaction.SlashCommandOptionChoice;
import org.javacord.api.interaction.SlashCommandOptionType;

import java.util.Collection;
import java.util.List;

/**
 * This class is internally used by the {@link SlashCommandOptionBuilder}.
 * You usually don't want to interact with this object.
 */
public interface SlashCommandOptionBuilderDelegate {

    /**
     * Sets the type of the slash command option.
     *
     * @param type The type.
     */
    void setType(SlashCommandOptionType type);

    /**
     * Sets the name of the slash command option.
     *
     * @param name The name.
     */
    void setName(String name);

    /**
     * Sets the description of the slash command option.
     *
     * @param description The description.
     */
    void setDescription(String description);

    /**
     * Sets if the slash command option is required.
     *
     * @param required Whether the option is required.
     */
    void setRequired(boolean required);

    /**
     * Sets if this option can be autocompleted.
     *
     * @param autocompletable Whether the option can be autocompleted.
     */
    void setAutocompletable(boolean autocompletable);

    /**
     * Adds a choice for the slash command option.
     *
     * @param choice The choice.
     */
    void addChoice(SlashCommandOptionChoice choice);

    /**
     * Sets the choices of the slash command option.
     *
     * @param choices The choices.
     */
    void setChoices(List<SlashCommandOptionChoice> choices);

    /**
     * Adds a slash command option to the slash command option.
     *
     * @param option The option.
     */
    void addOption(SlashCommandOption option);

    /**
     * Sets the slash commands for the slash command option.
     *
     * @param options The options.
     */
    void setOptions(List<SlashCommandOption> options);

    /**
     * Add a channel type to the slash command option.
     *
     * @param channelType The channel type.
     */
    void addChannelType(ChannelType channelType);

    /**
     * Sets the channel types for the slash command option.
     *
     * @param channelTypes The channel types.
     */
    void setChannelTypes(Collection<ChannelType> channelTypes);

    /**
     * Sets the {@link SlashCommandOptionType#LONG} min value for the slash command option.
     *
     * @param longMinValue The long min value.
     */
    void setLongMinValue(long longMinValue);

    /**
     * Sets the {@link SlashCommandOptionType#LONG} max value for the slash command option.
     *
     * @param longMaxValue The long max value.
     */
    void setLongMaxValue(long longMaxValue);

    /**
     * Sets the {@link SlashCommandOptionType#DECIMAL} min value for the slash command option.
     *
     * @param decimalMinValue The decimal min value.
     */
    void setDecimalMinValue(double decimalMinValue);

    /**
     * Sets the {@link SlashCommandOptionType#DECIMAL} max value for the slash command option.
     *
     * @param decimalMaxValue The decimal max value.
     */
    void setDecimalMaxValue(double decimalMaxValue);

    /**
     * Build the slash command option.
     *
     * @return The built option.
     */
    SlashCommandOption build();
}

package org.javacord.api.interaction;

import org.javacord.api.entity.channel.ChannelType;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * An slash command's option (i.e., a parameter for the command).
 */
public interface SlashCommandOption {

    /**
     * Gets the type of this option.
     *
     * @return The type.
     */
    SlashCommandOptionType getType();

    /**
     * Gets the name of this option.
     *
     * @return The name of this option.
     */
    String getName();

    /**
     * Gets the description of this option.
     *
     * @return The description of this option.
     */
    String getDescription();

    /**
     * Checks whether this option is required.
     *
     * @return Whether this option is required.
     */
    boolean isRequired();

    /**
     * Checks whether this option can be autocompleted.
     *
     * @return Whether this option can be autocompleted.
     */
    boolean isAutocompletable();

    /**
     * Gets a list with all choices for this option.
     *
     * <p>If this option has any choices, they are the only valid values for a user to pick.
     *
     * @return A list with all choices for this option.
     */
    List<SlashCommandOptionChoice> getChoices();

    /**
     * If this option is a subcommand or subcommand group type, this nested options will be the parameters.
     *
     * @return A list with the nested options.
     */
    List<SlashCommandOption> getOptions();

    /**
     * If the option is of type {@link SlashCommandOptionType#CHANNEL}, the channels shown will be restricted to these
     * types.
     *
     * @return A set with the channel types that are shown.
     */
    Set<ChannelType> getChannelTypes();

    /**
     * If the option is an {@link SlashCommandOptionType#LONG} type, the minimum value permitted.
     *
     * @return The minimum value permitted.
     */
    Optional<Long> getLongMinValue();

    /**
     * If the option is an {@link SlashCommandOptionType#LONG} type, the maximum value permitted.
     *
     * @return The maximum value permitted.
     */
    Optional<Long> getLongMaxValue();

    /**
     * If the option is an {@link SlashCommandOptionType#DECIMAL} type, the minimum value permitted.
     *
     * @return The minimum value permitted.
     */
    Optional<Double> getDecimalMinValue();

    /**
     * If the option is an {@link SlashCommandOptionType#DECIMAL} type, the maximum value permitted.
     *
     * @return The maximum value permitted.
     */
    Optional<Double> getDecimalMaxValue();

    /**
     * Create a new slash command option to be used with a slash command builder.
     * This is a convenience method.
     *
     * @param type        The type of the option.
     * @param name        The name of the option.
     * @param description The description of the option.
     * @param required    Whether this option is required.
     * @return The new slash command option builder.
     */
    static SlashCommandOption create(SlashCommandOptionType type,
                                     String name,
                                     String description,
                                     boolean required) {
        return new SlashCommandOptionBuilder()
                .setType(type)
                .setName(name)
                .setDescription(description)
                .setRequired(required)
                .build();
    }

    /**
     * Create a new slash command option to be used with a slash command builder.
     * This is a convenience method.
     *
     * @param type        The type of the option.
     * @param name        The name of the option.
     * @param description The description of the option.
     * @return The new slash command option builder.
     */
    static SlashCommandOption create(SlashCommandOptionType type, String name, String description) {
        return new SlashCommandOptionBuilder()
                .setType(type)
                .setName(name)
                .setDescription(description)
                .build();
    }

    /**
     * Create a new subcommand or subcommand group to be used with a slash command builder.
     * This is a convenience method.
     *
     * @param type        The type of the option. Must be either SUBCOMMAND or SUBCOMMAND_GROUP.
     * @param name        The name of the option.
     * @param description The description of the option.
     * @param options     The options of this subcommand or subcommand group.
     * @return The new slash command option builder.
     */
    static SlashCommandOption createWithOptions(SlashCommandOptionType type,
                                                String name,
                                                String description,
                                                SlashCommandOptionBuilder... options) {
        return createWithOptions(type, name, description,
                Arrays.stream(options)
                        .map(SlashCommandOptionBuilder::build)
                        .collect(Collectors.toList()));
    }

    /**
     * Create a new subcommand or subcommand group to be used with a slash command builder.
     * This is a convenience method.
     *
     * @param type        The type of the option. Must be either SUBCOMMAND or SUBCOMMAND_GROUP.
     * @param name        The name of the option.
     * @param description The description of the option.
     * @param options     The options of this subcommand or subcommand group.
     * @return The new slash command option builder.
     */
    static SlashCommandOption createWithOptions(SlashCommandOptionType type,
                                                String name,
                                                String description,
                                                List<SlashCommandOption> options) {
        return new SlashCommandOptionBuilder()
                .setType(type)
                .setName(name)
                .setDescription(description)
                .setOptions(options)
                .build();
    }

    /**
     * Create a new slash command option to be used with a slash command builder.
     * This is a convenience method.
     *
     * @param type        The type of the option.
     * @param name        The name of the option.
     * @param description The description of the option.
     * @param required    Whether this option is required.
     * @param choices     The choices of the option.
     * @return The new slash command option builder.
     */
    static SlashCommandOption createWithChoices(SlashCommandOptionType type,
                                                String name,
                                                String description,
                                                boolean required,
                                                SlashCommandOptionChoiceBuilder... choices) {
        return createWithChoices(type, name, description, required, Arrays.stream(choices)
                .map(SlashCommandOptionChoiceBuilder::build)
                .collect(Collectors.toList()));
    }

    /**
     * Create a new slash command option to be used with a slash command builder.
     * This is a convenience method.
     *
     * @param type        The type of the option.
     * @param name        The name of the option.
     * @param description The description of the option.
     * @param required    Whether this option is required.
     * @param choices     The choices of the option.
     * @return The new slash command option builder.
     */
    static SlashCommandOption createWithChoices(SlashCommandOptionType type,
                                                String name,
                                                String description,
                                                boolean required,
                                                List<SlashCommandOptionChoice> choices) {
        return new SlashCommandOptionBuilder()
                .setType(type)
                .setName(name)
                .setDescription(description)
                .setRequired(required)
                .setChoices(choices)
                .build();
    }

    /**
     * Create a new slash command option to be used with a slash command builder.
     * This is a convenience method.
     *
     * @param name         The name of the option.
     * @param description  The description of the option.
     * @param required     Whether this option is required.
     * @param channelTypes Channel types that are shown.
     * @return The new slash command option builder.
     */
    static SlashCommandOption createChannelOption(String name,
                                                  String description,
                                                  boolean required,
                                                  Collection<ChannelType> channelTypes) {
        return new SlashCommandOptionBuilder()
                .setType(SlashCommandOptionType.CHANNEL)
                .setName(name)
                .setDescription(description)
                .setRequired(required)
                .setChannelTypes(channelTypes)
                .build();
    }

    /**
     * Create a new {@link SlashCommandOptionType#DECIMAL} slash command option to be used with a slash command builder.
     * This is a convenience method.
     *
     * @param name        The name of the option.
     * @param description The description of the option.
     * @param required    Whether this option is required
     * @return The new slash command option builder.
     */
    static SlashCommandOption createDecimalOption(String name,
                                                  String description,
                                                  boolean required) {
        return new SlashCommandOptionBuilder()
                .setType(SlashCommandOptionType.DECIMAL)
                .setName(name)
                .setDescription(description)
                .setRequired(required)
                .build();
    }

    /**
     * Create a new {@link SlashCommandOptionType#DECIMAL} slash command option to be used with a slash command builder.
     * This is a convenience method.
     *
     * @param name         The name of the option.
     * @param description  The description of the option.
     * @param required     Whether this option is required
     * @param autocomplete Whether this option can be autocompleted
     * @return The new slash command option builder.
     */
    static SlashCommandOption createDecimalOption(String name,
                                                  String description,
                                                  boolean required,
                                                  boolean autocomplete) {
        return new SlashCommandOptionBuilder()
                .setType(SlashCommandOptionType.DECIMAL)
                .setName(name)
                .setDescription(description)
                .setRequired(required)
                .setAutocompletable(autocomplete)
                .build();
    }

    /**
     * Create a new {@link SlashCommandOptionType#DECIMAL} slash command option to be used with a slash command builder.
     * This is a convenience method.
     *
     * @param name        The name of the option.
     * @param description The description of the option.
     * @param required    Whether this option is required
     * @param minValue    The minimum value permitted.
     * @param maxValue    The maximum value permitted.
     * @return The new slash command option builder.
     */
    static SlashCommandOption createDecimalOption(String name,
                                                  String description,
                                                  boolean required,
                                                  double minValue,
                                                  double maxValue) {
        return new SlashCommandOptionBuilder()
                .setType(SlashCommandOptionType.DECIMAL)
                .setName(name)
                .setDescription(description)
                .setRequired(required)
                .setDecimalMinValue(minValue)
                .setDecimalMaxValue(maxValue)
                .build();
    }

    /**
     * Create a new {@link SlashCommandOptionType#LONG} slash command option to be used with a slash command builder.
     * This is a convenience method.
     *
     * @param name        The name of the option.
     * @param description The description of the option.
     * @param required    Whether this option is required
     * @return The new slash command option builder.
     */
    static SlashCommandOption createLongOption(String name,
                                               String description,
                                               boolean required) {
        return new SlashCommandOptionBuilder()
                .setType(SlashCommandOptionType.LONG)
                .setName(name)
                .setDescription(description)
                .setRequired(required)
                .build();
    }

    /**
     * Create a new {@link SlashCommandOptionType#LONG} slash command option to be used with a slash command builder.
     * This is a convenience method.
     *
     * @param name        The name of the option.
     * @param description The description of the option.
     * @param required    Whether this option is required
     * @param minValue    The minimum value permitted.
     * @param maxValue    The maximum value permitted.
     * @return The new slash command option builder.
     */
    static SlashCommandOption createLongOption(String name,
                                               String description,
                                               boolean required,
                                               long minValue,
                                               long maxValue) {
        return new SlashCommandOptionBuilder()
                .setType(SlashCommandOptionType.LONG)
                .setName(name)
                .setDescription(description)
                .setRequired(required)
                .setLongMinValue(minValue)
                .setLongMaxValue(maxValue)
                .build();
    }

    /**
     * Create a new {@link SlashCommandOptionType#LONG} slash command option to be used with a slash command builder.
     * This is a convenience method.
     *
     * @param name         The name of the option.
     * @param description  The description of the option.
     * @param required     Whether this option is required
     * @param autocomplete Whether this option can be autocompleted
     * @return The new slash command option builder.
     */
    static SlashCommandOption createLongOption(String name,
                                               String description,
                                               boolean required,
                                               boolean autocomplete) {
        return new SlashCommandOptionBuilder()
                .setType(SlashCommandOptionType.LONG)
                .setName(name)
                .setDescription(description)
                .setRequired(required)
                .setAutocompletable(autocomplete)
                .build();
    }

    /**
     * Create a new {@link SlashCommandOptionType#STRING} slash command option to be used with a slash command builder.
     * This is a convenience method.
     *
     * @param name        The name of the option.
     * @param description The description of the option.
     * @param required    Whether this option is required
     * @return The new slash command option builder.
     */
    static SlashCommandOption createStringOption(String name,
                                                 String description,
                                                 boolean required) {
        return new SlashCommandOptionBuilder()
                .setType(SlashCommandOptionType.STRING)
                .setName(name)
                .setDescription(description)
                .setRequired(required)
                .build();
    }

    /**
     * Create a new {@link SlashCommandOptionType#STRING} slash command option to be used with a slash command builder.
     * This is a convenience method.
     *
     * @param name         The name of the option.
     * @param description  The description of the option.
     * @param required     Whether this option is required
     * @param autocomplete Whether this option can be autocompleted
     * @return The new slash command option builder.
     */
    static SlashCommandOption createStringOption(String name,
                                                 String description,
                                                 boolean required,
                                                 boolean autocomplete) {
        return new SlashCommandOptionBuilder()
                .setType(SlashCommandOptionType.STRING)
                .setName(name)
                .setDescription(description)
                .setRequired(required)
                .setAutocompletable(autocomplete)
                .build();
    }

    /**
     * Create a new {@link SlashCommandOptionType#ROLE} slash command option to be used with a slash command builder.
     * This is a convenience method.
     *
     * @param name        The name of the option.
     * @param description The description of the option.
     * @param required    Whether this option is required
     * @return The new slash command option builder.
     */
    static SlashCommandOption createRoleOption(String name,
                                               String description,
                                               boolean required) {
        return new SlashCommandOptionBuilder()
                .setType(SlashCommandOptionType.ROLE)
                .setName(name)
                .setDescription(description)
                .setRequired(required)
                .build();
    }

    /**
     * Create a new {@link SlashCommandOptionType#MENTIONABLE} slash command option
     * to be used with a slash command builder.
     * This is a convenience method.
     *
     * @param name        The name of the option.
     * @param description The description of the option.
     * @param required    Whether this option is required
     * @return The new slash command option builder.
     */
    static SlashCommandOption createMentionableOption(String name,
                                                      String description,
                                                      boolean required) {
        return new SlashCommandOptionBuilder()
                .setType(SlashCommandOptionType.MENTIONABLE)
                .setName(name)
                .setDescription(description)
                .setRequired(required)
                .build();
    }

    /**
     * Create a new {@link SlashCommandOptionType#USER} slash command option to be used with a slash command builder.
     * This is a convenience method.
     *
     * @param name        The name of the option.
     * @param description The description of the option.
     * @param required    Whether this option is required
     * @return The new slash command option builder.
     */
    static SlashCommandOption createUserOption(String name,
                                               String description,
                                               boolean required) {
        return new SlashCommandOptionBuilder()
                .setType(SlashCommandOptionType.USER)
                .setName(name)
                .setDescription(description)
                .setRequired(required)
                .build();
    }

    /**
     * Create a new {@link SlashCommandOptionType#BOOLEAN} slash command option to be used with a slash command builder.
     * This is a convenience method.
     *
     * @param name        The name of the option.
     * @param description The description of the option.
     * @param required    Whether this option is required
     * @return The new slash command option builder.
     */
    static SlashCommandOption createBooleanOption(String name,
                                                  String description,
                                                  boolean required) {
        return new SlashCommandOptionBuilder()
                .setType(SlashCommandOptionType.BOOLEAN)
                .setName(name)
                .setDescription(description)
                .setRequired(required)
                .build();
    }

}

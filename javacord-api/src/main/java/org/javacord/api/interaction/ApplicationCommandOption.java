package org.javacord.api.interaction;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An application command's option (i.e., a parameter for the command).
 */
public interface ApplicationCommandOption {

    /**
     * Gets the type of this option.
     *
     * @return The type.
     */
    ApplicationCommandOptionType getType();

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
     * Checks whether or not this option is required.
     *
     * @return Whether or not this option is required.
     */
    boolean isRequired();

    /**
     * Gets a list with all choices for this option.
     *
     * <p>If this option has any choices, they are the only valid values for a user to pick.
     *
     * @return A list with all choices for this option.
     */
    List<ApplicationCommandOptionChoice> getChoices();

    /**
     * If this option is a subcommand or subcommand group type, this nested options will be the parameters.
     *
     * @return A list with the nested options.
     */
    List<ApplicationCommandOption> getOptions();

    /**
     * Create a new application command option to be used with an application command builder.
     * This is a convenience method.
     *
     * @param type The type of the option.
     * @param name The name of the option.
     * @param description The description of the option.
     * @param required Whether this option is required.
     * @return The new application command option builder.
     */
    static ApplicationCommandOption create(ApplicationCommandOptionType type,
                                           String name,
                                           String description,
                                           boolean required) {
        return new ApplicationCommandOptionBuilder()
            .setType(type)
            .setName(name)
            .setDescription(description)
            .setRequired(required)
            .build();
    }

    /**
     * Create a new application command option to be used with an application command builder.
     * This is a convenience method.
     *
     * @param type The type of the option.
     * @param name The name of the option.
     * @param description The description of the option.
     * @return The new application command option builder.
     */
    static ApplicationCommandOption create(ApplicationCommandOptionType type, String name, String description) {
        return new ApplicationCommandOptionBuilder()
            .setType(type)
            .setName(name)
            .setDescription(description)
            .build();
    }

    /**
     * Create a new subcommand or subcommand group to be used with an application command builder.
     * This is a convenience method.
     *
     * @param type The type of the option. Must be either SUBCOMMAND or SUBCOMMAND_GROUP.
     * @param name The name of the option.
     * @param description The description of the option.
     * @param options The options of this subcommand or subcommand group.
     * @return The new application command option builder.
     */
    static ApplicationCommandOption createWithOptions(ApplicationCommandOptionType type,
                                                      String name,
                                                      String description,
                                                      ApplicationCommandOptionBuilder... options) {
        return createWithOptions(type, name, description,
            Arrays.stream(options)
                .map(ApplicationCommandOptionBuilder::build)
                .collect(Collectors.toList()));
    }

    /**
     * Create a new subcommand or subcommand group to be used with an application command builder.
     * This is a convenience method.
     *
     * @param type The type of the option. Must be either SUBCOMMAND or SUBCOMMAND_GROUP.
     * @param name The name of the option.
     * @param description The description of the option.
     * @param options The options of this subcommand or subcommand group.
     * @return The new application command option builder.
     */
    static ApplicationCommandOption createWithOptions(ApplicationCommandOptionType type,
                                                      String name,
                                                      String description,
                                                      List<ApplicationCommandOption> options) {
        return new ApplicationCommandOptionBuilder()
            .setType(type)
            .setName(name)
            .setDescription(description)
            .setOptions(options)
            .build();
    }

    /**
     * Create a new application command option to be used with an application command builder.
     * This is a convenience method.
     *
     * @param type The type of the option.
     * @param name The name of the option.
     * @param description The description of the option.
     * @param required Whether this option is required.
     * @param choices The choices of the option.
     * @return The new application command option builder.
     */
    static ApplicationCommandOption createWithChoices(ApplicationCommandOptionType type,
                                                      String name,
                                                      String description,
                                                      boolean required,
                                                      ApplicationCommandOptionChoiceBuilder... choices) {
        return createWithChoices(type, name, description, required, Arrays.stream(choices)
            .map(ApplicationCommandOptionChoiceBuilder::build)
            .collect(Collectors.toList()));
    }

    /**
     * Create a new application command option to be used with an application command builder.
     * This is a convenience method.
     *
     * @param type The type of the option.
     * @param name The name of the option.
     * @param description The description of the option.
     * @param required Whether this option is required.
     * @param choices The choices of the option.
     * @return The new application command option builder.
     */
    static ApplicationCommandOption createWithChoices(ApplicationCommandOptionType type,
                                                      String name,
                                                      String description,
                                                      boolean required,
                                                      List<ApplicationCommandOptionChoice> choices) {
        return new ApplicationCommandOptionBuilder()
            .setType(type)
            .setName(name)
            .setDescription(description)
            .setRequired(required)
            .setChoices(choices)
            .build();
    }
}

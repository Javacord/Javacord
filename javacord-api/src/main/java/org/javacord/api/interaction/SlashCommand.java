package org.javacord.api.interaction;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.server.Server;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public interface SlashCommand extends ApplicationCommand, Mentionable {

    /**
     * Gets the mention tag of this slash command with its base name.
     *
     * @return The mention tag of this slash command.
     */
    @Override
    default String getMentionTag() {
        return "</" + getName() + ":" + getId() + ">";
    }

    /**
     * Gets all mention tags of this slash command taking the groups and subcommands into account.
     *
     * @return All mention tags of this slash command.
     */
    default List<String> getMentionTags() {
        return getFullCommandNames().stream().map(name -> "</" + name + ":" + getId() + ">")
                .collect(Collectors.toList());
    }

    /**
     * Gets the full command names.
     *
     * @return All command names for this slash command.
     */
    List<String> getFullCommandNames();

    /**
     * Gets all options (i.e., parameters) for this command.
     *
     * @return All options (i.e., parameters) for this command.
     */
    List<SlashCommandOption> getOptions();

    /**
     * Create a new slash command builder with the given name and description.
     * Call {@link SlashCommandBuilder#createForServer(Server)} or
     * {@link SlashCommandBuilder#createGlobal(DiscordApi)} on the returned builder to submit to Discord.
     *
     * @param name        The name of the new slash command.
     * @param description The description of the new slash command.
     * @return The new slash command builder
     */
    static SlashCommandBuilder with(String name, String description) {
        return new SlashCommandBuilder()
                .setName(name)
                .setDescription(description);
    }

    /**
     * Create a new slash command builder with the given name, description and options.
     * Call {@link SlashCommandBuilder#createForServer(Server)} or
     * {@link SlashCommandBuilder#createGlobal(DiscordApi)} on the returned builder to submit to Discord.
     *
     * @param name        The name of the new slash command.
     * @param description The description of the new slash command.
     * @param options     The options to add to the command
     * @return The new slash command builder
     */
    static SlashCommandBuilder with(String name, String description, SlashCommandOptionBuilder... options) {
        return with(name, description, Arrays.stream(options)
                .map(SlashCommandOptionBuilder::build)
                .collect(Collectors.toList()));
    }

    /**
     * Create a new slash command builder with the given name, description and options.
     * Call {@link SlashCommandBuilder#createForServer(Server)} or
     * {@link SlashCommandBuilder#createGlobal(DiscordApi)} on the returned builder to submit to Discord.
     *
     * @param name        The name of the new slash command.
     * @param description The description of the new slash command.
     * @param options     The options to add to the command
     * @return The new slash command builder
     */
    static SlashCommandBuilder with(String name, String description, List<SlashCommandOption> options) {
        return with(name, description).setOptions(options);
    }

    /**
     * Create a new slash command builder with the given name and description.
     * Call {@link SlashCommandBuilder#createForServer(Server)} or
     * {@link SlashCommandBuilder#createGlobal(DiscordApi)} on the returned builder to submit to Discord.
     *
     * @param name                The name of the new slash command.
     * @param description         The description of the new slash command.
     * @param requiredPermissions The required permissions to be able to use this command.
     * @return The new slash command builder
     */
    static SlashCommandBuilder withRequiredPermissions(String name, String description,
                                                       PermissionType... requiredPermissions) {
        return new SlashCommandBuilder()
                .setName(name)
                .setDefaultEnabledForPermissions(requiredPermissions)
                .setDescription(description);
    }

    /**
     * Create a new slash command builder with the given name and description.
     * Call {@link SlashCommandBuilder#createForServer(Server)} or
     * {@link SlashCommandBuilder#createGlobal(DiscordApi)} on the returned builder to submit to Discord.
     *
     * @param name                The name of the new slash command.
     * @param description         The description of the new slash command.
     * @param requiredPermissions The required permissions to be able to use this command.
     * @return The new slash command builder
     */
    static SlashCommandBuilder withRequiredPermissions(String name, String description,
                                                       EnumSet<PermissionType> requiredPermissions) {
        return new SlashCommandBuilder()
                .setName(name)
                .setDefaultEnabledForPermissions(requiredPermissions)
                .setDescription(description);
    }


    /**
     * Create a new slash command builder with the given name, description and options.
     * Call {@link SlashCommandBuilder#createForServer(Server)} or
     * {@link SlashCommandBuilder#createGlobal(DiscordApi)} on the returned builder to submit to Discord.
     *
     * @param name                The name of the new slash command.
     * @param description         The description of the new slash command.
     * @param options             The options to add to the command
     * @param requiredPermissions The required permissions to be able to use this command.
     * @return The new slash command builder
     */
    static SlashCommandBuilder withRequiredPermissions(String name, String description,
                                                       List<SlashCommandOption> options,
                                                       PermissionType... requiredPermissions) {
        return with(name, description).setOptions(options).setDefaultEnabledForPermissions(requiredPermissions);
    }

    /**
     * Create a new slash command builder with the given name, description and options.
     * Call {@link SlashCommandBuilder#createForServer(Server)} or
     * {@link SlashCommandBuilder#createGlobal(DiscordApi)} on the returned builder to submit to Discord.
     *
     * @param name                The name of the new slash command.
     * @param description         The description of the new slash command.
     * @param options             The options to add to the command
     * @param requiredPermissions The required permissions to be able to use this command.
     * @return The new slash command builder
     */
    static SlashCommandBuilder withRequiredPermissions(String name, String description,
                                                       List<SlashCommandOption> options,
                                                       EnumSet<PermissionType> requiredPermissions) {
        return with(name, description).setOptions(options).setDefaultEnabledForPermissions(requiredPermissions);
    }

    /**
     * Create a new prefilled slash command builder from the given slash command.
     * Call {@link SlashCommandBuilder#createForServer(Server)} or
     * {@link SlashCommandBuilder#createGlobal(DiscordApi)} on the returned builder to submit to Discord.
     *
     * @param slashCommand The slash command which the slash command builder should be prefilled with.
     * @return The new prefilled slash command builder.
     */
    static SlashCommandBuilder createPrefilledSlashCommandBuilder(SlashCommand slashCommand) {
        return with(slashCommand.getName(), slashCommand.getDescription())
                .setOptions(slashCommand.getOptions());
    }

    /**
     * Creates a slash command updater from this SlashCommand instance.
     *
     * @return The slash command updater for this SlashCommand instance.
     */
    default SlashCommandUpdater createSlashCommandUpdater() {
        return new SlashCommandUpdater(this.getId());
    }
}

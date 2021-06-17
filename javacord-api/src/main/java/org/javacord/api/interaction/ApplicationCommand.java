package org.javacord.api.interaction;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.DiscordEntity;
import org.javacord.api.entity.server.Server;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public interface ApplicationCommand extends DiscordEntity {

    /**
     * Gets the unique id of this command.
     *
     * @return The unique id of this command.
     */
    long getId();

    /**
     * Gets the unique id of the application that this command belongs to.
     *
     * @return The unique application id.
     */
    long getApplicationId();

    /**
     * Gets the name of this command.
     *
     * @return The name of this command.
     */
    String getName();

    /**
     * Gets the description of this command.
     *
     * @return The description of this command.
     */
    String getDescription();

    /**
     * Gets a list with all options (i.e., parameters) for this command.
     *
     * @return A list with all options (i.e., parameters) for this command.
     */
    List<ApplicationCommandOption> getOptions();

    /**
     * Gets the default permission of this command.
     *
     * @return The default permission of this command.
     */
    boolean getDefaultPermission();

    /**
     * Deletes this application command globally.
     *
     * @return A future to check if the deletion was successful.
     */
    CompletableFuture<Void> deleteGlobal();

    /**
     * Deletes this application command globally.
     *
     * @param server The server where the command should be deleted from.
     * @return A future to check if the deletion was successful.
     */
    CompletableFuture<Void> deleteForServer(Server server);

    /**
     * Create a new application command builder with the given name and description.
     * Call {@link ApplicationCommandBuilder#createForServer(Server)} or
     * {@link ApplicationCommandBuilder#createGlobal(DiscordApi)} on the returned builder to submit to Discord.
     *
     * @param name The name of the new application command.
     * @param description The description of the new application command.
     * @return The new application command builder
     */
    static ApplicationCommandBuilder with(String name, String description) {
        return new ApplicationCommandBuilder()
                .setName(name)
                .setDescription(description);
    }

    /**
     * Create a new application command builder with the given name, description and options.
     * Call {@link ApplicationCommandBuilder#createForServer(Server)} or
     * {@link ApplicationCommandBuilder#createGlobal(DiscordApi)} on the returned builder to submit to Discord.
     *
     * @param name The name of the new application command.
     * @param description The description of the new application command.
     * @param options The options to add to the command
     * @return The new application command builder
     */
    static ApplicationCommandBuilder with(String name, String description, ApplicationCommandOptionBuilder... options) {
        return with(name, description, Arrays.stream(options)
                .map(ApplicationCommandOptionBuilder::build)
                .collect(Collectors.toList()));
    }

    /**
     * Create a new application command builder with the given name, description and options.
     * Call {@link ApplicationCommandBuilder#createForServer(Server)} or
     * {@link ApplicationCommandBuilder#createGlobal(DiscordApi)} on the returned builder to submit to Discord.
     *
     * @param name The name of the new application command.
     * @param description The description of the new application command.
     * @param options The options to add to the command
     * @return The new application command builder
     */
    static ApplicationCommandBuilder with(String name, String description, List<ApplicationCommandOption> options) {
        return with(name, description).setOptions(options);
    }

    /**
     * Create a new prefilled application command builder from the given application command.
     * Call {@link ApplicationCommandBuilder#createForServer(Server)} or
     * {@link ApplicationCommandBuilder#createGlobal(DiscordApi)} on the returned builder to submit to Discord.
     *
     * @param applicationCommand The application command which the application command builder should be prefilled with.
     * @return The new prefilled application command builder.
     */
    static ApplicationCommandBuilder createPrefilledApplicationCommandBuilder(ApplicationCommand applicationCommand) {
        return with(applicationCommand.getName(), applicationCommand.getDescription())
                .setOptions(applicationCommand.getOptions());
    }

    /**
     * Creates an application command updater from this ApplicationCommand instance.
     *
     * @return The application command updater for this ApplicationCommand instance.
     */
    default ApplicationCommandUpdater createApplicationCommandUpdater() {
        return new ApplicationCommandUpdater(this.getId());
    }
}

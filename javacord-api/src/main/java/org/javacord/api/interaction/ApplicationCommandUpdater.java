package org.javacord.api.interaction;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.internal.ApplicationCommandUpdaterDelegate;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public abstract class ApplicationCommandUpdater<T extends ApplicationCommand,
        D extends ApplicationCommandUpdaterDelegate<T>, B extends ApplicationCommandUpdater<T, D, B>> {

    private final D delegate;

    protected ApplicationCommandUpdater(D delegate) {
        this.delegate = delegate;
    }

    /**
     * Sets the new name of the application command.
     *
     * @param name The name to set.
     * @return The current instance in order to chain call methods.
     */
    public B setName(String name) {
        delegate.setName(name);
        return (B) this;
    }

    /**
     * Adds a name localization for the given locale.
     *
     * @param locale The locale to add a localization for.
     * @param localization The localization.
     * @return The current instance in order to chain call methods.
     */
    public B addNameLocalization(DiscordLocale locale, String localization) {
        delegate.addNameLocalization(locale, localization);
        return (B) this;
    }

    /**
     * Sets the new description of the slash command.
     *
     * @param description The description to set.
     * @return The current instance in order to chain call methods.
     */
    public B setDescription(String description) {
        delegate.setDescription(description);
        return (B) this;
    }

    /**
     * Adds a description localization for the given locale.
     *
     * @param locale The locale to add this localization for.
     * @param localization The command description localization.
     * @return The current instance in order to chain call methods.
     */
    public B addDescriptionLocalization(DiscordLocale locale, String localization) {
        delegate.addDescriptionLocalization(locale, localization);
        return (B) this;
    }

    /**
     * Sets the default required permissions to be able to use this command.
     * Passing no arguments will cause the command to be available admins only.
     * This can later be overridden by server admins.
     *
     * @param requiredPermissions The required permissions to use this command.
     * @return The current instance in order to chain call methods.
     */
    public B setDefaultEnabledForPermissions(PermissionType... requiredPermissions) {
        delegate.setDefaultEnabledForPermissions(EnumSet.copyOf(Arrays.asList(requiredPermissions)));
        return (B) this;
    }

    /**
     * Sets the default required permissions to be able to use this command.
     * Passing no arguments will cause the command to be available admins only.
     * This can later be overridden by server admins.
     *
     * @param requiredPermissions The required permissions to use this command.
     * @return The current instance in order to chain call methods.
     */
    public B setDefaultEnabledForPermissions(EnumSet<PermissionType> requiredPermissions) {
        delegate.setDefaultEnabledForPermissions(requiredPermissions);
        return (B) this;
    }

    /**
     * Enables this command for use by all users.
     * This can later be overridden by server admins.
     * @return The current instance in order to chain call methods.
     */
    public B setDefaultEnabledForEveryone() {
        delegate.setDefaultEnabledForEveryone();
        return (B) this;
    }

    /**
     * Sets whether this command should be disabled and only usable by server administrators by default.
     * This can later be overridden by server administrators.
     *
     * @return Whether this command is disabled by default.
     */
    public B setDefaultDisabled() {
        delegate.setDefaultDisabled();
        return (B) this;
    }

    /**
     * Sets whether this command is able to be used in DMs. By default, this is {@code true}
     * This has no effect on server commands.
     *
     * @param enabledInDms Whether the command is enabled in DMs
     * @return The current instance in order to chain call methods.
     */
    public B setEnabledInDms(boolean enabledInDms) {
        delegate.setEnabledInDms(enabledInDms);
        return (B) this;
    }

    /**
     * Updates a global application command.
     * When used to update multiple global application commands at once
     * {@link DiscordApi#bulkOverwriteGlobalApplicationCommands(Set)} should be used instead.
     *
     * @param api The DiscordApi instance.
     * @return The updated application command.
     */
    public CompletableFuture<T> updateGlobal(DiscordApi api) {
        return delegate.updateGlobal(api);
    }

    /**
     * Updates an application command on the given server.
     * When used to update multiple server application commands at once
     * {@link DiscordApi#bulkOverwriteServerApplicationCommands(Server, Set)} should be used instead.
     *
     * @param server The server where the command should be updated.
     * @return The updated application command.
     */
    public CompletableFuture<T> updateForServer(Server server) {
        return delegate.updateForServer(server.getApi(), server.getId());
    }

    /**
     * Updates an application command on the given server.
     * When used to update multiple server application commands at once
     * {@link DiscordApi#bulkOverwriteServerApplicationCommands(Server, Set)} should be used instead.
     *
     * @param api The DiscordApi instance.
     * @param server The server where the command should be updated.
     * @return The updated application command.
     */
    public CompletableFuture<T> updateForServer(DiscordApi api, long server) {
        return delegate.updateForServer(api, server);
    }

    /**
     * Gets the delegate used by the application command updater internally.
     *
     * @return The delegate used by this application command updater internally.
     */
    public D getDelegate() {
        return delegate;
    }
}

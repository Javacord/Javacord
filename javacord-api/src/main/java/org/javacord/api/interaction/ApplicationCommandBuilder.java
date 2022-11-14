package org.javacord.api.interaction;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.internal.ApplicationCommandBuilderDelegate;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public abstract class ApplicationCommandBuilder<R extends ApplicationCommand,
        D extends ApplicationCommandBuilderDelegate<R>, T extends ApplicationCommandBuilder<R, D, T>> {

    private final D delegate;

    protected ApplicationCommandBuilder(D delegate) {
        this.delegate = delegate;
    }

    /**
     * Sets the name of the message context menu.
     *
     * @param name The name.
     * @return The current instance in order to chain call methods.
     */
    public T setName(String name) {
        delegate.setName(name);
        return (T) this;
    }

    /**
     * Adds a name localization for the given locale.
     *
     * @param locale The locale to add this localization for.
     * @param localization The command name localization.
     * @return The current instance in order to chain call methods.
     */
    public T addNameLocalization(DiscordLocale locale, String localization) {
        delegate.addNameLocalization(locale, localization);
        return (T) this;
    }

    /**
     * Sets the description of the slash command.
     *
     * @param description The description.
     * @return The current instance in order to chain call methods.
     */
    public T setDescription(String description) {
        delegate.setDescription(description);
        return (T) this;
    }

    /**
     * Adds a description localization for the given locale.
     *
     * @param locale The locale to add a localization for.
     * @param localization The command description localization.
     * @return The current instance in order to chain call methods.
     */
    public T addDescriptionLocalization(DiscordLocale locale, String localization) {
        delegate.addDescriptionLocalization(locale, localization);
        return (T) this;
    }

    /**
     * Sets the default required permissions to be able to use this command.
     * Passing no arguments will cause the command to be available to admins only.
     * This can later be overridden by server admins.
     *
     * @param requiredPermissions The required permissions to use this command.
     * @return The current instance in order to chain call methods.
     */
    public T setDefaultEnabledForPermissions(PermissionType... requiredPermissions) {
        delegate.setDefaultEnabledForPermissions(EnumSet.copyOf(Arrays.asList(requiredPermissions)));
        return (T) this;
    }

    /**
     * Sets the default required permissions to be able to use this command.
     * Passing no arguments will cause the command to be available to admins only.
     * This can later be overridden by server admins.
     *
     * @param requiredPermissions The required permissions to use this command.
     * @return The current instance in order to chain call methods.
     */
    public T setDefaultEnabledForPermissions(EnumSet<PermissionType> requiredPermissions) {
        delegate.setDefaultEnabledForPermissions(requiredPermissions);
        return (T) this;
    }

    /**
     * Enables this command for use by all users.
     * This can later be overridden by server admins.
     * @return The current instance in order to chain call methods.
     */
    public T setDefaultEnabledForEveryone() {
        delegate.setDefaultEnabledForEveryone();
        return (T) this;
    }

    /**
     * Sets whether this command should be disabled and only usable by server administrators by default.
     * This can later be overridden by server administrators.
     *
     * @return Whether this command is disabled by default.
     */
    public T setDefaultDisabled() {
        delegate.setDefaultDisabled();
        return (T) this;
    }

    /**
     * Sets whether this command is able to be used in DMs. By default, this is {@code true}
     * This has no effect on server commands.
     *
     * @param enabledInDms Whether the command is enabled in DMs
     * @return The current instance in order to chain call methods.
     */
    public T setEnabledInDms(boolean enabledInDms) {
        delegate.setEnabledInDms(enabledInDms);
        return (T) this;
    }

    /**
     * Sets the slash command as nsfw.
     *
     * @param nsfw Whether the command is nsfw.
     * @return The current instance in order to chain call methods.
     */
    public T setNsfw(boolean nsfw) {
        delegate.setNsfw(nsfw);
        return (T) this;
    }

    /**
     * Gets the delegate used by the application command builder internally.
     *
     * @return The delegate used by this application command builder internally.
     */
    public D getDelegate() {
        return delegate;
    }

    /**
     * Creates a global application command.
     * When used to update multiple global slash commands at once
     * {@link DiscordApi#bulkOverwriteGlobalApplicationCommands(Set)} should be used instead.
     *
     * @param api The discord api instance.
     * @return The built application command.
     */
    public CompletableFuture<R> createGlobal(DiscordApi api) {
        return delegate.createGlobal(api);
    }

    /**
     * Creates an application command for a specific server.
     * When used to create multiple server application commands at once
     * {@link DiscordApi#bulkOverwriteServerApplicationCommands(Server, Set)} should be used instead.
     *
     * @param server The server.
     * @return The built application command.
     */
    public CompletableFuture<R> createForServer(Server server) {
        return delegate.createForServer(server.getApi(), server.getId());
    }


    /**
     * Creates an application command for a specific server.
     * When used to create multiple server application commands at once
     * {@link DiscordApi#bulkOverwriteServerApplicationCommands(Server, Set)} should be used instead.
     *
     * @param api The discord api instance.
     * @param server The server.
     * @return The built application command.
     */
    public CompletableFuture<R> createForServer(DiscordApi api, long server) {
        return delegate.createForServer(api, server);
    }

}

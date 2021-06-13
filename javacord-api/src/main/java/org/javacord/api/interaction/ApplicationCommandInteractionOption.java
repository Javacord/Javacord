package org.javacord.api.interaction;

import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface ApplicationCommandInteractionOption extends ApplicationCommandInteractionOptionsProvider {

    /**
     * Gets the name of the option.
     *
     * @return The name.
     */
    String getName();

    /**
     * Checks if the option is a subcommand or group.
     *
     * <p>If the option is a subcommand or group, it does have options but no value.
     * If the option is not a subcommand or group, it does have a value but no options.
     *
     * @return If the option is a subcommand or group.
     */
    default boolean isSubcommandOrGroup() {
        return !getStringValue().isPresent();
    }

    /**
     * Gets the string representation of this option value.
     *
     * <p>If this option does not have a string value or the option itself is a subcommand or group,
     * the optional will be empty.
     *
     * @return The string value of this option.
     */
    Optional<String> getStringValue();

    /**
     * Gets the integer value of this option.
     *
     * <p>If this option does not have an integer value or the option itself is a subcommand or group,
     *     the optional will be empty.
     *
     * @return The integer value of this option.
     */
    Optional<Integer> getIntValue();

    /**
     * Gets the boolean value of this option.
     *
     * <p>If this option does not have a boolean value or the option itself is a subcommand or group,
     *     the optional will be empty.
     *
     * @return The boolean value of this option.
     */
    Optional<Boolean> getBooleanValue();

    /**
     * Gets the user value of this option.
     * Note: This method only respects cached users. To fetch the user from Discord if the user is not cached,
     *     use {@link ApplicationCommandInteractionOption#requestUserValue()}.
     *
     * <p>If this option does not have a user value or the option itself is a subcommand or group,
     *     the optional will be empty.
     *
     * @return The user value of this option.
     */
    Optional<User> getUserValue();

    /**
     * Gets the user value of this option.
     *
     * <p>If this option does not have a user value or the option itself is a subcommand or group,
     *     the optional will be empty.
     *
     * @return The user value of this option.
     */
    Optional<CompletableFuture<User>> requestUserValue();

    /**
     * Gets the channel value of this option.
     *
     * <p>If this option does not have a channel value or the option itself is a subcommand or group,
     *     the optional will be empty.
     *
     * @return The channel value of this option.
     */
    Optional<ServerChannel> getChannelValue();

    /**
     * Gets the role value of this option.
     *
     * <p>If this option does not have a role value or the option itself is a subcommand or group,
     *     the optional will be empty.
     *
     * @return The role value of this option.
     */
    Optional<Role> getRoleValue();

    /**
     * Gets the mentionable value of this option.
     * Note: This method only respects cached users if the ID of the Mentionable belongs to a user. To fetch the user
     *     from Discord if the user is not cached,
     *     use {@link ApplicationCommandInteractionOption#requestMentionableValue()}.
     *
     * <p>If this option does not have a mentionable value or the option itself is a subcommand or group,
     *     the optional will be empty.
     *
     * @return The mentionable value of this option.
     */
    Optional<Mentionable> getMentionableValue();

    /**
     * Gets the mentionable value of this option.
     *
     * <p>If this option does not have a mentionable value or the option itself is a subcommand or group,
     *     the optional will be empty.
     *
     * @return The mentionable value of this option.
     */
    Optional<CompletableFuture<Mentionable>> requestMentionableValue();

    /**
     * Gets a list with all options of this option, if this option denotes a subcommand or group.
     *
     * <p>If this option does not denote a subcommand or group, the list will be empty.
     *
     * @return A list with all options.
     */
    @Override // due to different JavaDoc; signature identical
    List<ApplicationCommandInteractionOption> getOptions();
}

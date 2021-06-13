package org.javacord.api.interaction;

import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Internal interface that offers a few option related convenience methods.
 * Outsourced to prevent code duplication.
 */
public interface ApplicationCommandInteractionOptionsProvider {
    /**
     * Gets a list with the params and values from the user, or, if this command contains subcommands and/or
     * subcommand groups, a list of subcommands and subcommand groups.
     *
     * @return A list with the params and values from the user.
     */
    List<ApplicationCommandInteractionOption> getOptions();

    /**
     * Get the first option, if present. Useful if you're working with a command that has only one option.
     *
     * @return The option at index 0, if present; an empty Optional otherwise
     */
    default Optional<ApplicationCommandInteractionOption> getFirstOption() {
        return getOptions().stream().findFirst();
    }

    /**
     * Gets the string representation of the first option (if present).
     *
     * @return An Optional with the string value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<String> getFirstOptionStringValue() {
        return getFirstOption().flatMap(ApplicationCommandInteractionOption::getStringValue);
    }

    /**
     * Gets the integer value of the first option (if present).
     *
     * @return An Optional with the integer value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Integer> getFirstOptionIntValue() {
        return getFirstOption().flatMap(ApplicationCommandInteractionOption::getIntValue);
    }

    /**
     * Gets the boolean value of the first option (if present).
     *
     * @return An Optional with the boolean value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Boolean> getFirstOptionBooleanValue() {
        return getFirstOption().flatMap(ApplicationCommandInteractionOption::getBooleanValue);
    }

    /**
     * Gets the user value of the first option (if present).
     * Note: This method only respects cached users. To fetch the user from Discord if the user is not cached,
     *     use {@code requestFirstOptionUserValue()}.
     *
     * @return An Optional with the user value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<User> getFirstOptionUserValue() {
        return getFirstOption().flatMap(ApplicationCommandInteractionOption::getUserValue);
    }

    /**
     * Gets the user value of the first option (if present).
     *
     * @return An Optional with the user value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<CompletableFuture<User>> requestFirstOptionUserValue() {
        return getFirstOption().flatMap(ApplicationCommandInteractionOption::requestUserValue);
    }

    /**
     * Gets the channel value of the first option (if present).
     *
     * @return An Optional with the channel value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<ServerChannel> getFirstOptionChannelValue() {
        return getFirstOption().flatMap(ApplicationCommandInteractionOption::getChannelValue);
    }

    /**
     * Gets the role value of the first option (if present).
     *
     * @return An Optional with the role value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Role> getFirstOptionRoleValue() {
        return getFirstOption().flatMap(ApplicationCommandInteractionOption::getRoleValue);
    }

    /**
     * Gets the mentionable value of the first option (if present) which may be a user, channel or role.
     * Note: This method only respects cached users if the ID of the Mentionable belongs to a user. To fetch the user
     *     from Discord if the user is not cached,
     *     use {@code requestFirstOptionMentionableValue()}.
     *
     * @return An Optional with the mentionable value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Mentionable> getFirstOptionMentionableValue() {
        return getFirstOption().flatMap(ApplicationCommandInteractionOption::getMentionableValue);
    }

    /**
     * Gets the mentionable value of the first option (if present) which may be a user, channel or role.
     *
     * @return An Optional with the mentionable value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<CompletableFuture<Mentionable>> requestFirstOptionMentionableValue() {
        return getFirstOption().flatMap(ApplicationCommandInteractionOption::requestFirstOptionMentionableValue);
    }

    /**
     * Get the second option, if present. Useful if you're working with a command that has two options.
     *
     * @return The option at index 0, if present; an empty Optional otherwise
     */
    default Optional<ApplicationCommandInteractionOption> getSecondOption() {
        return getOptionByIndex(1);
    }

    /**
     * Gets the string representation of the second option (if present).
     *
     * @return An Optional with the string value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<String> getSecondOptionStringValue() {
        return getSecondOption().flatMap(ApplicationCommandInteractionOption::getStringValue);
    }

    /**
     * Gets the integer value of the second option (if present).
     *
     * @return An Optional with the integer value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Integer> getSecondOptionIntValue() {
        return getSecondOption().flatMap(ApplicationCommandInteractionOption::getIntValue);
    }

    /**
     * Gets the boolean value of the second option (if present).
     *
     * @return An Optional with the boolean value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Boolean> getSecondOptionBooleanValue() {
        return getSecondOption().flatMap(ApplicationCommandInteractionOption::getBooleanValue);
    }

    /**
     * Gets the user value of the second option (if present).
     * Note: This method only respects cached users. To fetch the user from Discord if the user is not cached,
     *     use {@code requestSecondOptionUserValue()}.
     *
     * @return An Optional with the user value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<User> getSecondOptionUserValue() {
        return getSecondOption().flatMap(ApplicationCommandInteractionOption::getUserValue);
    }

    /**
     * Gets the user value of the second option (if present).
     *
     * @return An Optional with the user value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<CompletableFuture<User>> requestSecondOptionUserValue() {
        return getSecondOption().flatMap(ApplicationCommandInteractionOption::requestUserValue);
    }

    /**
     * Gets the channel value of the second option (if present).
     *
     * @return An Optional with the channel value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<ServerChannel> getSecondOptionChannelValue() {
        return getSecondOption().flatMap(ApplicationCommandInteractionOption::getChannelValue);
    }

    /**
     * Gets the role value of the second option (if present).
     *
     * @return An Optional with the role value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Role> getSecondOptionRoleValue() {
        return getSecondOption().flatMap(ApplicationCommandInteractionOption::getRoleValue);
    }

    /**
     * Gets the mentionable value of the second option (if present) which may be a user, channel or role.
     * Note: This method only respects cached users if the ID of the Mentionable belongs to a user. To fetch the user
     *     from Discord if the user is not cached,
     *     use {@code requestSecondOptionMentionableValue()}.
     *
     * @return An Optional with the mentionable value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Mentionable> getSecondOptionMentionableValue() {
        return getSecondOption().flatMap(ApplicationCommandInteractionOption::getMentionableValue);
    }

    /**
     * Gets the mentionable value of the second option (if present) which may be a user, channel or role.
     *
     * @return An Optional with the mentionable value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<CompletableFuture<Mentionable>> requestSecondOptionMentionableValue() {
        return getSecondOption().flatMap(ApplicationCommandInteractionOption::requestSecondOptionMentionableValue);
    }

    /**
     * Get the third option, if present. Useful if you're working with a command that has up to 3 options.
     *
     * @return The option at index 0, if present; an empty Optional otherwise
     */
    default Optional<ApplicationCommandInteractionOption> getThirdOption() {
        return getOptionByIndex(2);
    }

    /**
     * Gets the string representation of the third option (if present).
     *
     * @return An Optional with the string value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<String> getThirdOptionStringValue() {
        return getThirdOption().flatMap(ApplicationCommandInteractionOption::getStringValue);
    }

    /**
     * Gets the integer value of the third option (if present).
     *
     * @return An Optional with the integer value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Integer> getThirdOptionIntValue() {
        return getThirdOption().flatMap(ApplicationCommandInteractionOption::getIntValue);
    }

    /**
     * Gets the boolean value of the third option (if present).
     *
     * @return An Optional with the boolean value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Boolean> getThirdOptionBooleanValue() {
        return getThirdOption().flatMap(ApplicationCommandInteractionOption::getBooleanValue);
    }

    /**
     * Gets the user value of the third option (if present).
     * Note: This method only respects cached users. To fetch the user from Discord if the user is not cached,
     *     use {@code requestThirdOptionUserValue()}.
     *
     * @return An Optional with the user value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<User> getThirdOptionUserValue() {
        return getThirdOption().flatMap(ApplicationCommandInteractionOption::getUserValue);
    }

    /**
     * Gets the user value of the third option (if present).
     *
     * @return An Optional with the user value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<CompletableFuture<User>> requestThirdOptionUserValue() {
        return getThirdOption().flatMap(ApplicationCommandInteractionOption::requestUserValue);
    }

    /**
     * Gets the channel value of the third option (if present).
     *
     * @return An Optional with the channel value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<ServerChannel> getThirdOptionChannelValue() {
        return getThirdOption().flatMap(ApplicationCommandInteractionOption::getChannelValue);
    }

    /**
     * Gets the role value of the third option (if present).
     *
     * @return An Optional with the role value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Role> getThirdOptionRoleValue() {
        return getThirdOption().flatMap(ApplicationCommandInteractionOption::getRoleValue);
    }

    /**
     * Gets the mentionable value of the third option (if present) which may be a user, channel or role.
     * Note: This method only respects cached users if the ID of the Mentionable belongs to a user. To fetch the user
     *     from Discord if the user is not cached,
     *     use {@code requestThirdOptionMentionableValue()}.
     *
     * @return An Optional with the mentionable value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Mentionable> getThirdOptionMentionableValue() {
        return getThirdOption().flatMap(ApplicationCommandInteractionOption::getMentionableValue);
    }

    /**
     * Gets the mentionable value of the third option (if present) which may be a user, channel or role.
     *
     * @return An Optional with the mentionable value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<CompletableFuture<Mentionable>> requestThirdOptionMentionableValue() {
        return getThirdOption().flatMap(ApplicationCommandInteractionOption::requestThirdOptionMentionableValue);
    }

    /**
     * Get an option having the specified name.
     *
     * @param name The name of the option to search for.
     * @return The option with the specified name, if found; an empty Optional otherwise
     */
    default Optional<ApplicationCommandInteractionOption> getOptionByName(String name) {
        return getOptions()
                .stream()
                .filter(option -> option.getName().equalsIgnoreCase(name))
                .findAny();
    }

    /**
     * Gets the string value of an option having the specified name.
     *
     * @param name The name of the option to search for.
     * @return An Optional with the string value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<String> getOptionStringValueByName(String name) {
        return getOptionByName(name).flatMap(ApplicationCommandInteractionOption::getStringValue);
    }

    /**
     * Gets the integer value of an option having the specified name.
     *
     * @param name The name of the option to search for.
     * @return An Optional with the integer value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Integer> getOptionIntValueByName(String name) {
        return getOptionByName(name).flatMap(ApplicationCommandInteractionOption::getIntValue);
    }

    /**
     * Gets the boolean value of an option having the specified name.
     *
     * @param name The name of the option to search for.
     * @return An Optional with the boolean value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Boolean> getOptionBooleanValueByName(String name) {
        return getOptionByName(name).flatMap(ApplicationCommandInteractionOption::getBooleanValue);
    }

    /**
     * Gets the user value of an option having the specified name.
     * Note: This method only respects cached users. To fetch the user from Discord if the user is not cached,
     *     use {@code requestOptionUserValueByName()}.
     *
     * @param name The name of the option to search for.
     * @return An Optional with the user value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<User> getOptionUserValueByName(String name) {
        return getOptionByName(name).flatMap(ApplicationCommandInteractionOption::getUserValue);
    }

    /**
     * Gets the user value of an option having the specified name.
     *
     * @param name The name of the option to search for.
     * @return An Optional with the user value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<CompletableFuture<User>> requestOptionUserValueByName(String name) {
        return getOptionByName(name).flatMap(ApplicationCommandInteractionOption::requestUserValue);
    }

    /**
     * Gets the channel value of an option having the specified name.
     *
     * @param name The name of the option to search for.
     * @return An Optional with the channel value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<ServerChannel> getOptionChannelValueByName(String name) {
        return getOptionByName(name).flatMap(ApplicationCommandInteractionOption::getChannelValue);
    }

    /**
     * Gets the role value of an option having the specified name.
     *
     * @param name The name of the option to search for.
     * @return An Optional with the role value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Role> getOptionRoleValueByName(String name) {
        return getOptionByName(name).flatMap(ApplicationCommandInteractionOption::getRoleValue);
    }

    /**
     * Gets the mentionable value of an option having the specified name which may be a user, channel or role.
     * Note: This method only respects cached users if the ID of the Mentionable belongs to a user. To fetch the user
     *     from Discord if the user is not cached,
     *     use {@code requestOptionMentionableValueByName()}.
     *
     * @param name The name of the option to search for.
     * @return An Optional with the mentionable value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Mentionable> getOptionMentionableValueByName(String name) {
        return getOptionByName(name).flatMap(ApplicationCommandInteractionOption::getMentionableValue);
    }

    /**
     * Gets the mentionable value of an option having the specified name which may be a user, channel or role.
     *
     * @param name The name of the option to search for.
     * @return An Optional with the mentionable value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<CompletableFuture<Mentionable>> requestOptionMentionableValueByName(String name) {
        return getOptionByName(name).flatMap(ApplicationCommandInteractionOption::requestFirstOptionMentionableValue);
    }

    /**
     * Gets the option at the specified index, if present.
     *
     * @param index The index of the option to search for.
     * @return The option with the specified index, if found; an empty Optional otherwise
     */
    default Optional<ApplicationCommandInteractionOption> getOptionByIndex(int index) {
        return getOptions()
                .stream()
                .skip(index)
                .findFirst();
    }

    /**
     * Gets the string value of an option at the specified index.
     *
     * @param index The index of the option to search for.
     * @return An Optional with the string value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<String> getOptionStringValueByIndex(int index) {
        return getOptionByIndex(index).flatMap(ApplicationCommandInteractionOption::getStringValue);
    }

    /**
     * Gets the integer value of an option at the specified index.
     *
     * @param index The index of the option to search for.
     * @return An Optional with the integer value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Integer> getOptionIntValueByIndex(int index) {
        return getOptionByIndex(index).flatMap(ApplicationCommandInteractionOption::getIntValue);
    }

    /**
     * Gets the boolean value of an option at the specified index.
     *
     * @param index The index of the option to search for.
     * @return An Optional with the boolean value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Boolean> getOptionBooleanValueByIndex(int index) {
        return getOptionByIndex(index).flatMap(ApplicationCommandInteractionOption::getBooleanValue);
    }

    /**
     * Gets the user value of an option at the specified index.
     * Note: This method only respects cached users. To fetch the user from Discord if the user is not cached,
     *     use {@code requestOptionUserValueByIndex()}.
     *
     * @param index The index of the option to search for.
     * @return An Optional with the user value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<User> getOptionUserValueByIndex(int index) {
        return getOptionByIndex(index).flatMap(ApplicationCommandInteractionOption::getUserValue);
    }

    /**
     * Gets the user value of an option at the specified index.
     *
     * @param index The index of the option to search for.
     * @return An Optional with the user value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<CompletableFuture<User>> requestOptionUserValueByIndex(int index) {
        return getOptionByIndex(index).flatMap(ApplicationCommandInteractionOption::requestUserValue);
    }

    /**
     * Gets the channel value of an option at the specified index.
     *
     * @param index The index of the option to search for.
     * @return An Optional with the channel value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<ServerChannel> getOptionChannelValueByIndex(int index) {
        return getOptionByIndex(index).flatMap(ApplicationCommandInteractionOption::getChannelValue);
    }

    /**
     * Gets the role value of an option at the specified index.
     *
     * @param index The index of the option to search for.
     * @return An Optional with the role value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Role> getOptionRoleValueByIndex(int index) {
        return getOptionByIndex(index).flatMap(ApplicationCommandInteractionOption::getRoleValue);
    }

    /**
     * Gets the mentionable value of an option at the specified index which may be a user, channel or role.
     * Note: This method only respects cached users if the ID of the Mentionable belongs to a user. To fetch the user
     *     from Discord if the user is not cached,
     *     use {@code requestOptionMentionableValueByIndex()}.
     *
     * @param index The index of the option to search for.
     * @return An Optional with the mentionable value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Mentionable> getOptionMentionableValueByIndex(int index) {
        return getOptionByIndex(index).flatMap(ApplicationCommandInteractionOption::getMentionableValue);
    }

    /**
     * Gets the mentionable value of an option at the specified index which may be a user, channel or role.
     *
     * @param index The index of the option to search for.
     * @return An Optional with the mentionable value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<CompletableFuture<Mentionable>> requestOptionMentionableValueByIndex(int index) {
        return getOptionByIndex(index).flatMap(ApplicationCommandInteractionOption::requestFirstOptionMentionableValue);
    }
}

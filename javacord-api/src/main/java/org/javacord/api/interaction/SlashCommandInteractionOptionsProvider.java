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
public interface SlashCommandInteractionOptionsProvider {
    /**
     * Gets a list with the params and values from the user, or, if this command contains subcommands and/or
     * subcommand groups, a list of subcommands and subcommand groups.
     *
     * @return A list with the params and values from the user.
     */
    List<SlashCommandInteractionOption> getOptions();

    /**
     * Get the first option, if present. Useful if you're working with a command that has only one option.
     *
     * @return The option at index 0, if present; an empty Optional otherwise
     */
    default Optional<SlashCommandInteractionOption> getFirstOption() {
        return getOptions().stream().findFirst();
    }

    /**
     * Gets the string representation of the first option (if present).
     *
     * @return An Optional with the string value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<String> getFirstOptionStringValue() {
        return getFirstOption().flatMap(SlashCommandInteractionOption::getStringValue);
    }

    /**
     * Gets the integer value of the first option (if present).
     *
     * @return An Optional with the integer value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Integer> getFirstOptionIntValue() {
        return getFirstOption().flatMap(SlashCommandInteractionOption::getIntValue);
    }

    /**
     * Gets the boolean value of the first option (if present).
     *
     * @return An Optional with the boolean value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Boolean> getFirstOptionBooleanValue() {
        return getFirstOption().flatMap(SlashCommandInteractionOption::getBooleanValue);
    }

    /**
     * Gets the user value of the first option (if present).
     * Note: This method only respects cached users. To fetch the user from Discord if the user is not cached,
     *     use {@code requestFirstOptionUserValue()}.
     *
     * @return An Optional with the user value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<User> getFirstOptionUserValue() {
        return getFirstOption().flatMap(SlashCommandInteractionOption::getUserValue);
    }

    /**
     * Gets the user value of the first option (if present).
     *
     * @return An Optional with the user value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<CompletableFuture<User>> requestFirstOptionUserValue() {
        return getFirstOption().flatMap(SlashCommandInteractionOption::requestUserValue);
    }

    /**
     * Gets the channel value of the first option (if present).
     *
     * @return An Optional with the channel value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<ServerChannel> getFirstOptionChannelValue() {
        return getFirstOption().flatMap(SlashCommandInteractionOption::getChannelValue);
    }

    /**
     * Gets the role value of the first option (if present).
     *
     * @return An Optional with the role value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Role> getFirstOptionRoleValue() {
        return getFirstOption().flatMap(SlashCommandInteractionOption::getRoleValue);
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
        return getFirstOption().flatMap(SlashCommandInteractionOption::getMentionableValue);
    }

    /**
     * Gets the mentionable value of the first option (if present) which may be a user, channel or role.
     *
     * @return An Optional with the mentionable value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<CompletableFuture<Mentionable>> requestFirstOptionMentionableValue() {
        return getFirstOption().flatMap(SlashCommandInteractionOption::requestFirstOptionMentionableValue);
    }

    /**
     * Get the second option, if present. Useful if you're working with a command that has two options.
     *
     * @return The option at index 0, if present; an empty Optional otherwise
     */
    default Optional<SlashCommandInteractionOption> getSecondOption() {
        return getOptionByIndex(1);
    }

    /**
     * Gets the string representation of the second option (if present).
     *
     * @return An Optional with the string value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<String> getSecondOptionStringValue() {
        return getSecondOption().flatMap(SlashCommandInteractionOption::getStringValue);
    }

    /**
     * Gets the integer value of the second option (if present).
     *
     * @return An Optional with the integer value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Integer> getSecondOptionIntValue() {
        return getSecondOption().flatMap(SlashCommandInteractionOption::getIntValue);
    }

    /**
     * Gets the boolean value of the second option (if present).
     *
     * @return An Optional with the boolean value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Boolean> getSecondOptionBooleanValue() {
        return getSecondOption().flatMap(SlashCommandInteractionOption::getBooleanValue);
    }

    /**
     * Gets the user value of the second option (if present).
     * Note: This method only respects cached users. To fetch the user from Discord if the user is not cached,
     *     use {@code requestSecondOptionUserValue()}.
     *
     * @return An Optional with the user value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<User> getSecondOptionUserValue() {
        return getSecondOption().flatMap(SlashCommandInteractionOption::getUserValue);
    }

    /**
     * Gets the user value of the second option (if present).
     *
     * @return An Optional with the user value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<CompletableFuture<User>> requestSecondOptionUserValue() {
        return getSecondOption().flatMap(SlashCommandInteractionOption::requestUserValue);
    }

    /**
     * Gets the channel value of the second option (if present).
     *
     * @return An Optional with the channel value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<ServerChannel> getSecondOptionChannelValue() {
        return getSecondOption().flatMap(SlashCommandInteractionOption::getChannelValue);
    }

    /**
     * Gets the role value of the second option (if present).
     *
     * @return An Optional with the role value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Role> getSecondOptionRoleValue() {
        return getSecondOption().flatMap(SlashCommandInteractionOption::getRoleValue);
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
        return getSecondOption().flatMap(SlashCommandInteractionOption::getMentionableValue);
    }

    /**
     * Gets the mentionable value of the second option (if present) which may be a user, channel or role.
     *
     * @return An Optional with the mentionable value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<CompletableFuture<Mentionable>> requestSecondOptionMentionableValue() {
        return getSecondOption().flatMap(SlashCommandInteractionOption::requestSecondOptionMentionableValue);
    }

    /**
     * Get the third option, if present. Useful if you're working with a command that has up to 3 options.
     *
     * @return The option at index 0, if present; an empty Optional otherwise
     */
    default Optional<SlashCommandInteractionOption> getThirdOption() {
        return getOptionByIndex(2);
    }

    /**
     * Gets the string representation of the third option (if present).
     *
     * @return An Optional with the string value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<String> getThirdOptionStringValue() {
        return getThirdOption().flatMap(SlashCommandInteractionOption::getStringValue);
    }

    /**
     * Gets the integer value of the third option (if present).
     *
     * @return An Optional with the integer value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Integer> getThirdOptionIntValue() {
        return getThirdOption().flatMap(SlashCommandInteractionOption::getIntValue);
    }

    /**
     * Gets the boolean value of the third option (if present).
     *
     * @return An Optional with the boolean value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Boolean> getThirdOptionBooleanValue() {
        return getThirdOption().flatMap(SlashCommandInteractionOption::getBooleanValue);
    }

    /**
     * Gets the user value of the third option (if present).
     * Note: This method only respects cached users. To fetch the user from Discord if the user is not cached,
     *     use {@code requestThirdOptionUserValue()}.
     *
     * @return An Optional with the user value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<User> getThirdOptionUserValue() {
        return getThirdOption().flatMap(SlashCommandInteractionOption::getUserValue);
    }

    /**
     * Gets the user value of the third option (if present).
     *
     * @return An Optional with the user value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<CompletableFuture<User>> requestThirdOptionUserValue() {
        return getThirdOption().flatMap(SlashCommandInteractionOption::requestUserValue);
    }

    /**
     * Gets the channel value of the third option (if present).
     *
     * @return An Optional with the channel value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<ServerChannel> getThirdOptionChannelValue() {
        return getThirdOption().flatMap(SlashCommandInteractionOption::getChannelValue);
    }

    /**
     * Gets the role value of the third option (if present).
     *
     * @return An Optional with the role value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Role> getThirdOptionRoleValue() {
        return getThirdOption().flatMap(SlashCommandInteractionOption::getRoleValue);
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
        return getThirdOption().flatMap(SlashCommandInteractionOption::getMentionableValue);
    }

    /**
     * Gets the mentionable value of the third option (if present) which may be a user, channel or role.
     *
     * @return An Optional with the mentionable value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<CompletableFuture<Mentionable>> requestThirdOptionMentionableValue() {
        return getThirdOption().flatMap(SlashCommandInteractionOption::requestThirdOptionMentionableValue);
    }

    /**
     * Get an option having the specified name.
     *
     * @param name The name of the option to search for.
     * @return The option with the specified name, if found; an empty Optional otherwise
     */
    default Optional<SlashCommandInteractionOption> getOptionByName(String name) {
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
        return getOptionByName(name).flatMap(SlashCommandInteractionOption::getStringValue);
    }

    /**
     * Gets the integer value of an option having the specified name.
     *
     * @param name The name of the option to search for.
     * @return An Optional with the integer value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Integer> getOptionIntValueByName(String name) {
        return getOptionByName(name).flatMap(SlashCommandInteractionOption::getIntValue);
    }

    /**
     * Gets the boolean value of an option having the specified name.
     *
     * @param name The name of the option to search for.
     * @return An Optional with the boolean value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Boolean> getOptionBooleanValueByName(String name) {
        return getOptionByName(name).flatMap(SlashCommandInteractionOption::getBooleanValue);
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
        return getOptionByName(name).flatMap(SlashCommandInteractionOption::getUserValue);
    }

    /**
     * Gets the user value of an option having the specified name.
     *
     * @param name The name of the option to search for.
     * @return An Optional with the user value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<CompletableFuture<User>> requestOptionUserValueByName(String name) {
        return getOptionByName(name).flatMap(SlashCommandInteractionOption::requestUserValue);
    }

    /**
     * Gets the channel value of an option having the specified name.
     *
     * @param name The name of the option to search for.
     * @return An Optional with the channel value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<ServerChannel> getOptionChannelValueByName(String name) {
        return getOptionByName(name).flatMap(SlashCommandInteractionOption::getChannelValue);
    }

    /**
     * Gets the role value of an option having the specified name.
     *
     * @param name The name of the option to search for.
     * @return An Optional with the role value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Role> getOptionRoleValueByName(String name) {
        return getOptionByName(name).flatMap(SlashCommandInteractionOption::getRoleValue);
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
        return getOptionByName(name).flatMap(SlashCommandInteractionOption::getMentionableValue);
    }

    /**
     * Gets the mentionable value of an option having the specified name which may be a user, channel or role.
     *
     * @param name The name of the option to search for.
     * @return An Optional with the mentionable value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<CompletableFuture<Mentionable>> requestOptionMentionableValueByName(String name) {
        return getOptionByName(name).flatMap(SlashCommandInteractionOption::requestFirstOptionMentionableValue);
    }

    /**
     * Gets the option at the specified index, if present.
     *
     * @param index The index of the option to search for.
     * @return The option with the specified index, if found; an empty Optional otherwise
     */
    default Optional<SlashCommandInteractionOption> getOptionByIndex(int index) {
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
        return getOptionByIndex(index).flatMap(SlashCommandInteractionOption::getStringValue);
    }

    /**
     * Gets the integer value of an option at the specified index.
     *
     * @param index The index of the option to search for.
     * @return An Optional with the integer value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Integer> getOptionIntValueByIndex(int index) {
        return getOptionByIndex(index).flatMap(SlashCommandInteractionOption::getIntValue);
    }

    /**
     * Gets the boolean value of an option at the specified index.
     *
     * @param index The index of the option to search for.
     * @return An Optional with the boolean value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Boolean> getOptionBooleanValueByIndex(int index) {
        return getOptionByIndex(index).flatMap(SlashCommandInteractionOption::getBooleanValue);
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
        return getOptionByIndex(index).flatMap(SlashCommandInteractionOption::getUserValue);
    }

    /**
     * Gets the user value of an option at the specified index.
     *
     * @param index The index of the option to search for.
     * @return An Optional with the user value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<CompletableFuture<User>> requestOptionUserValueByIndex(int index) {
        return getOptionByIndex(index).flatMap(SlashCommandInteractionOption::requestUserValue);
    }

    /**
     * Gets the channel value of an option at the specified index.
     *
     * @param index The index of the option to search for.
     * @return An Optional with the channel value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<ServerChannel> getOptionChannelValueByIndex(int index) {
        return getOptionByIndex(index).flatMap(SlashCommandInteractionOption::getChannelValue);
    }

    /**
     * Gets the role value of an option at the specified index.
     *
     * @param index The index of the option to search for.
     * @return An Optional with the role value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Role> getOptionRoleValueByIndex(int index) {
        return getOptionByIndex(index).flatMap(SlashCommandInteractionOption::getRoleValue);
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
        return getOptionByIndex(index).flatMap(SlashCommandInteractionOption::getMentionableValue);
    }

    /**
     * Gets the mentionable value of an option at the specified index which may be a user, channel or role.
     *
     * @param index The index of the option to search for.
     * @return An Optional with the mentionable value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<CompletableFuture<Mentionable>> requestOptionMentionableValueByIndex(int index) {
        return getOptionByIndex(index).flatMap(SlashCommandInteractionOption::requestFirstOptionMentionableValue);
    }
}

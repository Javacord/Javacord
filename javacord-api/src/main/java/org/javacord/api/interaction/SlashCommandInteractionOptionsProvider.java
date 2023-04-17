package org.javacord.api.interaction;

import org.javacord.api.entity.Attachment;
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
     * Gets the params and values from the user, or, if this command contains subcommands and/or
     * subcommand groups, a list of subcommands and subcommand groups.
     *
     * @return The params and values from the user.
     */
    List<SlashCommandInteractionOption> getOptions();

    /**
     * Gets the arguments of this slash command if there are any.
     *
     * <p>This is a shorthand method to avoid checking for Subcommmands or SubcommandGroups
     * to get the slash command arguments.
     *
     * @return The argument options.
     */
    List<SlashCommandInteractionOption> getArguments();

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
     * Get an argument having the specified name.
     *
     * @param name The name of the argument to search for.
     * @return The argument with the specified name, if found; an empty Optional otherwise
     */
    default Optional<SlashCommandInteractionOption> getArgumentByName(String name) {
        return getArguments()
                .stream()
                .filter(option -> option.getName().equalsIgnoreCase(name))
                .findAny();
    }

    /**
     * Gets the string representation value of an argument having the specified name.
     *
     * @param name The name of the argument to search for.
     * @return An Optional with the string representation value of such an argument, if the argument exists;
     *         an empty Optional otherwise
     */
    default Optional<String> getArgumentStringRepresentationValueByName(String name) {
        return getArgumentByName(name).flatMap(SlashCommandInteractionOption::getStringRepresentationValue);
    }

    /**
     * Gets the string value of an argument having the specified name.
     *
     * @param name The name of the argument to search for.
     * @return An Optional with the string value of such an argument if it exists; an empty Optional otherwise
     */
    default Optional<String> getArgumentStringValueByName(String name) {
        return getArgumentByName(name).flatMap(SlashCommandInteractionOption::getStringValue);
    }

    /**
     * Gets the long value of an argument having the specified name.
     * This will be present if the argument is of type {@link SlashCommandOptionType#LONG}.
     *
     * @param name The name of the argument to search for.
     * @return An Optional with the long value of such an argument if it exists; an empty Optional otherwise
     */
    default Optional<Long> getArgumentLongValueByName(String name) {
        return getArgumentByName(name).flatMap(SlashCommandInteractionOption::getLongValue);
    }

    /**
     * Gets the boolean value of an argument having the specified name.
     *
     * @param name The name of the argument to search for.
     * @return An Optional with the boolean value of such an argument if it exists; an empty Optional otherwise
     */
    default Optional<Boolean> getArgumentBooleanValueByName(String name) {
        return getArgumentByName(name).flatMap(SlashCommandInteractionOption::getBooleanValue);
    }

    /**
     * Gets the user value of an argument having the specified name.
     * Note: This method only respects cached users. To fetch the user from Discord if the user is not cached,
     * use {@code requestArgumentUserValueByName()}.
     *
     * @param name The name of the argument to search for.
     * @return An Optional with the user value of such an argument if it exists; an empty Optional otherwise
     */
    default Optional<User> getArgumentUserValueByName(String name) {
        return getArgumentByName(name).flatMap(SlashCommandInteractionOption::getUserValue);
    }

    /**
     * Gets the user value of an argument having the specified name.
     *
     * @param name The name of the argument to search for.
     * @return An Optional with the user value of such an argument if it exists; an empty Optional otherwise
     */
    default Optional<CompletableFuture<User>> requestArgumentUserValueByName(String name) {
        return getArgumentByName(name).flatMap(SlashCommandInteractionOption::requestUserValue);
    }

    /**
     * Gets the channel value of an argument having the specified name.
     *
     * @param name The name of the argument to search for.
     * @return An Optional with the channel value of such an argument if it exists; an empty Optional otherwise
     */
    default Optional<ServerChannel> getArgumentChannelValueByName(String name) {
        return getArgumentByName(name).flatMap(SlashCommandInteractionOption::getChannelValue);
    }

    /**
     * Gets the role value of an argument having the specified name.
     *
     * @param name The name of the argument to search for.
     * @return An Optional with the role value of such an argument if it exists; an empty Optional otherwise
     */
    default Optional<Role> getArgumentRoleValueByName(String name) {
        return getArgumentByName(name).flatMap(SlashCommandInteractionOption::getRoleValue);
    }

    /**
     * Gets the mentionable value of an argument having the specified name which may be a user, channel or role.
     * Note: This method only respects cached users if the ID of the Mentionable belongs to a user. To fetch the user
     * from Discord if the user is not cached,
     * use {@code requestArgumentMentionableValueByName()}.
     *
     * @param name The name of the argument to search for.
     * @return An Optional with the mentionable value of such an argument if it exists; an empty Optional otherwise
     */
    default Optional<Mentionable> getArgumentMentionableValueByName(String name) {
        return getArgumentByName(name).flatMap(SlashCommandInteractionOption::getMentionableValue);
    }

    /**
     * Gets the mentionable value of an argument having the specified name which may be a user, channel or role.
     *
     * @param name The name of the argument to search for.
     * @return An Optional with the mentionable value of such an argument if it exists; an empty Optional otherwise
     */
    default Optional<CompletableFuture<Mentionable>> requestArgumentMentionableValueByName(String name) {
        return getArgumentByName(name).flatMap(SlashCommandInteractionOption::requestMentionableValue);
    }

    /**
     * Gets the double value of an argument having the specified name.
     *
     * @param name The name of the argument to search for.
     * @return An Optional with the double value of such an argument if it exists; an empty Optional otherwise
     */
    default Optional<Double> getArgumentDecimalValueByName(String name) {
        return getArgumentByName(name).flatMap(SlashCommandInteractionOption::getDecimalValue);
    }

    /**
     * Gets the attachment value of an argument having the specified name.
     *
     * @param name The name of the argument to search for.
     * @return An Optional with the attachment value of such an argument if it exists; an empty Optional otherwise
     */
    default Optional<Attachment> getArgumentAttachmentValueByName(String name) {
        return getArgumentByName(name).flatMap(SlashCommandInteractionOption::getAttachmentValue);
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
     * Gets the argument at the specified index, if present.
     *
     * @param index The index of the argument to search for.
     * @return The argument with the specified index, if found; an empty Optional otherwise
     */
    default Optional<SlashCommandInteractionOption> getArgumentByIndex(int index) {
        return getArguments()
                .stream()
                .skip(index)
                .findFirst();
    }

    /**
     * Gets the string representation value of an argument at the specified index.
     *
     * @param index The index of the argument to search for.
     * @return An Optional with the string representation value of such an argument, if the argument exists;
     *         an empty Optional otherwise
     */
    default Optional<String> getArgumentStringRepresentationValueByIndex(int index) {
        return getArgumentByIndex(index).flatMap(SlashCommandInteractionOption::getStringRepresentationValue);
    }

    /**
     * Gets the string value of an argument at the specified index.
     *
     * @param index The index of the argument to search for.
     * @return An Optional with the string value of such an argument if it exists; an empty Optional otherwise
     */
    default Optional<String> getArgumentStringValueByIndex(int index) {
        return getArgumentByIndex(index).flatMap(SlashCommandInteractionOption::getStringValue);
    }

    /**
     * Gets the long value of an argument at the specified index.
     * This will be present if the argument is of type {@link SlashCommandOptionType#LONG}.
     *
     * @param index The index of the argument to search for.
     * @return An Optional with the long value of such an argument if it exists; an empty Optional otherwise
     */
    default Optional<Long> getArgumentLongValueByIndex(int index) {
        return getArgumentByIndex(index).flatMap(SlashCommandInteractionOption::getLongValue);
    }

    /**
     * Gets the boolean value of an argument at the specified index.
     *
     * @param index The index of the argument to search for.
     * @return An Optional with the boolean value of such an argument if it exists; an empty Optional otherwise
     */
    default Optional<Boolean> getArgumentBooleanValueByIndex(int index) {
        return getArgumentByIndex(index).flatMap(SlashCommandInteractionOption::getBooleanValue);
    }

    /**
     * Gets the user value of an argument at the specified index.
     * Note: This method only respects cached users. To fetch the user from Discord if the user is not cached,
     * use {@code requestArgumentUserValueByIndex()}.
     *
     * @param index The index of the argument to search for.
     * @return An Optional with the user value of such an argument if it exists; an empty Optional otherwise
     */
    default Optional<User> getArgumentUserValueByIndex(int index) {
        return getArgumentByIndex(index).flatMap(SlashCommandInteractionOption::getUserValue);
    }

    /**
     * Gets the user value of an argument at the specified index.
     *
     * @param index The index of the argument to search for.
     * @return An Optional with the user value of such an argument if it exists; an empty Optional otherwise
     */
    default Optional<CompletableFuture<User>> requestArgumentUserValueByIndex(int index) {
        return getArgumentByIndex(index).flatMap(SlashCommandInteractionOption::requestUserValue);
    }

    /**
     * Gets the channel value of an argument at the specified index.
     *
     * @param index The index of the argument to search for.
     * @return An Optional with the channel value of such an argument if it exists; an empty Optional otherwise
     */
    default Optional<ServerChannel> getArgumentChannelValueByIndex(int index) {
        return getArgumentByIndex(index).flatMap(SlashCommandInteractionOption::getChannelValue);
    }

    /**
     * Gets the role value of an argument at the specified index.
     *
     * @param index The index of the argument to search for.
     * @return An Optional with the role value of such an argument if it exists; an empty Optional otherwise
     */
    default Optional<Role> getArgumentRoleValueByIndex(int index) {
        return getArgumentByIndex(index).flatMap(SlashCommandInteractionOption::getRoleValue);
    }

    /**
     * Gets the mentionable value of an argument at the specified index which may be a user, channel or role.
     * Note: This method only respects cached users if the ID of the Mentionable belongs to a user. To fetch the user
     * from Discord if the user is not cached,
     * use {@code requestArgumentMentionableValueByIndex()}.
     *
     * @param index The index of the argument to search for.
     * @return An Optional with the mentionable value of such an argument if it exists; an empty Optional otherwise
     */
    default Optional<Mentionable> getArgumentMentionableValueByIndex(int index) {
        return getArgumentByIndex(index).flatMap(SlashCommandInteractionOption::getMentionableValue);
    }

    /**
     * Gets the mentionable value of an argument at the specified index which may be a user, channel or role.
     *
     * @param index The index of the argument to search for.
     * @return An Optional with the mentionable value of such an argument if it exists; an empty Optional otherwise
     */
    default Optional<CompletableFuture<Mentionable>> requestArgumentMentionableValueByIndex(int index) {
        return getArgumentByIndex(index).flatMap(SlashCommandInteractionOption::requestMentionableValue);
    }

    /**
     * Gets the double value of an argument at the specified index.
     *
     * @param index The index of the argument to search for.
     * @return An Optional with the double value of such an argument if it exists; an empty Optional otherwise
     */
    default Optional<Double> getArgumentDecimalValueByIndex(int index) {
        return getArgumentByIndex(index).flatMap(SlashCommandInteractionOption::getDecimalValue);
    }

    /**
     * Gets the attachment value of an argument at the specified index.
     *
     * @param index The index of the argument to search for.
     * @return An Optional with the attachment value of such an argument if it exists; an empty Optional otherwise
     */
    default Optional<Attachment> getArgumentAttachmentValueByIndex(int index) {
        return getArgumentByIndex(index).flatMap(SlashCommandInteractionOption::getAttachmentValue);
    }
}

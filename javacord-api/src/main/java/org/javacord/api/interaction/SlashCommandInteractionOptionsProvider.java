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
     * Gets a list with the params and values from the user, or, if this command contains subcommands and/or
     * subcommand groups, a list of subcommands and subcommand groups.
     *
     * @return A list with the params and values from the user.
     */
    List<SlashCommandInteractionOption> getOptions();

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
     * Gets the string representation value of an option having the specified name.
     *
     * @param name The name of the option to search for.
     * @return An Optional with the string value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<String> getOptionStringRepresentationValueByName(String name) {
        return getOptionByName(name).flatMap(SlashCommandInteractionOption::getStringRepresentationValue);
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
     * Gets the long value of an option having the specified name.
     * This will be present if the option is of type {@link SlashCommandOptionType#LONG}.
     *
     * @param name The name of the option to search for.
     * @return An Optional with the long value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Long> getOptionLongValueByName(String name) {
        return getOptionByName(name).flatMap(SlashCommandInteractionOption::getLongValue);
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
        return getOptionByName(name).flatMap(SlashCommandInteractionOption::requestMentionableValue);
    }

    /**
     * Gets the double value of an option having the specified name.
     *
     * @param name The name of the option to search for.
     * @return An Optional with the double value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Double> getOptionDecimalValueByName(String name) {
        return getOptionByName(name).flatMap(SlashCommandInteractionOption::getDecimalValue);
    }

    /**
     * Gets the attachment value of an option having the specified name.
     *
     * @param name The name of the option to search for.
     * @return An Optional with the attachment value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Attachment> getOptionAttachmentValueByName(String name) {
        return getOptionByName(name).flatMap(SlashCommandInteractionOption::getAttachmentValue);
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
     * Gets the string representation value of an option at the specified index.
     *
     * @param index The index of the option to search for.
     * @return An Optional with the string value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<String> getOptionStringRepresentationValueByIndex(int index) {
        return getOptionByIndex(index).flatMap(SlashCommandInteractionOption::getStringRepresentationValue);
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
     * Gets the long value of an option at the specified index.
     * This will be present if the option is of type {@link SlashCommandOptionType#LONG}.
     *
     * @param index The index of the option to search for.
     * @return An Optional with the long value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Long> getOptionLongValueByIndex(int index) {
        return getOptionByIndex(index).flatMap(SlashCommandInteractionOption::getLongValue);
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
        return getOptionByIndex(index).flatMap(SlashCommandInteractionOption::requestMentionableValue);
    }

    /**
     * Gets the double value of an option at the specified index.
     *
     * @param index The index of the option to search for.
     * @return An Optional with the double value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Double> getOptionDecimalValueByIndex(int index) {
        return getOptionByIndex(index).flatMap(SlashCommandInteractionOption::getDecimalValue);
    }

    /**
     * Gets the attachment value of an option at the specified index.
     *
     * @param index The index of the option to search for.
     * @return An Optional with the attachment value of such an option if it exists; an empty Optional otherwise
     */
    default Optional<Attachment> getOptionAttachmentValueByIndex(int index) {
        return getOptionByIndex(index).flatMap(SlashCommandInteractionOption::getAttachmentValue);
    }
}

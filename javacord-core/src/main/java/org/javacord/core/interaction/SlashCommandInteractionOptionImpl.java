package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.Attachment;
import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.api.interaction.SlashCommandOptionType;
import org.javacord.core.util.logging.LoggerUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class SlashCommandInteractionOptionImpl implements SlashCommandInteractionOption {

    private final DiscordApi api;
    private final Map<Long, User> resolvedUsers;
    private final String name;
    private final String stringRepresentation;
    private final String stringValue;
    /**
     * This is {@link SlashCommandOptionType#LONG} but it can be any integer between -2^53 and 2^53 and
     * therefore exceeds Javas Integer range. This is the INTEGER option according to the Discord docs.
     */
    private final Long longValue;
    private final Boolean booleanValue;
    private final Long userValue;
    private final Long channelValue;
    private final Long roleValue;
    private final Long mentionableValue;
    private final Attachment attachmentValue;
    /**
     * This is the NUMBER option according to the Discord docs.
     */
    private final Double decimalValue;

    private final List<SlashCommandInteractionOption> options;

    private final Boolean focused;

    private static final Logger LOGGER = LoggerUtil.getLogger(SlashCommandInteractionOptionImpl.class);

    /**
     * Class constructor.
     *
     * @param api           The DiscordApi instance.
     * @param jsonData      The json data of the option.
     * @param resolvedUsers The map of resolved users and their ID.
     * @param resolvedAttachments The map of resolved attachments and their ID.
     */
    public SlashCommandInteractionOptionImpl(final DiscordApi api, final JsonNode jsonData,
                                             Map<Long, User> resolvedUsers, Map<Long, Attachment> resolvedAttachments) {
        this.api = api;
        this.resolvedUsers = resolvedUsers;
        name = jsonData.get("name").asText();
        focused = jsonData.has("focused") ? jsonData.get("focused").asBoolean() : null;
        options = new ArrayList<>();
        final JsonNode valueNode = jsonData.get("value");

        String localStringRepresentation = null;
        String localStringValue = null;
        Long localLongValue = null;
        Boolean localBooleanValue = null;
        Long localUserValue = null;
        Long localChannelValue = null;
        Long localRoleValue = null;
        Long localMentionableValue = null;
        Double localDecimalValue = null;
        Attachment localAttachmentValue = null;

        final int typeInt = jsonData.get("type").asInt();
        final SlashCommandOptionType slashCommandOptionType = SlashCommandOptionType.fromValue(typeInt);
        switch (slashCommandOptionType) {
            case SUB_COMMAND:
            case SUB_COMMAND_GROUP:
                if (jsonData.has("options") && jsonData.get("options").isArray()) {
                    for (final JsonNode optionJson : jsonData.get("options")) {
                        options.add(new SlashCommandInteractionOptionImpl(api, optionJson, resolvedUsers,
                                resolvedAttachments));
                    }
                }
                break;
            case STRING:
                localStringValue = valueNode.asText();
                localStringRepresentation = localStringValue;
                break;
            case LONG:
                localLongValue = valueNode.asLong();
                localStringRepresentation = String.valueOf(localLongValue);
                break;
            case BOOLEAN:
                localBooleanValue = valueNode.asBoolean();
                localStringRepresentation = String.valueOf(localBooleanValue);
                break;
            case USER:
                localUserValue = Long.parseLong(valueNode.asText());
                localStringRepresentation = String.valueOf(localUserValue);
                break;
            case CHANNEL:
                localChannelValue = Long.parseLong(valueNode.asText());
                localStringRepresentation = String.valueOf(localChannelValue);
                break;
            case ROLE:
                localRoleValue = Long.parseLong(valueNode.asText());
                localStringRepresentation = String.valueOf(localRoleValue);
                break;
            case MENTIONABLE:
                localMentionableValue = Long.parseLong(valueNode.asText());
                localStringRepresentation = String.valueOf(localMentionableValue);
                break;
            case DECIMAL:
                localDecimalValue = valueNode.asDouble();
                localStringRepresentation = String.valueOf(localDecimalValue);
                break;
            case ATTACHMENT:
                localAttachmentValue = resolvedAttachments.get(Long.parseLong(valueNode.asText()));
                localStringRepresentation = valueNode.asText();
                break;
            default:
                LOGGER.warn("Received slash command option of unknown type <{}>. "
                        + "Please contact the developer!", typeInt);
        }

        stringRepresentation = localStringRepresentation;
        stringValue = localStringValue;
        longValue = localLongValue;
        booleanValue = localBooleanValue;
        userValue = localUserValue;
        channelValue = localChannelValue;
        roleValue = localRoleValue;
        mentionableValue = localMentionableValue;
        decimalValue = localDecimalValue;
        attachmentValue = localAttachmentValue;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Optional<Boolean> isFocused() {
        return Optional.ofNullable(focused);
    }

    @Override
    public Optional<String> getStringRepresentationValue() {
        return Optional.ofNullable(stringRepresentation);
    }

    @Override
    public Optional<String> getStringValue() {
        return Optional.ofNullable(stringValue);
    }

    @Override
    public Optional<Long> getLongValue() {
        return Optional.ofNullable(longValue);
    }

    @Override
    public Optional<Boolean> getBooleanValue() {
        return Optional.ofNullable(booleanValue);
    }

    @Override
    public Optional<User> getUserValue() {
        return Optional.ofNullable(userValue)
                .map(id -> api.getCachedUserById(id).orElseGet(() -> resolvedUsers.get(id)));
    }

    @Override
    public Optional<CompletableFuture<User>> requestUserValue() {
        return Optional.ofNullable(userValue)
                .map(api::getUserById);
    }

    @Override
    public Optional<ServerChannel> getChannelValue() {
        return Optional.ofNullable(channelValue)
                .flatMap(api::getServerChannelById);
    }

    @Override
    public Optional<Attachment> getAttachmentValue() {
        return Optional.ofNullable(attachmentValue);
    }

    @Override
    public Optional<Role> getRoleValue() {
        return Optional.ofNullable(roleValue)
                .flatMap(api::getRoleById);
    }

    @Override
    public Optional<Mentionable> getMentionableValue() {
        Optional<Mentionable> mentionable = Optional.empty();
        // No Optional#or() in Java 8 :(
        if (mentionableValue != null) {
            mentionable = api.getRoleById(mentionableValue).map(Mentionable.class::cast);
            if (mentionable.isPresent()) {
                return mentionable;
            }

            mentionable = api.getServerChannelById(mentionableValue).map(Mentionable.class::cast);
            if (mentionable.isPresent()) {
                return mentionable;
            }

            mentionable = api.getCachedUserById(mentionableValue).map(Mentionable.class::cast);
            if (!mentionable.isPresent()) {
                mentionable = Optional.ofNullable(resolvedUsers.get(mentionableValue)).map(Mentionable.class::cast);
            }
        }
        return mentionable;
    }

    @Override
    public Optional<Double> getDecimalValue() {
        return Optional.ofNullable(decimalValue);
    }

    @Override
    public Optional<CompletableFuture<Mentionable>> requestMentionableValue() {
        final Optional<CompletableFuture<Mentionable>> cacheOptional = getMentionableValue()
                .map(CompletableFuture::completedFuture);
        if (cacheOptional.isPresent()) {
            return cacheOptional;
        }
        return Optional.ofNullable(mentionableValue)
                .map(api::getUserById).map(future -> future.thenApply(Mentionable.class::cast));
    }

    @Override
    public List<SlashCommandInteractionOption> getOptions() {
        return Collections.unmodifiableList(options);
    }

    // TODO: Move the implementation to the SlashCommandInteractionOptionsProvider once we upgraded to Java 9+
    // because interfaces currently do not support private methods
    @Override
    public List<SlashCommandInteractionOption> getArguments() {
        return getArgumentsRecursive(getOptions());
    }

    private List<SlashCommandInteractionOption> getArgumentsRecursive(List<SlashCommandInteractionOption> options) {
        if (options.isEmpty()) {
            return Collections.emptyList();
        } else if (options.get(0).isSubcommandOrGroup()) {
            return getArgumentsRecursive(options.get(0).getOptions());
        } else {
            return options;
        }
    }
}

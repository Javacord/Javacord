package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
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
    private final Integer intValue;
    private final Boolean booleanValue;
    private final Long userValue;
    private final Long channelValue;
    private final Long roleValue;
    private final Long mentionableValue;
    private final Double numberValue;

    private final List<SlashCommandInteractionOption> options;

    private static final Logger LOGGER = LoggerUtil.getLogger(SlashCommandInteractionOptionImpl.class);

    /**
     * Class constructor.
     *
     * @param api      The DiscordApi instance.
     * @param jsonData The json data of the option.
     * @param resolvedUsers The map of resolved users and their ID.
     */
    public SlashCommandInteractionOptionImpl(final DiscordApi api, final JsonNode jsonData,
                                             Map<Long, User> resolvedUsers) {
        this.api = api;
        this.resolvedUsers = resolvedUsers;
        name = jsonData.get("name").asText();
        options = new ArrayList<>();
        final JsonNode valueNode = jsonData.get("value");

        String localStringRepresentation = null;
        String localStringValue = null;
        Integer localIntValue = null;
        Boolean localBooleanValue = null;
        Long localUserValue = null;
        Long localChannelValue = null;
        Long localRoleValue = null;
        Long localMentionableValue = null;
        Double localNumberValue = null;

        final int typeInt = jsonData.get("type").asInt();
        final SlashCommandOptionType slashCommandOptionType = SlashCommandOptionType.fromValue(typeInt);
        switch (slashCommandOptionType) {
            case SUB_COMMAND:
            case SUB_COMMAND_GROUP:
                if (jsonData.has("options") && jsonData.get("options").isArray()) {
                    for (final JsonNode optionJson : jsonData.get("options")) {
                        options.add(new SlashCommandInteractionOptionImpl(api, optionJson, resolvedUsers));
                    }
                }
                break;
            case STRING:
                localStringValue = valueNode.asText();
                localStringRepresentation = localStringValue;
                break;
            case INTEGER:
                localIntValue = valueNode.asInt();
                localStringRepresentation = String.valueOf(localIntValue);
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
            case NUMBER:
                localNumberValue = valueNode.asDouble();
                localStringRepresentation = String.valueOf(localNumberValue);
                break;
            default:
                LOGGER.warn("Received slash command option of unknown type <{}>. "
                        + "Please contact the developer!", typeInt);
        }

        stringRepresentation = localStringRepresentation;
        stringValue = localStringValue;
        intValue = localIntValue;
        booleanValue = localBooleanValue;
        userValue = localUserValue;
        channelValue = localChannelValue;
        roleValue = localRoleValue;
        mentionableValue = localMentionableValue;
        numberValue = localNumberValue;
    }

    @Override
    public String getName() {
        return name;
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
    public Optional<Integer> getIntValue() {
        return Optional.ofNullable(intValue);
    }

    @Override
    public Optional<Boolean> getBooleanValue() {
        return Optional.ofNullable(booleanValue);
    }

    @Override
    public Optional<User> getUserValue() {
        return Optional.ofNullable(api.getCachedUserById(userValue)
                .orElseGet(() -> resolvedUsers.get(userValue)));
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
    public Optional<Double> getNumberValue() {
        return Optional.ofNullable(numberValue);
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
}

package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.Mentionable;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.api.util.DiscordRegexPattern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class SlashCommandInteractionOptionImpl implements SlashCommandInteractionOption {

    private final DiscordApi api;
    private final String name;
    private final String stringValue;
    private final Integer intValue;
    private final Boolean booleanValue;
    private final List<SlashCommandInteractionOption> options;

    /**
     * Class constructor.
     *
     * @param api      The DiscordApi instance.
     * @param jsonData The json data of the option.
     */
    public SlashCommandInteractionOptionImpl(DiscordApi api, JsonNode jsonData) {
        this.api = api;
        name = jsonData.get("name").asText();
        JsonNode valueNode = jsonData.get("value");

        if (valueNode != null && valueNode.isTextual()) {
            stringValue = valueNode.asText();
            intValue = null;
            booleanValue = null;
        } else if (valueNode != null && valueNode.isInt()) {
            intValue = valueNode.asInt();
            stringValue = null;
            booleanValue = null;
        } else if (valueNode != null && valueNode.isBoolean()) {
            booleanValue = valueNode.asBoolean();
            stringValue = null;
            intValue = null;
        } else {
            intValue = null;
            stringValue = null;
            booleanValue = null;
        }

        options = new ArrayList<>();
        if (jsonData.has("options") && jsonData.get("options").isArray()) {
            for (JsonNode optionJson : jsonData.get("options")) {
                options.add(new SlashCommandInteractionOptionImpl(api, optionJson));
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Optional<String> getStringValue() {
        if (stringValue != null) {
            return Optional.of(stringValue);
        }
        return getIntValue().map(String::valueOf);
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
        return getAsSnowflake()
                .flatMap(api::getCachedUserById);
    }

    @Override
    public Optional<CompletableFuture<User>> requestUserValue() {
        return getAsSnowflake()
                .map(api::getUserById);
    }

    @Override
    public Optional<ServerChannel> getChannelValue() {
        return getAsSnowflake()
                .flatMap(api::getServerChannelById);
    }

    @Override
    public Optional<Role> getRoleValue() {
        return getAsSnowflake()
                .flatMap(api::getRoleById);
    }

    @Override
    public Optional<Mentionable> getMentionableValue() {
        // No Optional#or() in Java 8 :(

        Optional<Mentionable> optional = getRoleValue().map(Mentionable.class::cast);
        if (optional.isPresent()) {
            return optional;
        }

        optional = getChannelValue().map(Mentionable.class::cast);
        if (optional.isPresent()) {
            return optional;
        }

        return getUserValue().map(Mentionable.class::cast);
    }

    @Override
    public Optional<CompletableFuture<Mentionable>> requestMentionableValue() {
        Optional<CompletableFuture<Mentionable>> cacheOptional = getMentionableValue()
                .map(CompletableFuture::completedFuture);
        if (cacheOptional.isPresent()) {
            return cacheOptional;
        }
        return requestUserValue().map(future -> future.thenApply(Mentionable.class::cast));
    }

    private Optional<String> getAsSnowflake() {
        return Optional.ofNullable(stringValue)
                .filter(s -> DiscordRegexPattern.SNOWFLAKE.matcher(s).matches());
    }

    @Override
    public List<SlashCommandInteractionOption> getOptions() {
        return Collections.unmodifiableList(options);
    }
}

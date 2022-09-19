package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.interaction.DiscordLocale;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionChoice;
import org.javacord.api.interaction.SlashCommandOptionType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class SlashCommandOptionImpl implements SlashCommandOption {

    private final SlashCommandOptionType type;
    private final String name;
    private final Map<DiscordLocale, String> nameLocalizations;
    private final String description;
    private final Map<DiscordLocale, String> descriptionLocalizations;
    private final boolean required;
    private final List<SlashCommandOptionChoice> choices;
    private final List<SlashCommandOption> options;
    private final Set<ChannelType> channelTypes;
    private final Long longMinValue;
    private final Long longMaxValue;
    private final Double decimalMinValue;
    private final Double decimalMaxValue;
    private final Long maxLength;
    private final Long minLength;
    private final boolean autocomplete;

    /**
     * Class constructor.
     *
     * @param data The json data of the slash command option.
     */
    public SlashCommandOptionImpl(JsonNode data) {
        type = SlashCommandOptionType.fromValue(data.get("type").intValue());
        name = data.get("name").asText();
        nameLocalizations = new HashMap<>();
        data.path("name_localizations").fields().forEachRemaining(e ->
                nameLocalizations.put(DiscordLocale.fromLocaleCode(e.getKey()), e.getValue().asText()));
        description = data.get("description").asText();
        descriptionLocalizations = new HashMap<>();
        data.path("description_localizations").fields().forEachRemaining(e ->
                descriptionLocalizations.put(DiscordLocale.fromLocaleCode(e.getKey()), e.getValue().asText()));
        required = data.has("required") && data.get("required").asBoolean(false);
        autocomplete = data.has("autocomplete") && data.get("autocomplete").asBoolean();
        choices = new ArrayList<>();
        if (data.has("choices")) {
            for (JsonNode choiceJson : data.get("choices")) {
                choices.add(new SlashCommandOptionChoiceImpl(choiceJson));
            }
        }

        options = new ArrayList<>();
        if (data.has("options")) {
            for (JsonNode optionJson : data.get("options")) {
                options.add(new SlashCommandOptionImpl(optionJson));
            }
        }

        channelTypes = new HashSet<>();
        if (data.has("channel_types")) {
            for (JsonNode channelTypeJson : data.get("channel_types")) {
                channelTypes.add(ChannelType.fromId(channelTypeJson.intValue()));
            }
        }

        if (type == SlashCommandOptionType.LONG) {
            longMinValue = data.has("min_value") ? data.get("min_value").asLong() : null;
            longMaxValue = data.has("max_value") ? data.get("max_value").asLong() : null;
            decimalMinValue = null;
            decimalMaxValue = null;
            minLength = null;
            maxLength = null;
        } else if (type == SlashCommandOptionType.DECIMAL) {
            decimalMinValue = data.has("min_value") ? data.get("min_value").asDouble() : null;
            decimalMaxValue = data.has("max_value") ? data.get("max_value").asDouble() : null;
            longMinValue = null;
            longMaxValue = null;
            minLength = null;
            maxLength = null;
        } else if (type == SlashCommandOptionType.STRING) {
            minLength = data.has("min_length") ? data.get("min_length").asLong() : null;
            maxLength = data.has("max_length") ? data.get("max_length").asLong() : null;
            longMinValue = null;
            longMaxValue = null;
            decimalMinValue = null;
            decimalMaxValue = null;
        } else {
            minLength = null;
            maxLength = null;
            longMinValue = null;
            longMaxValue = null;
            decimalMinValue = null;
            decimalMaxValue = null;
        }
    }

    /**
     * Class constructor.
     *
     * @param type The type.
     * @param name The name.
     * @param nameLocalizations The name localizations.
     * @param description The description.
     * @param descriptionLocalizations The description localizations.
     * @param required If the option is required.
     * @param autocomplete If the option is autocompletable.
     * @param choices The choices.
     * @param options The options.
     * @param channelTypes The channel types.
     * @param longMinValue The {@link SlashCommandOptionType#LONG} min value
     * @param longMaxValue The {@link SlashCommandOptionType#LONG} max value
     * @param decimalMinValue The {@link SlashCommandOptionType#DECIMAL} min value
     * @param decimalMaxValue The {@link SlashCommandOptionType#DECIMAL} max value
     * @param minLength The {@link SlashCommandOptionType#STRING} min length.
     * @param maxLength The {@link SlashCommandOptionType#STRING} max length.
     */
    public SlashCommandOptionImpl(
            SlashCommandOptionType type,
            String name,
            Map<DiscordLocale, String> nameLocalizations,
            String description,
            Map<DiscordLocale, String> descriptionLocalizations,
            boolean required,
            boolean autocomplete,
            List<SlashCommandOptionChoice> choices,
            List<SlashCommandOption> options,
            Set<ChannelType> channelTypes,
            Long longMinValue,
            Long longMaxValue,
            Double decimalMinValue,
            Double decimalMaxValue,
            Long minLength,
            Long maxLength
    ) {
        this.type = type;
        this.name = name;
        this.nameLocalizations = nameLocalizations;
        this.description = description;
        this.descriptionLocalizations = descriptionLocalizations;
        this.required = required;
        this.autocomplete = autocomplete;
        this.choices = choices;
        this.options = options;
        this.channelTypes = channelTypes;
        this.longMinValue = longMinValue;
        this.longMaxValue = longMaxValue;
        this.decimalMinValue = decimalMinValue;
        this.decimalMaxValue = decimalMaxValue;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    @Override
    public SlashCommandOptionType getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<DiscordLocale, String> getNameLocalizations() {
        return Collections.unmodifiableMap(nameLocalizations);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Map<DiscordLocale, String> getDescriptionLocalizations() {
        return Collections.unmodifiableMap(descriptionLocalizations);
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public boolean isAutocompletable() {
        return autocomplete;
    }

    @Override
    public List<SlashCommandOptionChoice> getChoices() {
        return Collections.unmodifiableList(choices);
    }

    @Override
    public List<SlashCommandOption> getOptions() {
        return Collections.unmodifiableList(options);
    }

    @Override
    public Set<ChannelType> getChannelTypes() {
        return Collections.unmodifiableSet(channelTypes);
    }

    @Override
    public Optional<Long> getLongMinValue() {
        return Optional.ofNullable(longMinValue);
    }

    @Override
    public Optional<Long> getLongMaxValue() {
        return Optional.ofNullable(longMaxValue);
    }

    @Override
    public Optional<Double> getDecimalMinValue() {
        return Optional.ofNullable(decimalMinValue);
    }

    @Override
    public Optional<Double> getDecimalMaxValue() {
        return Optional.ofNullable(decimalMaxValue);
    }

    @Override
    public Optional<Long> getMinLength() {
        return Optional.ofNullable(minLength);
    }

    @Override
    public Optional<Long> getMaxLength() {
        return Optional.ofNullable(maxLength);
    }

    /**
     * Creates a json node with the option's data.
     *
     * @return The json node.
     */
    public JsonNode toJsonNode() {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("type", type.getValue());
        node.put("name", name);
        node.put("description", description);
        node.put("required", required);
        node.put("autocomplete", autocomplete);
        if (!choices.isEmpty()) {
            ArrayNode jsonChoices = node.putArray("choices");
            choices.forEach(choice -> jsonChoices.add(((SlashCommandOptionChoiceImpl) choice).toJsonNode()));
        }
        if (!options.isEmpty()) {
            ArrayNode jsonOptions = node.putArray("options");
            options.forEach(option -> jsonOptions.add(((SlashCommandOptionImpl) option).toJsonNode()));
        }
        if (!channelTypes.isEmpty()) {
            ArrayNode jsonChannelTypes = node.putArray("channel_types");
            channelTypes.forEach(channelType -> jsonChannelTypes.add(channelType.getId()));
        }
        if (type == SlashCommandOptionType.LONG) {
            if (longMinValue != null) {
                node.put("min_value", longMinValue);
            }
            if (longMaxValue != null) {
                node.put("max_value", longMaxValue);
            }
        } else if (type == SlashCommandOptionType.DECIMAL) {
            if (decimalMinValue != null) {
                node.put("min_value", decimalMinValue);
            }
            if (decimalMaxValue != null) {
                node.put("max_value", decimalMaxValue);
            }
        } else if (type == SlashCommandOptionType.STRING) {
            if (minLength != null) {
                node.put("min_length", minLength);
            }
            if (maxLength != null) {
                node.put("max_length", maxLength);
            }
        }
        if (!nameLocalizations.isEmpty()) {
            ObjectNode nameLocalizationsJsonObject = node.putObject("name_localizations");
            nameLocalizations.forEach(
                    (locale, localization) -> nameLocalizationsJsonObject.put(locale.getLocaleCode(), localization));
        }
        if (!descriptionLocalizations.isEmpty()) {
            ObjectNode descriptionLocalizationsJsonObject = node.putObject("description_localizations");
            descriptionLocalizations.forEach(
                    (locale, localization) ->
                            descriptionLocalizationsJsonObject.put(locale.getLocaleCode(), localization));
        }
        return node;
    }
}

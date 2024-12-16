package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.interaction.DiscordLocale;
import org.javacord.api.interaction.SlashCommandOptionChoice;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SlashCommandOptionChoiceImpl implements SlashCommandOptionChoice {

    private final String name;
    private final Map<DiscordLocale, String> nameLocalizations;
    private final String stringValue;
    private final Long longValue;
    private final Double doubleValue;

    /**
     * Class constructor.
     *
     * @param data The json data of the choice.
     */
    public SlashCommandOptionChoiceImpl(JsonNode data) {
        name = data.get("name").asText();
        nameLocalizations = new HashMap<>();
        data.path("name_localizations").fields().forEachRemaining(e ->
                nameLocalizations.put(DiscordLocale.fromLocaleCode(e.getKey()), e.getValue().asText()));
        if (data.get("value").isTextual()) {
            stringValue = data.get("value").textValue();
        } else {
            stringValue = null;
        }
        if (data.get("value").canConvertToLong()) {
            longValue = data.get("value").asLong();
        } else {
            longValue = null;
        }
        if (data.get("value").isDouble()) {
            doubleValue = data.get("value").asDouble();
        } else {
            doubleValue = null;
        }
    }

    /**
     * Class constructor.
     *
     * @param name The name of the choice.
     * @param nameLocalizations The name localizations of this choice.
     * @param stringValue The string value of the choice or null if it is an int value.
     * @param longValue The long value of the choice or null if it is a string value.
     */
    public SlashCommandOptionChoiceImpl(String name, Map<DiscordLocale, String> nameLocalizations,
                                        String stringValue, Long longValue, Double doubleValue) {
        this.name = name;
        this.nameLocalizations = nameLocalizations;
        this.stringValue = stringValue;
        this.longValue = longValue;
        this.doubleValue = doubleValue;
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
    public Optional<String> getStringValue() {
        return Optional.ofNullable(stringValue);
    }

    @Override
    public Optional<Long> getLongValue() {
        return Optional.ofNullable(longValue);
    }

    @Override
    public Optional<Double> getDoubleValue() {
        return Optional.ofNullable(doubleValue);
    }

    /**
     * Creates a json node with the choice's data.
     *
     * @return The json node.
     */
    public JsonNode toJsonNode() {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("name", name);
        if (!nameLocalizations.isEmpty()) {
            ObjectNode nameLocalizationsJsonObject = node.putObject("name_localizations");
            nameLocalizations.forEach(
                    (locale, localization) -> nameLocalizationsJsonObject.put(locale.getLocaleCode(), localization));
        }
        getLongValue().ifPresent(value -> node.put("value", value));
        getStringValue().ifPresent(value -> node.put("value", value));
        getDoubleValue().ifPresent(value -> node.put("value", value));
        return node;
    }
}

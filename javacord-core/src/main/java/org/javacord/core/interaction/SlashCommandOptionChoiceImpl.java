package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.interaction.SlashCommandOptionChoice;

import java.util.Optional;

public class SlashCommandOptionChoiceImpl implements SlashCommandOptionChoice {

    private final String name;
    private final String stringValue;
    private final Long longValue;

    /**
     * Class constructor.
     *
     * @param data The json data of the choice.
     */
    public SlashCommandOptionChoiceImpl(JsonNode data) {
        name = data.get("name").asText();
        if (data.get("value").isTextual()) {
            stringValue = data.get("value").textValue();
        } else {
            stringValue = null;
        }
        if (data.get("value").isLong()) {
            longValue = data.get("value").asLong();
        } else {
            longValue = null;
        }
    }

    /**
     * Class constructor.
     *
     * @param name The name of the choice.
     * @param stringValue The string value of the choice or null if it is an int value.
     * @param longValue The long value of the choice or null if it is a string value.
     */
    public SlashCommandOptionChoiceImpl(String name, String stringValue, Long longValue) {
        this.name = name;
        this.stringValue = stringValue;
        this.longValue = longValue;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Optional<String> getStringValue() {
        return Optional.ofNullable(stringValue);
    }

    @Override
    public Optional<Long> getLongValue() {
        return Optional.ofNullable(longValue);
    }

    /**
     * Creates a json node with the choice's data.
     *
     * @return The json node.
     */
    public JsonNode toJsonNode() {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("name", name);
        getLongValue().ifPresent(value -> node.put("value", value));
        getStringValue().ifPresent(value -> node.put("value", value));
        return node;
    }
}

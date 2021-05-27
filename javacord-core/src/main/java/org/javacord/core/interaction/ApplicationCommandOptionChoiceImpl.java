package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.interaction.ApplicationCommandOptionChoice;

import java.util.Optional;

public class ApplicationCommandOptionChoiceImpl implements ApplicationCommandOptionChoice {

    private final String name;
    private final String stringValue;
    private final Integer intValue;

    /**
     * Class constructor.
     *
     * @param data The json data of the choice.
     */
    public ApplicationCommandOptionChoiceImpl(JsonNode data) {
        name = data.get("name").asText();
        if (data.get("value").isTextual()) {
            stringValue = data.get("value").textValue();
        } else {
            stringValue = null;
        }
        if (data.get("value").isInt()) {
            intValue = data.get("value").intValue();
        } else {
            intValue = null;
        }
    }

    /**
     * Class constructor.
     *
     * @param name The name of the choice.
     * @param stringValue The string value of the choice or null if it is an int value.
     * @param intValue The int value of the choice or null if it is a string value.
     */
    public ApplicationCommandOptionChoiceImpl(String name, String stringValue, Integer intValue) {
        this.name = name;
        this.stringValue = stringValue;
        this.intValue = intValue;
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
    public Optional<Integer> getIntValue() {
        return Optional.ofNullable(intValue);
    }

    /**
     * Creates a json node with the choice's data.
     *
     * @return The json node.
     */
    public JsonNode toJsonNode() {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("name", name);
        getIntValue().ifPresent(value -> node.put("value", value));
        getStringValue().ifPresent(value -> node.put("value", value));
        return node;
    }
}

package org.javacord.core.command;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.command.ApplicationCommandInteractionDataOption;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ApplicationCommandInteractionDataOptionImpl implements ApplicationCommandInteractionDataOption {

    private final String name;
    private final String stringValue;
    private final Integer intValue;
    private final List<ApplicationCommandInteractionDataOption> options;

    /**
     * Class constructor.
     *
     * @param jsonData The json data of the option.
     */
    public ApplicationCommandInteractionDataOptionImpl(JsonNode jsonData) {
        name = jsonData.get("name").asText();
        JsonNode valueNode = jsonData.get("value");

        if (valueNode != null && valueNode.isTextual()) {
            stringValue = valueNode.asText();
            intValue = null;
        } else if (valueNode != null && valueNode.isInt()) {
            intValue = valueNode.asInt();
            stringValue = null;
        } else {
            intValue = null;
            stringValue = null;
        }
        options = new ArrayList<>();
        if (jsonData.has("options") && jsonData.get("options").isArray()) {
            for (JsonNode optionJson : jsonData.get("options")) {
                options.add(new ApplicationCommandInteractionDataOptionImpl(optionJson));
            }
        }
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

    @Override
    public List<ApplicationCommandInteractionDataOption> getOptions() {
        return Collections.unmodifiableList(options);
    }
}

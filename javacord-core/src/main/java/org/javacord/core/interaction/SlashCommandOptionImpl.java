package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionChoice;
import org.javacord.api.interaction.SlashCommandOptionType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SlashCommandOptionImpl implements SlashCommandOption {

    private final SlashCommandOptionType type;
    private final String name;
    private final String description;
    private final boolean required;
    private final List<SlashCommandOptionChoice> choices;
    private final List<SlashCommandOption> options;

    /**
     * Class constructor.
     *
     * @param data The json data of the slash command option.
     */
    public SlashCommandOptionImpl(JsonNode data) {
        type = SlashCommandOptionType.fromValue(data.get("type").intValue());
        name = data.get("name").asText();
        description = data.get("description").asText();
        required = data.has("required") && data.get("required").asBoolean(false);
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
    }

    /**
     * Class constructor.
     *
     * @param type The type.
     * @param name The name.
     * @param description The description.
     * @param required If the option is required.
     * @param choices The choices.
     * @param options The options.
     */
    public SlashCommandOptionImpl(
            SlashCommandOptionType type,
            String name,
            String description,
            boolean required,
            List<SlashCommandOptionChoice> choices,
            List<SlashCommandOption> options
    ) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.required = required;
        this.choices = choices;
        this.options = options;
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
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public List<SlashCommandOptionChoice> getChoices() {
        return Collections.unmodifiableList(choices);
    }

    @Override
    public List<SlashCommandOption> getOptions() {
        return Collections.unmodifiableList(options);
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
        if (!choices.isEmpty()) {
            ArrayNode jsonChoices = node.putArray("choices");
            choices.forEach(choice -> jsonChoices.add(((SlashCommandOptionChoiceImpl) choice).toJsonNode()));
        }
        if (!options.isEmpty()) {
            ArrayNode jsonOptions = node.putArray("options");
            options.forEach(option -> jsonOptions.add(((SlashCommandOptionImpl) option).toJsonNode()));
        }
        return node;
    }
}

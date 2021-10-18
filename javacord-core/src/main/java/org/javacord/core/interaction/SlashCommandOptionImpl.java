package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionChoice;
import org.javacord.api.interaction.SlashCommandOptionType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class SlashCommandOptionImpl implements SlashCommandOption {

    private final SlashCommandOptionType type;
    private final String name;
    private final String description;
    private final boolean required;
    private final List<SlashCommandOptionChoice> choices;
    private final List<SlashCommandOption> options;
    private final Set<ChannelType> channelTypes;
    private final Long integerMinValue;
    private final Long integerMaxValue;
    private final Double numberMinValue;
    private final Double numberMaxValue;

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

        channelTypes = new HashSet<>();
        if (data.has("channel_types")) {
            for (JsonNode channelTypeJson : data.get("channel_types")) {
                channelTypes.add(ChannelType.fromId(channelTypeJson.intValue()));
            }
        }

        if (type == SlashCommandOptionType.INTEGER) {
            integerMinValue = data.has("min_value") ? data.get("min_value").asLong() : null;
            integerMaxValue = data.has("max_value") ? data.get("max_value").asLong() : null;
            numberMinValue = null;
            numberMaxValue = null;
        } else if (type == SlashCommandOptionType.NUMBER) {
            numberMinValue = data.has("min_value") ? data.get("min_value").asDouble() : null;
            numberMaxValue = data.has("max_value") ? data.get("max_value").asDouble() : null;
            integerMinValue = null;
            integerMaxValue = null;
        } else {
            integerMinValue = null;
            integerMaxValue = null;
            numberMinValue = null;
            numberMaxValue = null;
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
     * @param channelTypes The channel types.
     * @param integerMinValue The {@link SlashCommandOptionType#INTEGER} min value
     * @param integerMaxValue The {@link SlashCommandOptionType#INTEGER} max value
     * @param numberMinValue The {@link SlashCommandOptionType#NUMBER} min value
     * @param numberMaxValue The {@link SlashCommandOptionType#NUMBER} max value
     */
    public SlashCommandOptionImpl(
            SlashCommandOptionType type,
            String name,
            String description,
            boolean required,
            List<SlashCommandOptionChoice> choices,
            List<SlashCommandOption> options,
            Set<ChannelType> channelTypes,
            Long integerMinValue,
            Long integerMaxValue,
            Double numberMinValue,
            Double numberMaxValue
    ) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.required = required;
        this.choices = choices;
        this.options = options;
        this.channelTypes = channelTypes;
        this.integerMinValue = integerMinValue;
        this.integerMaxValue = integerMaxValue;
        this.numberMinValue = numberMinValue;
        this.numberMaxValue = numberMaxValue;
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

    @Override
    public Set<ChannelType> getChannelTypes() {
        return Collections.unmodifiableSet(channelTypes);
    }

    @Override
    public Optional<Long> getIntegerMinValue() {
        return Optional.ofNullable(integerMinValue);
    }

    @Override
    public Optional<Long> getIntegerMaxValue() {
        return Optional.ofNullable(integerMaxValue);
    }

    @Override
    public Optional<Double> getNumberMinValue() {
        return Optional.ofNullable(numberMinValue);
    }

    @Override
    public Optional<Double> getNumberMaxValue() {
        return Optional.ofNullable(numberMaxValue);
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
        if (!channelTypes.isEmpty()) {
            ArrayNode jsonChannelTypes = node.putArray("channel_types");
            channelTypes.forEach(channelType -> jsonChannelTypes.add(channelType.getId()));
        }
        if (type == SlashCommandOptionType.INTEGER) {
            if (integerMinValue != null) {
                node.put("min_value", integerMinValue);
            }
            if (integerMaxValue != null) {
                node.put("max_value", integerMaxValue);
            }
        } else if (type == SlashCommandOptionType.NUMBER) {
            if (numberMinValue != null) {
                node.put("min_value", numberMinValue);
            }
            if (numberMaxValue != null) {
                node.put("max_value", numberMaxValue);
            }
        }
        return node;
    }
}

package org.javacord.core.interaction;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.component.ComponentType;
import org.javacord.api.entity.message.component.SelectMenuOption;
import org.javacord.api.interaction.SelectMenuInteraction;
import org.javacord.core.DiscordApiImpl;
import org.javacord.core.entity.message.component.SelectMenuOptionImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SelectMenuInteractionImpl extends MessageComponentInteractionImpl implements SelectMenuInteraction {
    private final List<SelectMenuOption> selectMenuOptions = new ArrayList<>();
    private final List<SelectMenuOption> chosenSelectMenuOption = new ArrayList<>();
    private String placeholder;
    private int minimumValues;
    private int maximumValues;

    /**
     * Class constructor.
     *
     * @param api      The api instance.
     * @param channel  The channel in which the interaction happened. Can be {@code null}.
     * @param jsonData The json data of the interaction.
     */
    public SelectMenuInteractionImpl(DiscordApiImpl api, TextChannel channel, JsonNode jsonData) {
        super(api, channel, jsonData);

        JsonNode messageObject = jsonData.get("message");
        JsonNode componentsObject = messageObject.get("components");

        JsonNode dataObject = jsonData.get("data");

        for (JsonNode actionRow : componentsObject) {
            for (JsonNode interaction : actionRow.get("components")) {
                if (interaction.get("custom_id").asText().equals(dataObject.get("custom_id").asText())) {
                    placeholder = interaction.has("placeholder") ? interaction.get("placeholder").asText() : null;
                    maximumValues = interaction.has("max_value") ? interaction.get("max_value").asInt() : 1;
                    minimumValues = interaction.has("min_value") ? interaction.get("min_values").asInt() : 1;
                    for (JsonNode optionObject : interaction.get("options")) {
                        selectMenuOptions.add(new SelectMenuOptionImpl(optionObject));
                    }
                }
            }
        }

        JsonNode valuesArray = dataObject.get("values");

        for (JsonNode jsonNode : valuesArray) {
            chosenSelectMenuOption.addAll(selectMenuOptions.stream()
                    .filter(option -> option.getValue().equals(jsonNode.asText())).collect(Collectors.toList()));
        }

    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.SELECT_MENU;
    }

    @Override
    public List<SelectMenuOption> getChosenOptions() {
        return chosenSelectMenuOption;
    }

    @Override
    public List<SelectMenuOption> getPossibleOptions() {
        return selectMenuOptions;
    }


    @Override
    public Optional<String> getPlaceholder() {
        return Optional.ofNullable(placeholder);
    }

    @Override
    public int getMinimumValues() {
        return minimumValues;
    }

    @Override
    public int getMaximumValues() {
        return maximumValues;
    }
}

package org.javacord.core.entity.message.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.message.component.ComponentType;
import org.javacord.api.entity.message.component.SelectMenu;
import org.javacord.api.entity.message.component.SelectMenuOption;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SelectMenuImpl extends ComponentImpl implements SelectMenu {

    private final List<SelectMenuOption> options;
    private final String placeholder;
    private final String customId;
    private final int minimumValues;
    private final int maximumValues;
    private final boolean isDisabled;

    /**
     * Creates a new select menu.
     *
     * @param data The json data of the select menu.
     */
    public SelectMenuImpl(JsonNode data) {
        super(ComponentType.SELECT_MENU);
        options = new ArrayList<>();

        this.customId = data.get("custom_id").asText();
        for (JsonNode optionData : data.get("options")) {
            options.add(new SelectMenuOptionImpl(optionData));
        }
        this.placeholder = data.has("placeholder") ? data.get("placeholder").asText() : null;
        this.minimumValues = data.has("min_values") ? data.get("min_values").asInt() : 1;
        this.maximumValues = data.has("max_values") ? data.get("max_values").asInt() : 1;
        this.isDisabled = data.has("disabled") && data.get("disabled").asBoolean();
    }

    /**
     * Creates a new select menu.
     *
     * @param selectMenuOptions The select menu's options.
     * @param placeholder The select menu's placeholder.
     * @param customId The select menu's custom ID.
     * @param minimumValues The select menu's minimum values.
     * @param maximumValues The select menu's maximum values.
     * @param isDisabled If the select menu should be disabled.
     */
    public SelectMenuImpl(List<SelectMenuOption> selectMenuOptions, String placeholder,
                          String customId, int minimumValues, int maximumValues, boolean isDisabled) {
        super(ComponentType.SELECT_MENU);
        this.options = selectMenuOptions;
        this.placeholder = placeholder;
        this.customId = customId;
        this.minimumValues = minimumValues;
        this.maximumValues = maximumValues;
        this.isDisabled = isDisabled;
    }

    @Override
    public Optional<String> getPlaceholder() {
        return Optional.ofNullable(placeholder);
    }

    @Override
    public String getCustomId() {
        return customId;
    }

    @Override
    public int getMinimumValues() {
        return minimumValues;
    }

    @Override
    public int getMaximumValues() {
        return maximumValues;
    }

    @Override
    public List<SelectMenuOption> getOptions() {
        return options;
    }

    @Override
    public boolean isDisabled() {
        return isDisabled;
    }

    /**
     * Gets the select menu as a {@link ObjectNode}. This is what is sent to Discord.
     *
     * @return The select menu as a ObjectNode.
     */
    ObjectNode toJsonNode() {
        ObjectNode object = JsonNodeFactory.instance.objectNode();
        return toJsonNode(object);
    }

    /**
     * Gets the select menu as a {@link ObjectNode}. This is what is sent to Discord.
     *
     * @param object The object, the data should be added to.
     * @return The select menu as a ObjectNode.
     */
    public ObjectNode toJsonNode(ObjectNode object) {
        object.put("type", ComponentType.SELECT_MENU.value());
        object.put("custom_id", this.customId);

        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();

        for (SelectMenuOption option: options) {
            arrayNode.add(((SelectMenuOptionImpl) option).toJson());
        }

        object.set("options", arrayNode);
        object.put("min_values", this.minimumValues);
        object.put("max_values", this.maximumValues);
        object.put("disabled", this.isDisabled);

        if (placeholder != null) {
            object.put("placeholder", this.placeholder);
        }

        return object;
    }
}

package org.javacord.core.entity.message.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.channel.ChannelType;
import org.javacord.api.entity.message.component.ComponentType;
import org.javacord.api.entity.message.component.SelectMenu;
import org.javacord.api.entity.message.component.SelectMenuOption;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class SelectMenuImpl extends ComponentImpl implements SelectMenu {

    private final List<SelectMenuOption> options;
    private final EnumSet<ChannelType> channelTypes;
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
        super(ComponentType.fromId(data.get("type").asInt()));
        options = new ArrayList<>();
        channelTypes = EnumSet.noneOf(ChannelType.class);

        this.customId = data.get("custom_id").asText();

        if (data.hasNonNull("options")) {
            for (JsonNode optionData : data.get("options")) {
                options.add(new SelectMenuOptionImpl(optionData));
            }
        }

        if (data.hasNonNull("channel_types")) {
            data.get("channel_types").forEach(channelType -> channelTypes.add(ChannelType.fromId(channelType.asInt())));
        }

        this.placeholder = data.has("placeholder") ? data.get("placeholder").asText() : null;
        this.minimumValues = data.has("min_values") ? data.get("min_values").asInt() : 1;
        this.maximumValues = data.has("max_values") ? data.get("max_values").asInt() : 1;
        this.isDisabled = data.has("disabled") && data.get("disabled").asBoolean();
    }

    /**
     * Creates a new select menu.
     *
     * @param type              The type of the select menu.
     * @param selectMenuOptions The select menu's options.
     * @param placeholder       The select menu's placeholder.
     * @param customId          The select menu's custom ID.
     * @param minimumValues     The select menu's minimum values.
     * @param maximumValues     The select menu's maximum values.
     * @param isDisabled        If the select menu should be disabled.
     * @param channelTypes      The channel types of the select menu.
     */
    public SelectMenuImpl(ComponentType type, List<SelectMenuOption> selectMenuOptions, String placeholder,
                          String customId, int minimumValues, int maximumValues, boolean isDisabled,
                          EnumSet<ChannelType> channelTypes) {
        super(type);
        this.options = selectMenuOptions;
        this.placeholder = placeholder;
        this.customId = customId;
        this.minimumValues = minimumValues;
        this.maximumValues = maximumValues;
        this.isDisabled = isDisabled;
        this.channelTypes = channelTypes;
    }

    @Override
    public EnumSet<ChannelType> getChannelTypes() {
        return EnumSet.copyOf(channelTypes);
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
        return Collections.unmodifiableList(options);
    }

    @Override
    public boolean isDisabled() {
        return isDisabled;
    }

    @Override
    public ObjectNode toJsonNode() {
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
        object.put("type", getType().value());
        object.put("custom_id", this.customId);

        if (getType() == ComponentType.SELECT_MENU_STRING) {
            ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
            options.forEach(option -> arrayNode.add(((SelectMenuOptionImpl) option).toJson()));
            object.set("options", arrayNode);
        }

        if (getType() == ComponentType.SELECT_MENU_CHANNEL) {
            ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
            channelTypes.forEach(channelType -> arrayNode.add(channelType.getId()));
            object.set("channel_types", arrayNode);
        }

        object.put("min_values", this.minimumValues);
        object.put("max_values", this.maximumValues);
        object.put("disabled", this.isDisabled);

        if (placeholder != null) {
            object.put("placeholder", this.placeholder);
        }

        return object;
    }
}

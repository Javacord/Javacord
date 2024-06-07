package org.javacord.core.entity.message.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.message.component.ComponentType;
import org.javacord.api.entity.message.component.TextInput;
import org.javacord.api.entity.message.component.TextInputStyle;

import java.util.Optional;

/**
 * The implementation of {@link TextInput}.
 */
public class TextInputImpl extends ComponentImpl implements TextInput {

    private TextInputStyle style;

    private String label;

    private String customId;

    private String value;

    private String placeholder;

    private boolean required;

    private Integer minimumLength;

    private Integer maximumLength;

    /**
     * Creates a new text input.
     *
     * @param data The json data of the text input.
     */
    public TextInputImpl(JsonNode data) {
        super(ComponentType.TEXT_INPUT);
        this.customId = data.get("custom_id").asText();
        this.value = data.get("value").asText();
        this.label = data.has("label") ? data.get("label").asText() : null;
        this.style = data.has("style") ? TextInputStyle.fromId(data.get("style").asInt()) : null;
        this.required = data.has("required") && data.get("required").asBoolean();
        this.placeholder = data.has("placeholder") ? data.get("placeholder").asText() : null;
        this.minimumLength = data.has("min_length") ? data.get("min_length").asInt() : null;
        this.maximumLength = data.has("max_length") ? data.get("max_length").asInt() : null;
    }

    /**
     * Creates a new Text Input.
     *
     * @param style         The text input's style.
     * @param label         The text input's label.
     * @param customId      The text input's custom ID.
     * @param value         The text input's value.
     * @param placeholder   The text input's placeholder.
     * @param required      Whether the text input is a required component.
     * @param minimumLength The text input's minimum length.
     * @param maximumLength The text input's maximum length.
     */
    public TextInputImpl(TextInputStyle style, String label, String customId, String value,
                         String placeholder, boolean required, Integer minimumLength, Integer maximumLength) {
        super(ComponentType.TEXT_INPUT);
        this.style = style;
        this.label = label;
        this.customId = customId;
        this.value = value;
        this.placeholder = placeholder;
        this.required = required;
        this.minimumLength = minimumLength;
        this.maximumLength = maximumLength;
    }

    /**
     * Sets the style of the text input.
     *
     * @param style The style of the text input.
     */
    void setStyle(TextInputStyle style) {
        this.style = style;
    }

    /**
     * Sets the custom id of the text input.
     *
     * @param customId The custom id of the text input.
     */
    void setCustomId(String customId) {
        this.customId = customId;
    }

    /**
     * Sets the label of the text input.
     *
     * @param label The label of the text input.
     */
    void setLabel(String label) {
        this.label = label;
    }

    /**
     * Sets the minimum length of the text input.
     *
     * @param minimumLength The minimum length of the text input.
     */
    void setMinimumLength(int minimumLength) {
        this.minimumLength = minimumLength;
    }

    /**
     * Sets the maximum length of the text input.
     *
     * @param maximumLength The maximum length of the text input.
     */
    void setMaximumLength(int maximumLength) {
        this.maximumLength = maximumLength;
    }

    /**
     * Sets whether the text input is required.
     *
     * @param required Whether the text input is required.
     */
    void setRequired(boolean required) {
        this.required = required;
    }

    /**
     * Sets the value of the text input.
     *
     * @param value The value of the text input.
     */
    void setValue(String value) {
        this.value = value;
    }

    /**
     * Sets the placeholder of the text input.
     *
     * @param placeholder The placeholder of the text input.
     */
    void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    public Optional<TextInputStyle> getStyle() {
        return Optional.ofNullable(style);
    }

    @Override
    public String getCustomId() {
        return customId;
    }

    @Override
    public Optional<String> getLabel() {
        return Optional.ofNullable(label);
    }

    @Override
    public Optional<Integer> getMinimumLength() {
        return Optional.ofNullable(minimumLength);
    }

    @Override
    public Optional<Integer> getMaximumLength() {
        return Optional.ofNullable(maximumLength);
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public Optional<String> getPlaceholder() {
        return Optional.ofNullable(placeholder);
    }

    @Override
    public ObjectNode toJsonNode() {
        ObjectNode object = JsonNodeFactory.instance.objectNode();
        return toJsonNode(object);
    }

    /**
     * Gets the text input as a {@link ObjectNode}. This is what is sent to Discord.
     *
     * @param object The object, the data should be added to.
     * @return The text input as a ObjectNode.
     */
    public ObjectNode toJsonNode(ObjectNode object) {
        object.put("type", ComponentType.TEXT_INPUT.value());
        object.put("custom_id", customId);
        object.put("style", style.getValue());
        object.put("required", required);

        if (label != null && !label.isEmpty()) {
            object.put("label", label);
        }

        if (!value.isEmpty()) {
            object.put("value", value);
        }

        if (placeholder != null && !placeholder.isEmpty()) {
            object.put("placeholder", placeholder);
        }

        if (minimumLength != null) {
            object.put("min_length", minimumLength);
        }

        if (maximumLength != null) {
            object.put("max_length", maximumLength);
        }

        return object;
    }
}

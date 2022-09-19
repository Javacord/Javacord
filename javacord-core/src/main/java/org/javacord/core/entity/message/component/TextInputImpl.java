package org.javacord.core.entity.message.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.message.component.ComponentType;
import org.javacord.api.entity.message.component.TextInput;
import org.javacord.api.entity.message.component.TextInputStyle;
import java.util.Optional;

public class TextInputImpl extends ComponentImpl implements TextInput {

    private final TextInputStyle style;

    private final String label;

    private final String customId;

    private final String value;

    private final String placeholder;

    private final boolean required;

    private final Integer minimumLength;

    private final Integer maximumLength;

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

package org.javacord.core.entity.message.component;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.message.component.ButtonBuilderDelegate;
import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.message.component.ComponentType;

public class ButtonBuilderDelegateImpl implements ButtonBuilderDelegate  {
    private final ComponentType type = ComponentType.Button;

    private ButtonStyle style = null;

    private String label = null;

    private String customID = null;

    private String url = null;

    private Boolean disabled = null;

    public ComponentType getType() {
        return type;
    }

    @Override
    public ButtonBuilderDelegateImpl setStyle(ButtonStyle style) {
        this.style = style;
        return this;
    }

    @Override
    public ButtonBuilderDelegateImpl setStyle(String styleName) {
        this.style = ButtonStyle.devalue(styleName);
        return this;
    }

    @Override
    public ButtonBuilderDelegateImpl setStyle(int color) {
        this.style = ButtonStyle.devalue(color);
        return this;
    }

    @Override
    public ButtonBuilderDelegateImpl setLabel(String label) {
        this.label = label;
        return this;
    }

    @Override
    public ButtonBuilderDelegateImpl setCustomID(String customID) {
        this.customID = customID;
        return this;
    }

    @Override
    public ButtonBuilderDelegateImpl setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public ButtonBuilderDelegateImpl setDisabled(Boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    public ObjectNode toJsonNode() {
        ObjectNode object = JsonNodeFactory.instance.objectNode();
        return toJsonNode(object);
    }

    public ObjectNode toJsonNode(ObjectNode object) {
        object.put("type", type.value());

        if (style != null) {
            object.put("style", style.value());
        }

        if (label != null && !label.equals("")) {
            object.put("label", label);
        }

        if (customID != null && !customID.equals("")) {
            object.put("custom_id", customID);
        }

        if (url != null && !url.equals("")) {
            object.put("url", url);
        }

        if (disabled != null) {
            object.put("disabled", disabled);
        }

        return object;
    }
}

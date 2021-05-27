package org.javacord.core.entity.message.component;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.message.component.ComponentType;

import java.util.Optional;

public class ButtonImpl extends ComponentImpl implements Button {
    private final ButtonStyle style;

    private final String label;

    private final String customID;

    private final String url;

    private final Boolean disabled;

    public ButtonImpl(JsonNode data) {
        super(ComponentType.Button);
        this.style = data.has("style") ? ButtonStyle.devalue(data.get("style").asInt()) : null;
        this.label = data.has("label") ? data.get("label").asText() : null;
        this.customID = data.has("customID") ? data.get("custom_id").asText() : null;
        this.url = data.has("url") ? data.get("url").asText() : null;
        this.disabled = data.has("disabled") ? data.get("disabled").asBoolean() : null;
    }

    @Override
    public ButtonStyle getStyle() { return style; }

    @Override
    public Optional<String> getCustomID() { return Optional.ofNullable(customID); }

    @Override
    public Optional<String> getLabel() { return Optional.ofNullable(label); }

    @Override
    public Optional<String> getUrl() { return Optional.ofNullable(url); }

    @Override
    public Optional<Boolean> isDisabled() { return Optional.ofNullable(disabled); }

    @Override
    public Optional<Emoji> getEmoji() {
        return Optional.empty();
    }
}

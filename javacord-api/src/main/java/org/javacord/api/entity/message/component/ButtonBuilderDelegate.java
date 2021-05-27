package org.javacord.api.entity.message.component;

public interface ButtonBuilderDelegate {
    ComponentType getType();

    ButtonBuilderDelegate setStyle(ButtonStyle style);

    ButtonBuilderDelegate setStyle(String styleName);

    ButtonBuilderDelegate setStyle(int color);

    ButtonBuilderDelegate setLabel(String label);

    ButtonBuilderDelegate setCustomID(String customID);

    ButtonBuilderDelegate setUrl(String url);

    ButtonBuilderDelegate setDisabled(Boolean disabled);
}

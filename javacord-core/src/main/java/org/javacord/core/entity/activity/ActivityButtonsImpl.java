package org.javacord.core.entity.activity;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.activity.ActivityButtons;

public class ActivityButtonsImpl implements ActivityButtons {
    private final String label;

    private final String url;

    /**
     * Creates a new activity buttons object.
     *
     * @param data The json data of the buttons.
     */
    public ActivityButtonsImpl(JsonNode data) {

        this.label = data.get("label").asText();

        this.url = data.get("url").asText();
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getUrl() {
        return url;
    }
}

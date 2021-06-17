package org.javacord.core.entity.message.component;

import com.fasterxml.jackson.databind.JsonNode;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.component.ComponentType;
import org.javacord.api.entity.message.component.LowLevelComponent;

import java.util.ArrayList;
import java.util.List;

public class ActionRowImpl extends ComponentImpl implements ActionRow {
    private final List<LowLevelComponent> components;

    /**
     * Create a new Action Row object.
     *
     * @param data The json data of the component.
     */
    public ActionRowImpl(JsonNode data) {
        super(ComponentType.ACTION_ROW);

        this.components = new ArrayList<>();
        if (data.has("components")) {
            for (JsonNode componentJson : data.get("components")) {
                int typeInt = componentJson.get("type").asInt();
                ComponentType type = ComponentType.fromId(typeInt);

                switch (type) {
                    case ACTION_ROW:
                        throw new IllegalStateException("An action row is inside of an action row.");
                    case BUTTON:
                        Button button = new ButtonImpl(componentJson);
                        components.add(button);
                        break;
                    default:
                        throw new IllegalStateException(
                                String.format(
                                        "Couldn't parse the component of type '%d'. Please contact the developer!",
                                        typeInt
                                )
                        );
                }
            }
        }
    }

    /**
     * Create a new Action Row object.
     *
     * @param data Starting list of components.
     */
    public ActionRowImpl(List<LowLevelComponent> data) {
        super(ComponentType.ACTION_ROW);

        this.components = data;
    }

    /**
     * Get the components of the action row.
     *
     * @return A list which contains components.
     */
    @Override
    public List<LowLevelComponent> getComponents() {
        return components;
    }
}

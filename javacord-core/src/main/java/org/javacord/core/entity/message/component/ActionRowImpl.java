package org.javacord.core.entity.message.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
     * Gets the ActionRow as a {@link ObjectNode}. This is what is sent to Discord.
     *
     * @return The button as a ObjectNode.
     */
    public ObjectNode toJsonNode() {
        ObjectNode object = JsonNodeFactory.instance.objectNode();
        return toJsonNode(object);
    }

    /**
     * Gets the ActionRow as a {@link ObjectNode}. This is what is sent to Discord.
     *
     * @param object The object, the data should be added to.
     * @return The button as a ObjectNode.
     * @throws IllegalStateException if the ActionRowBuilder has an ActionRow component
     */
    public ObjectNode toJsonNode(ObjectNode object) throws IllegalStateException {
        object.put("type", ComponentType.ACTION_ROW.value());

        if (components.size() == 0) {
            object.putArray("components");
            return object;
        }

        ArrayNode componentsJson = JsonNodeFactory.instance.objectNode().arrayNode();
        for (LowLevelComponent component : this.components) {
            switch (component.getType()) {
                case ACTION_ROW:
                    throw new IllegalStateException("An action row can not contain an action row.");
                case BUTTON:
                    ButtonImpl button = (ButtonImpl) component;
                    componentsJson.add(button.toJsonNode());
                    break;
                default:
                    throw new IllegalStateException("An unknown component type was added.");
            }
        }
        object.set("components", componentsJson);

        return object;
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

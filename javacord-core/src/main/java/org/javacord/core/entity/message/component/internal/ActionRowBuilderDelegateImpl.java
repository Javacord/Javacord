package org.javacord.core.entity.message.component.internal;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.component.ButtonBuilder;
import org.javacord.api.entity.message.component.ComponentBuilder;
import org.javacord.api.entity.message.component.ComponentType;
import org.javacord.api.entity.message.component.LowLevelComponentBuilder;
import org.javacord.api.entity.message.component.internal.ActionRowBuilderDelegate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActionRowBuilderDelegateImpl implements ActionRowBuilderDelegate {
    private final ComponentType type = ComponentType.ACTION_ROW;

    private final List<LowLevelComponentBuilder> components = new ArrayList<>();

    @Override
    public void addComponents(LowLevelComponentBuilder... components) {
        this.components.addAll(Arrays.asList(components));
    }

    @Override
    public void copy(ActionRow actionRow) {
        actionRow.getComponents().forEach(component -> {
            if (component.getType() == ComponentType.BUTTON) {
                ButtonBuilder builder = new ButtonBuilder();
                builder.copy((Button) component);
                this.addComponents(builder);
            }
        });
    }

    @Override
    public void removeComponent(LowLevelComponentBuilder component) {
        components.remove(component);
    }

    @Override
    public void removeComponent(int index) {
        components.remove(index);
    }

    @Override
    public void removeComponent(String customId) {
        components.removeIf(componentBuilder -> {
            if (componentBuilder.getType() == ComponentType.BUTTON) {
                ButtonBuilder buttonBuilder = (ButtonBuilder) componentBuilder;
                return buttonBuilder.getDelegate().getCustomId().equals(customId);
            }
            return false;
        });
    }

    @Override
    public List<LowLevelComponentBuilder> getComponents() {
        return this.components;
    }

    /**
     * Gets the button as a {@link ObjectNode}. This is what is sent to Discord.
     *
     * @return The button as a ObjectNode.
     */
    public ObjectNode toJsonNode() {
        ObjectNode object = JsonNodeFactory.instance.objectNode();
        return toJsonNode(object);
    }

    /**
     * Gets the button as a {@link ObjectNode}. This is what is sent to Discord.
     *
     * @param object The object, the data should be added to.
     * @return The button as a ObjectNode.
     * @throws IllegalStateException if the ActionRowBuilder has an ActionRow component
     */
    public ObjectNode toJsonNode(ObjectNode object) throws IllegalStateException {
        object.put("type", this.type.value());

        if (components.size() == 0) {
            object.putArray("components");
            return object;
        }

        ArrayNode componentsJson = JsonNodeFactory.instance.objectNode().arrayNode();
        for (ComponentBuilder component : this.components) {
            switch (component.getType()) {
                case ACTION_ROW:
                    throw new IllegalStateException("An action row can not contain an action row.");
                case BUTTON:
                    ButtonBuilderDelegateImpl button = (ButtonBuilderDelegateImpl) component.getDelegate();
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
     * Get the component's type (always {@link ComponentType#ACTION_ROW}.
     *
     * @return The component's type.
     */
    public ComponentType getType() {
        return this.type;
    }
}

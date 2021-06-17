package org.javacord.core.entity.message.component.internal;

import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.component.ButtonBuilder;
import org.javacord.api.entity.message.component.ComponentType;
import org.javacord.api.entity.message.component.LowLevelComponent;
import org.javacord.api.entity.message.component.internal.ActionRowBuilderDelegate;
import org.javacord.core.entity.message.component.ActionRowImpl;
import java.util.ArrayList;
import java.util.List;

public class ActionRowBuilderDelegateImpl implements ActionRowBuilderDelegate {
    private final ComponentType type = ComponentType.ACTION_ROW;

    private final List<LowLevelComponent> components = new ArrayList<>();

    @Override
    public void addComponents(List<LowLevelComponent> components) {
        this.components.addAll(components);
    }

    @Override
    public void copy(ActionRow actionRow) {
        actionRow.getComponents().forEach(component -> {
            if (component.getType() == ComponentType.BUTTON) {
                ButtonBuilder builder = new ButtonBuilder();
                builder.copy((Button) component);
                components.add(builder.build());
            }
        });
    }

    @Override
    public void removeComponent(LowLevelComponent component) {
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
    public List<LowLevelComponent> getComponents() {
        return this.components;
    }

    @Override
    public ActionRow build() {
        return new ActionRowImpl(components);
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

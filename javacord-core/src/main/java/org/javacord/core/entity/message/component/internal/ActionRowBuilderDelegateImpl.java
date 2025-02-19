package org.javacord.core.entity.message.component.internal;

import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.component.ButtonBuilder;
import org.javacord.api.entity.message.component.ComponentType;
import org.javacord.api.entity.message.component.LowLevelComponent;
import org.javacord.api.entity.message.component.SelectMenu;
import org.javacord.api.entity.message.component.SelectMenuBuilder;
import org.javacord.api.entity.message.component.internal.ActionRowBuilderDelegate;
import org.javacord.core.entity.message.component.ActionRowImpl;
import org.javacord.core.entity.message.component.ButtonImpl;
import org.javacord.core.entity.message.component.EditableButtonImpl;
import org.javacord.core.entity.message.component.EditableSelectMenuImpl;
import org.javacord.core.entity.message.component.EditableTextInputImpl;
import org.javacord.core.entity.message.component.SelectMenuImpl;
import org.javacord.core.entity.message.component.TextInputImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * The implementation of {@link ActionRowBuilderDelegate}.
 */
public class ActionRowBuilderDelegateImpl implements ActionRowBuilderDelegate {
    private final ComponentType type = ComponentType.ACTION_ROW;

    private final List<LowLevelComponent> components = new ArrayList<>();

    @Override
    public void addComponents(List<LowLevelComponent> components) {
        this.components.addAll(components);
    }

    @Override
    public void updateComponents(Predicate<LowLevelComponent> predicate, Consumer<LowLevelComponent> updater) {
        components.stream()
                .filter(predicate)
                .map(lowLevelComponent -> {
                    if (lowLevelComponent.isButton()) {
                        return new EditableButtonImpl((ButtonImpl) lowLevelComponent);
                    } else if (lowLevelComponent.isSelectMenu()) {
                        return new EditableSelectMenuImpl((SelectMenuImpl) lowLevelComponent);
                    } else if (lowLevelComponent.isTextInput()) {
                        return new EditableTextInputImpl((TextInputImpl) lowLevelComponent);
                    } else {
                        throw new IllegalStateException("Unknown low-level component type");
                    }
                })
                .forEach(lowLevelComponent -> {
                    if (lowLevelComponent instanceof EditableButtonImpl) {
                        updater.andThen(button -> ((EditableButtonImpl) button).clearDelegate())
                                .accept(lowLevelComponent);
                    } else if (lowLevelComponent instanceof EditableSelectMenuImpl) {
                        updater.andThen(selectMenu -> ((EditableSelectMenuImpl) selectMenu).clearDelegate())
                                .accept(lowLevelComponent);
                    } else {
                        updater.andThen(textInput -> ((EditableTextInputImpl) textInput).clearDelegate())
                                .accept(lowLevelComponent);
                    }
                });
    }

    @Override
    public void copy(ActionRow actionRow) {
        actionRow.getComponents().forEach(component -> {
            if (component.getType() == ComponentType.BUTTON) {
                ButtonBuilder builder = new ButtonBuilder();
                builder.copy((Button) component);
                components.add(builder.build());
            } else if (component.getType().isSelectMenuType()) {
                SelectMenu sm = (SelectMenu) component;
                SelectMenuBuilder builder = new SelectMenuBuilder(sm.getType(), sm.getCustomId());
                builder.copy(sm);
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
            } else if (componentBuilder.getType().isSelectMenuType()) {
                SelectMenuBuilder selectMenuBuilder = (SelectMenuBuilder) componentBuilder;
                return selectMenuBuilder.getDelegate().getCustomId().equals(customId);
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
     * Get the component's type (always {@link ComponentType#ACTION_ROW}).
     *
     * @return The component's type.
     */
    public ComponentType getType() {
        return this.type;
    }
}

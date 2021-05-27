package org.javacord.api.entity.message.component.internal;

import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.ComponentType;
import org.javacord.api.entity.message.component.LowLevelComponentBuilder;

import java.util.List;

public interface ActionRowBuilderDelegate extends ComponentBuilderDelegate {
    /**
     * Add multiple low-level component builders to the ActionRow.
     *
     * @param components Any low-level ComponentBuilder.
     */
    void addComponents(LowLevelComponentBuilder... components);

    /**
     * Copy an action row's values into the builder.
     *
     * @param actionRow The action row to copy.
     */
    void copy(ActionRow actionRow);

    /**
     * Remove a low-level component from the ActionRow.
     *
     * @param component The low-level component being removed.
     */
    void removeComponent(LowLevelComponentBuilder component);

    /**
     * Remove a low-level component from the ActionRow.
     *
     * @param index The index placement to remove.
     */
    void removeComponent(int index);

    /**
     * Remove a low-level component from the ActionRow.
     *
     * @param customId The low-level component's identifier.
     */
    void removeComponent(String customId);

    /**
     * Get current low-level components.
     *
     * @return The current low-level components.
     */
    List<LowLevelComponentBuilder> getComponents();

    /**
     * Get the component's type (always {@link ComponentType#ACTION_ROW}.
     *
     * @return The component's type.
     */
    ComponentType getType();
}

package org.javacord.api.entity.message.component.internal;

import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.ActionRowBuilder;
import org.javacord.api.entity.message.component.ComponentType;
import org.javacord.api.entity.message.component.LowLevelComponent;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * This class is internally used by the {@link ActionRowBuilder} to created action rows.
 * You usually don't want to interact with this object.
 */
public interface ActionRowBuilderDelegate extends ComponentBuilderDelegate {
    /**
     * Add multiple low-level components to the ActionRow.
     *
     * @param components A list containing low-level components.
     */
    void addComponents(List<LowLevelComponent> components);

    /**
     * Updates all components of an action row that satisfy the given predicate using the given updater.
     *
     * @param predicate The predicate that components have to satisfy to get updated.
     * @param updater   The updater for the components.
     */
    void updateComponents(Predicate<LowLevelComponent> predicate, Consumer<LowLevelComponent> updater);

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
    void removeComponent(LowLevelComponent component);

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
    List<LowLevelComponent> getComponents();

    /**
     * Creates an {@link ActionRow} instance with the given values.
     *
     * @return The created action row instance.
     */
    ActionRow build();

    /**
     * Get the component's type (always {@link ComponentType#ACTION_ROW}).
     *
     * @return The component's type.
     */
    ComponentType getType();
}

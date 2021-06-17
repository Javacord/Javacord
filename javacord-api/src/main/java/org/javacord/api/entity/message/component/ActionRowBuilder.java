package org.javacord.api.entity.message.component;

import org.javacord.api.entity.message.component.internal.ActionRowBuilderDelegate;
import org.javacord.api.util.internal.DelegateFactory;

import java.util.Arrays;
import java.util.List;

public class ActionRowBuilder implements HighLevelComponentBuilder {
    private final ActionRowBuilderDelegate delegate = DelegateFactory.createActionRowBuilderDelegate();

    /**
     * Add multiple low-level component builders.
     *
     * @param components The low-level components.
     * @return The builder instance to chain methods.
     */
    public ActionRowBuilder addComponents(LowLevelComponent... components) {
        return addComponents(Arrays.asList(components));
    }

    /**
     * Add a list containing low-level components builders.
     *
     * @param components The list containing low-level components.
     * @return The builder instance to chain methods.
     */
    public ActionRowBuilder addComponents(List<LowLevelComponent> components) {
        delegate.addComponents(components);
        return this;
    }

    /**
     * Copy an action row into this action row builder's values.
     *
     * @param actionRow The action row to copy.
     * @return The builder instance to chain methods.
     */
    public ActionRowBuilder copy(ActionRow actionRow) {
        delegate.copy(actionRow);
        return this;
    }

    /**
     * Remove a low-level component from the ActionRow.
     *
     * @param component The low-level component being removed.
     * @return The builder instance to chain methods.
     */
    public ActionRowBuilder removeComponent(LowLevelComponent component) {
        delegate.removeComponent(component);
        return this;
    }

    /**
     * Remove a low-level component from the ActionRow.
     *
     * @param index The index placement to remove.
     * @return The builder instance to chain methods.
     */
    public ActionRowBuilder removeComponent(int index) {
        delegate.removeComponent(index);
        return this;
    }

    /**
     * Remove a low-level component from the ActionRow.
     *
     * @param customId The low-level component's identifier.
     * @return The builder instance to chain methods.
     */
    public ActionRowBuilder removeComponent(String customId) {
        delegate.removeComponent(customId);
        return this;
    }

    /**
     * Get the low-level components of this action row.
     *
     * @return A list of components.
     */
    public List<LowLevelComponent> getComponents() {
        return delegate.getComponents();
    }

    /**
     * Get the type of this component (always {@link ComponentType#ACTION_ROW}).
     *
     * @return The component type.
     */
    public ComponentType getType() {
        return delegate.getType();
    }

    /**
     * Creates a {@link ActionRow} instance with the given values.
     *
     * @return The created action row instance.
     */
    public ActionRow build() {
        return delegate.build();
    }

    /**
     * Gets the delegate used by the component builder internally.
     *
     * @return The delegate used by this component builder internally.
     */
    public ActionRowBuilderDelegate getDelegate() {
        return delegate;
    }
}

package org.javacord.api.entity.message.component;

import org.javacord.api.entity.message.component.internal.SelectMenuBuilderDelegate;
import org.javacord.api.util.internal.DelegateFactory;

import java.util.List;

public class SelectMenuBuilder implements LowLevelComponentBuilder {
    private final SelectMenuBuilderDelegate delegate = DelegateFactory.createSelectMenuBuilderDelegate();

    @Override
    public ComponentType getType() {
        return delegate.getType();
    }

    @Override
    public SelectMenuBuilderDelegate getDelegate() {
        return delegate;
    }

    /**
     * Set the placeholder for the select menu.
     *
     * @param placeholder The placeholder.
     * @return The builder.
     */
    public SelectMenuBuilder setPlaceholder(String placeholder) {
        delegate.setPlaceholder(placeholder);
        return this;
    }

    /**
     * Set the minimum amount of options which must be selected.
     *
     * @param minimumValues The minimum values.
     * @return The builder.
     */
    public SelectMenuBuilder setMinimumValues(int minimumValues) {
        delegate.setMinimumValues(minimumValues);
        return this;
    }

    /**
     * Set the maximum amount of options which can be selected.
     *
     * @param maximumValues The maximum values.
     * @return The builder.
     */
    public SelectMenuBuilder setMaximumValues(int maximumValues) {
        delegate.setMaximumValues(maximumValues);
        return this;
    }

    /**
     * Set the custom ID for the select menu.
     *
     * @param customId The custom ID.
     * @return The builder.
     */
    public SelectMenuBuilder setCustomId(String customId) {
        delegate.setCustomId(customId);
        return this;
    }

    /**
     * Add an option to the select menu.
     *
     * @param selectMenuOption The option.
     * @return The builder.
     */
    public SelectMenuBuilder addOption(SelectMenuOption selectMenuOption) {
        delegate.addOption(selectMenuOption);
        return this;
    }

    /**
     * Remove an option from the select menu.
     *
     * @param selectMenuOption The option.
     * @return The builder.
     */
    public SelectMenuBuilder removeOption(SelectMenuOption selectMenuOption) {
        delegate.removeOption(selectMenuOption);
        return this;
    }

    /**
     * Adds all given options to the select menu.
     *
     * @param selectMenuOptions The options.
     * @return The builder.
     */
    public SelectMenuBuilder addOptions(List<SelectMenuOption> selectMenuOptions) {
        selectMenuOptions.forEach(delegate::addOption);
        return this;
    }

    /**
     * Removes all options from the select menu.
     *
     * @return The builder.
     */
    public SelectMenuBuilder removeAllOptions() {
        delegate.removeAllOptions();
        return this;
    }

    /**
     * Set if the select menu should be disabled.
     *
     * @param isDisabled Is disabled.
     * @return The builder.
     */
    public SelectMenuBuilder setDisabled(boolean isDisabled) {
        delegate.setDisabled(isDisabled);
        return this;
    }

    /**
     * Create a copy of an existing select menu.
     *
     * @param selectMenu The select menu to copy.
     * @return The select menu builder.
     */
    public SelectMenuBuilder copy(SelectMenu selectMenu) {
        delegate.copy(selectMenu);
        return this;
    }

    /**
     * Creates a {@link SelectMenu} instance with the given values.
     *
     * @return The created select menu instance.
     */
    public SelectMenu build() {
        return delegate.build();
    }
}

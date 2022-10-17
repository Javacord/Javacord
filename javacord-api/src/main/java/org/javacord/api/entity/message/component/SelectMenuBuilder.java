package org.javacord.api.entity.message.component;

import org.javacord.api.entity.channel.ChannelType;
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
     * Create a new SelectMenuBuilder.
     *
     * @param type     The type of SelectMenu to create.
     * @param customId The custom id of the SelectMenu.
     */
    public SelectMenuBuilder(ComponentType type, String customId) {
        if (!type.isSelectMenuType()) {
            throw new IllegalArgumentException("Invalid SelectMenu type.");
        }
        delegate.setType(type);
        delegate.setCustomId(customId);
    }

    private SelectMenuBuilder() {

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
     * Add a channel type to the select menu.
     * 
     * <p>Only usable with {@link ComponentType#SELECT_MENU_CHANNEL}.
     *
     * @param channelType The channel type to add.
     * @return The builder.
     */
    public SelectMenuBuilder addChannelType(ChannelType channelType) {
        delegate.addChannelType(channelType);
        return this;
    }

    /**
     * Adds all given channel types to the select menu.
     * 
     * <p>Only usable with {@link ComponentType#SELECT_MENU_CHANNEL}.
     *
     * @param channelTypes The channel types to add.
     * @return The builder.
     */
    public SelectMenuBuilder addChannelTypes(Iterable<ChannelType> channelTypes) {
        channelTypes.forEach(delegate::addChannelType);
        return this;
    }

    /**
     * Add an option to the select menu.
     * 
     * <p>Only usable with {@link ComponentType#SELECT_MENU_STRING}.
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
     * <p>Only usable with {@link ComponentType#SELECT_MENU_STRING}.
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
     * <p>Only usable with {@link ComponentType#SELECT_MENU_STRING}.
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
     * <p>Only usable with {@link ComponentType#SELECT_MENU_STRING}.
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

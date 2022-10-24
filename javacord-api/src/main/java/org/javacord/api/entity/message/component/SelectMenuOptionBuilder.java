package org.javacord.api.entity.message.component;

import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.message.component.internal.SelectMenuOptionBuilderDelegate;
import org.javacord.api.util.internal.DelegateFactory;

public class SelectMenuOptionBuilder {
    private final SelectMenuOptionBuilderDelegate delegate = DelegateFactory.createSelectMenuOptionBuilderDelegate();

    /**
     * Set the label for the select menu option.
     *
     * @param label The label.
     * @return The builder.
     */
    public SelectMenuOptionBuilder setLabel(String label) {
        delegate.setLabel(label);
        return this;
    }

    /**
     * Set the value for the select menu option.
     *
     * @param value The value.
     * @return The builder.
     */
    public SelectMenuOptionBuilder setValue(String value) {
        delegate.setValue(value);
        return this;
    }

    /**
     * Set the description for the select menu option.
     *
     * @param description The description.
     * @return The builder.
     */
    public SelectMenuOptionBuilder setDescription(String description) {
        delegate.setDescription(description);
        return this;
    }

    /**
     * Sets the emoji for the select menu option.
     *
     * @param unicode The emoji as a unicode string
     * @return The builder.
     */
    public SelectMenuOptionBuilder setEmoji(String unicode) {
        delegate.setEmoji(unicode);
        return this;
    }

    /**
     * Set the emoji for the select menu option.
     *
     * @param emoji The emoji.
     * @return The builder.
     */
    public SelectMenuOptionBuilder setEmoji(Emoji emoji) {
        delegate.setEmoji(emoji);
        return this;
    }

    /**
     * Set the option to default for the menu.
     *
     * @param isDefault If it is default.
     * @return The builder.
     */
    public SelectMenuOptionBuilder setDefault(boolean isDefault) {
        delegate.setDefault(isDefault);
        return this;
    }

    /**
     * Creates a {@link SelectMenuOption} instance with the given values.
     *
     * @return The created select menu option instance.
     */
    public SelectMenuOption build() {
        return delegate.build();
    }
}

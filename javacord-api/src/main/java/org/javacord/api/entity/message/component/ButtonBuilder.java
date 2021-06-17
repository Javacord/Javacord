package org.javacord.api.entity.message.component;

import org.javacord.api.entity.emoji.CustomEmoji;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.message.component.internal.ButtonBuilderDelegate;
import org.javacord.api.util.internal.DelegateFactory;

public class ButtonBuilder implements LowLevelComponentBuilder {
    private final ButtonBuilderDelegate delegate = DelegateFactory.createButtonBuilderDelegate();

    /**
     * Copy a button's value into this builder.
     *
     * @param button The button to copy.
     * @return The builder instance to chain methods.
     */
    public ButtonBuilder copy(Button button) {
        delegate.copy(button);
        return this;
    }

    /**
     * Get the component's type (always {@link ComponentType#BUTTON}.
     *
     * @return The component's type.
     */
    public ComponentType getType() {
        return delegate.getType();
    }

    /**
     * Add this button to a new action row and return the new action row.
     * This is useful if you only want one button and quickly wrap it into an ActionRow to be used with a message.
     *
     * @return The new ActionRowBuilder with this button added to it.
     */
    public ActionRowBuilder inActionRow() {
        return new ActionRowBuilder()
            .addComponents(build());
    }

    /**
     * Set the button's style.
     *
     * @param style A new button style.
     * @return The builder instance to chain methods.
     */
    public ButtonBuilder setStyle(ButtonStyle style) {
        delegate.setStyle(style);
        return this;
    }

    /**
     * Set the button's style.
     *
     * @param styleName The style of the button based on Discord's default names.
     * @return The current instance in order to chain call methods.
     */
    public ButtonBuilder setStyle(String styleName) {
        ButtonStyle parsed = ButtonStyle.fromName(styleName);
        delegate.setStyle(parsed);
        return this;
    }

    /**
     * Set the button's style.
     *
     * @param styleValue The style of the button based on Discord's style values.
     * @return The current instance in order to chain call methods.
     */
    public ButtonBuilder setStyle(int styleValue) {
        ButtonStyle parsed = ButtonStyle.fromId(styleValue);
        delegate.setStyle(parsed);
        return this;
    }

    /**
     * Set the button's label.
     *
     * @param label The button's label.
     * @return The current instance in order to chain call methods.
     */
    public ButtonBuilder setLabel(String label) {
        delegate.setLabel(label);
        return this;
    }

    /**
     * Set the button's custom ID.
     *
     * @param customId The button's identifier.
     * @return The current instance in order to chain call methods.
     */
    public ButtonBuilder setCustomId(String customId) {
        delegate.setCustomId(customId);
        return this;
    }

    /**
     * Set the button's URL.
     *
     * @param url The button's clickable URL.
     * @return The current instance in order to chain call methods.
     */
    public ButtonBuilder setUrl(String url) {
        delegate.setUrl(url);
        return this;
    }

    /**
     * Set the button to disabled.
     *
     * @param isDisabled Whether the button is disabled or not.
     * @return The current instance in order to chain call methods.
     */
    public ButtonBuilder setDisabled(Boolean isDisabled) {
        delegate.setDisabled(isDisabled);
        return this;
    }

    /**
     * Set the button's emoji based on a unicode character.
     *
     * @param unicode The emoji unicode character.
     * @return The current instance in order to chaain call methods.
     */
    public ButtonBuilder setEmoji(String unicode) {
        delegate.setEmoji(unicode);
        return this;
    }

    /**
     * Set the button's emoji.
     *
     * @param emoji The emoji.
     * @return The current instance in order to chaain call methods.
     */
    public ButtonBuilder setEmoji(Emoji emoji) {
        delegate.setEmoji(emoji);
        return this;
    }

    /**
     * Set the button's emoji to a custom emoji.
     *
     * @param emoji The custom emoji.
     * @return The current instance in order to chaain call methods.
     */
    public ButtonBuilder setEmoji(CustomEmoji emoji) {
        delegate.setEmoji(emoji);
        return this;
    }

    /**
     * Set the button's style based on a color name (ignores case sensitivity).
     *
     * @param styleName A color name from Discord's selection.
     * @return The current instance in order to chain call methods.
     */
    public ButtonBuilder setStyleIgnoreCase(String styleName) {
        ButtonStyle parsed = ButtonStyle.fromName(styleName.toLowerCase());
        delegate.setStyle(parsed);
        return this;
    }

    /**
     * Creates a {@link Button} instance with the given values.
     *
     * @return The created button instance.
     */
    public Button build() {
        return delegate.build();
    }

    /**
     * Gets the delegate used by the component builder internally.
     *
     * @return The delegate used by this component builder internally.
     */
    @Override
    public ButtonBuilderDelegate getDelegate() {
        return delegate;
    }
}

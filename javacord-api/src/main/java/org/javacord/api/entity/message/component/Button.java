package org.javacord.api.entity.message.component;

import org.javacord.api.entity.emoji.Emoji;

import java.util.Optional;

/**
 * This interface represents a Button component.
 */
public interface Button extends LowLevelComponent {
    /**
     * Get the button's style.
     *
     * @return The button's style.
     */
    ButtonStyle getStyle();

    /**
     * Get the button's identifier.
     *
     * @return The button's custom identifier.
     */
    Optional<String> getCustomId();

    /**
     * Get the button's label.
     *
     * @return The button's label.
     */
    Optional<String> getLabel();

    /**
     * Get the button's clickable URL.
     *
     * @return The button's URL.
     */
    Optional<String> getUrl();

    /**
     * Get whether the button is disabled.
     *
     * @return Whether the button is disabled.
     */
    Optional<Boolean> isDisabled();

    /**
     * Get the emoji button's emoji.
     *
     * @return The emoji button's emoji.
     */
    Optional<Emoji> getEmoji();

    /**
     * Create a new button with the red danger style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label    The label for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button danger(String customId, String label) {
        return create(customId, ButtonStyle.DANGER, label, (Emoji) null, false);
    }

    /**
     * Create a new button with the red danger style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label    The label for this button.
     * @param disabled Whether the button is disabled.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button danger(String customId, String label, boolean disabled) {
        return create(customId, ButtonStyle.DANGER, label, (Emoji) null, disabled);
    }

    /**
     * Create a new button with the red danger style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label    The label for this button.
     * @param emoji    The emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button danger(String customId, String label, Emoji emoji) {
        return create(customId, ButtonStyle.DANGER, label, emoji, false);
    }

    /**
     * Create a new button with the red danger style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label    The label for this button.
     * @param emoji    The emoji for this button.
     * @param disabled Whether the button is disabled.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button danger(String customId, String label, Emoji emoji, boolean disabled) {
        return create(customId, ButtonStyle.DANGER, label, emoji, disabled);
    }

    /**
     * Create a new button with the red danger style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId     The custom id for this button.
     * @param label        The label for this button.
     * @param unicodeEmoji The emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button danger(String customId, String label, String unicodeEmoji) {
        return create(customId, ButtonStyle.DANGER, label, unicodeEmoji, false);
    }

    /**
     * Create a new button with the red danger style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId     The custom id for this button.
     * @param label        The label for this button.
     * @param unicodeEmoji The emoji for this button.
     * @param disabled     Whether the button is disabled.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button danger(String customId, String label, String unicodeEmoji, boolean disabled) {
        return create(customId, ButtonStyle.DANGER, label, unicodeEmoji, disabled);
    }

    /**
     * Create a new button with the red danger style and the given emoji.
     * If you want to use a unicode emoji, use {@link Button#danger(String, String, String)} instead
     * and pass {@code null} as your label.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param emoji    The emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button danger(String customId, Emoji emoji) {
        return create(customId, ButtonStyle.DANGER, null, emoji, false);
    }

    /**
     * Create a new button with the red danger style and the given emoji.
     * If you want to use a unicode emoji, use {@link Button#danger(String, String, String, boolean)} instead
     * and pass {@code null} as your label.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param emoji    The emoji for this button.
     * @param disabled Whether the button is disabled.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button danger(String customId, Emoji emoji, boolean disabled) {
        return create(customId, ButtonStyle.DANGER, null, emoji, disabled);
    }

    /**
     * Create a new button with the blurple primary style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label    The label for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button primary(String customId, String label) {
        return create(customId, ButtonStyle.PRIMARY, label, (Emoji) null, false);
    }

    /**
     * Create a new button with the blurple primary style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label    The label for this button.
     * @param disabled Whether the button is disabled.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button primary(String customId, String label, boolean disabled) {
        return create(customId, ButtonStyle.PRIMARY, label, (Emoji) null, disabled);
    }

    /**
     * Create a new button with the blurple primary style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label    The label for this button.
     * @param emoji    The emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button primary(String customId, String label, Emoji emoji) {
        return create(customId, ButtonStyle.PRIMARY, label, emoji, false);
    }

    /**
     * Create a new button with the blurple primary style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label    The label for this button.
     * @param emoji    The emoji for this button.
     * @param disabled Whether the button is disabled.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button primary(String customId, String label, Emoji emoji, boolean disabled) {
        return create(customId, ButtonStyle.PRIMARY, label, emoji, disabled);
    }

    /**
     * Create a new button with the blurple primary style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId     The custom id for this button.
     * @param label        The label for this button.
     * @param unicodeEmoji The emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button primary(String customId, String label, String unicodeEmoji) {
        return create(customId, ButtonStyle.PRIMARY, label, unicodeEmoji, false);
    }

    /**
     * Create a new button with the blurple primary style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId     The custom id for this button.
     * @param label        The label for this button.
     * @param unicodeEmoji The emoji for this button.
     * @param disabled     Whether the button is disabled.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button primary(String customId, String label, String unicodeEmoji, boolean disabled) {
        return create(customId, ButtonStyle.PRIMARY, label, unicodeEmoji, disabled);
    }

    /**
     * Create a new button with the blurple primary style and the given emoji.
     * If you want to use a unicode emoji, use {@link Button#primary(String, String, String)} instead
     * and pass {@code null} as your label.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param emoji    The emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button primary(String customId, Emoji emoji) {
        return create(customId, ButtonStyle.PRIMARY, null, emoji, false);
    }

    /**
     * Create a new button with the blurple primary style and the given emoji.
     * If you want to use a unicode emoji, use {@link Button#primary(String, String, String, boolean)} instead
     * and pass {@code null} as your label.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param emoji    The emoji for this button.
     * @param disabled Whether the button is disabled.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button primary(String customId, Emoji emoji, boolean disabled) {
        return create(customId, ButtonStyle.PRIMARY, null, emoji, disabled);
    }

    /**
     * Create a new button with the grey secondary style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label    The label for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button secondary(String customId, String label) {
        return create(customId, ButtonStyle.SECONDARY, label, (Emoji) null, false);
    }

    /**
     * Create a new button with the grey secondary style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label    The label for this button.
     * @param disabled Whether the button is disabled.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button secondary(String customId, String label, boolean disabled) {
        return create(customId, ButtonStyle.SECONDARY, label, (Emoji) null, disabled);
    }

    /**
     * Create a new button with the grey secondary style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label    The label for this button.
     * @param emoji    The emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button secondary(String customId, String label, Emoji emoji) {
        return create(customId, ButtonStyle.SECONDARY, label, emoji, false);
    }

    /**
     * Create a new button with the grey secondary style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label    The label for this button.
     * @param emoji    The emoji for this button.
     * @param disabled Whether the button is disabled.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button secondary(String customId, String label, Emoji emoji, boolean disabled) {
        return create(customId, ButtonStyle.SECONDARY, label, emoji, disabled);
    }

    /**
     * Create a new button with the grey secondary style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId     The custom id for this button.
     * @param label        The label for this button.
     * @param unicodeEmoji The emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button secondary(String customId, String label, String unicodeEmoji) {
        return create(customId, ButtonStyle.SECONDARY, label, unicodeEmoji, false);
    }

    /**
     * Create a new button with the grey secondary style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId     The custom id for this button.
     * @param label        The label for this button.
     * @param unicodeEmoji The emoji for this button.
     * @param disabled     Whether the button is disabled.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button secondary(String customId, String label, String unicodeEmoji, boolean disabled) {
        return create(customId, ButtonStyle.SECONDARY, label, unicodeEmoji, disabled);
    }

    /**
     * Create a new button with the grey secondary style and the given emoji.
     * If you only want to use a unicode emoji, use {@link Button#secondary(String, String, String)} instead
     * and pass {@code null} as your label.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param emoji    The emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button secondary(String customId, Emoji emoji) {
        return create(customId, ButtonStyle.SECONDARY, null, emoji, false);
    }

    /**
     * Create a new button with the grey secondary style and the given emoji.
     * If you only want to use a unicode emoji, use {@link Button#secondary(String, String, String, boolean)} instead
     * and pass {@code null} as your label.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param emoji    The emoji for this button.
     * @param disabled Whether the button is disabled.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button secondary(String customId, Emoji emoji, boolean disabled) {
        return create(customId, ButtonStyle.SECONDARY, null, emoji, disabled);
    }

    /**
     * Create a new button with the green success style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label    The label for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button success(String customId, String label) {
        return create(customId, ButtonStyle.SUCCESS, label, (Emoji) null, false);
    }

    /**
     * Create a new button with the green success style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label    The label for this button.
     * @param disabled Whether the button is disabled.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button success(String customId, String label, boolean disabled) {
        return create(customId, ButtonStyle.SUCCESS, label, (Emoji) null, disabled);
    }

    /**
     * Create a new button with the green success style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label    The label for this button.
     * @param emoji    The emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button success(String customId, String label, Emoji emoji) {
        return create(customId, ButtonStyle.SUCCESS, label, emoji, false);
    }

    /**
     * Create a new button with the green success style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label    The label for this button.
     * @param emoji    The emoji for this button.
     * @param disabled Whether the button is disabled.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button success(String customId, String label, Emoji emoji, boolean disabled) {
        return create(customId, ButtonStyle.SUCCESS, label, emoji, disabled);
    }

    /**
     * Create a new button with the green success style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId     The custom id for this button.
     * @param label        The label for this button.
     * @param unicodeEmoji The emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button success(String customId, String label, String unicodeEmoji) {
        return create(customId, ButtonStyle.SUCCESS, label, unicodeEmoji, false);
    }

    /**
     * Create a new button with the green success style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId     The custom id for this button.
     * @param label        The label for this button.
     * @param unicodeEmoji The emoji for this button.
     * @param disabled     Whether the button is disabled.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button success(String customId, String label, String unicodeEmoji, boolean disabled) {
        return create(customId, ButtonStyle.SUCCESS, label, unicodeEmoji, disabled);
    }

    /**
     * Create a new button with the green success style and the given emoji.
     * If you only want to use a unicode emoji, use {@link Button#success(String, String, String)} instead
     * and pass {@code null} as your label.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param emoji    The emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button success(String customId, Emoji emoji) {
        return create(customId, ButtonStyle.SUCCESS, null, emoji, false);
    }

    /**
     * Create a new button with the green success style and the given emoji.
     * If you only want to use a unicode emoji, use {@link Button#success(String, String, String, boolean)} instead
     * and pass {@code null} as your label.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param emoji    The emoji for this button.
     * @param disabled Whether the button is disabled.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button success(String customId, Emoji emoji, boolean disabled) {
        return create(customId, ButtonStyle.SUCCESS, null, emoji, disabled);
    }

    /**
     * Create a new button for a link button.
     * This is a convenience shorthand method.
     *
     * @param url   The url for this link button.
     * @param label The label for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button link(String url, String label) {
        return link(url, label, false);
    }

    /**
     * Create a new button for a link button.
     * This is a convenience shorthand method.
     *
     * @param url      The url for this link button.
     * @param label    The label for this button.
     * @param disabled Whether the button is disabled.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button link(String url, String label, boolean disabled) {
        return new ButtonBuilder()
                .setStyle(ButtonStyle.LINK)
                .setUrl(url)
                .setLabel(label)
                .setDisabled(disabled)
                .build();
    }

    /**
     * Create a new button for a link button.
     * This is a convenience shorthand method.
     *
     * @param url   The url for this link button.
     * @param label The label for this button.
     * @param emoji The emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button link(String url, String label, Emoji emoji) {
        return link(url, label, emoji, false);
    }

    /**
     * Create a new button for a link button.
     * This is a convenience shorthand method.
     *
     * @param url      The url for this link button.
     * @param label    The label for this button.
     * @param emoji    The emoji for this button.
     * @param disabled Whether the button is disabled.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button link(String url, String label, Emoji emoji, boolean disabled) {
        return new ButtonBuilder()
                .setStyle(ButtonStyle.LINK)
                .setUrl(url)
                .setLabel(label)
                .setEmoji(emoji)
                .setDisabled(disabled)
                .build();
    }

    /**
     * Create a new button for a link button.
     * This is a convenience shorthand method.
     *
     * @param url          The url for this link button.
     * @param label        The label for this button.
     * @param unicodeEmoji The emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button link(String url, String label, String unicodeEmoji) {
        return link(url, label, unicodeEmoji, false);
    }

    /**
     * Create a new button for a link button.
     * This is a convenience shorthand method.
     *
     * @param url          The url for this link button.
     * @param label        The label for this button.
     * @param unicodeEmoji The emoji for this button.
     * @param disabled     Whether the button is disabled.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button link(String url, String label, String unicodeEmoji, boolean disabled) {
        return new ButtonBuilder()
                .setStyle(ButtonStyle.LINK)
                .setUrl(url)
                .setLabel(label)
                .setEmoji(unicodeEmoji)
                .setDisabled(disabled)
                .build();
    }

    /**
     * Create a new button for a link button.
     * If you only want to use a unicode emoji, use {@link Button#link(String, String, String)} instead
     * and pass {@code null} as your label.
     * This is a convenience shorthand method.
     *
     * @param url   The url for this link button.
     * @param emoji The emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button link(String url, Emoji emoji) {
        return link(url, emoji, false);
    }

    /**
     * Create a new button for a link button.
     * If you only want to use a unicode emoji, use {@link Button#link(String, String, String, boolean)} instead
     * and pass {@code null} as your label.
     * This is a convenience shorthand method.
     *
     * @param url      The url for this link button.
     * @param emoji    The emoji for this button.
     * @param disabled Whether the button is disabled.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button link(String url, Emoji emoji, boolean disabled) {
        return new ButtonBuilder()
                .setStyle(ButtonStyle.LINK)
                .setUrl(url)
                .setEmoji(emoji)
                .setDisabled(disabled)
                .build();
    }

    /**
     * Create a new button with the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param style    The button style for this button.
     * @param label    The label for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button create(String customId, ButtonStyle style, String label) {
        return create(customId, style, label, (Emoji) null, false);
    }

    /**
     * Create a new button with the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param style    The button style for this button.
     * @param label    The label for this button.
     * @param disabled Whether the button is disabled.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button create(String customId, ButtonStyle style, String label, boolean disabled) {
        return create(customId, style, label, (Emoji) null, disabled);
    }

    /**
     * Create a new button with the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param style    The button style for this button.
     * @param label    The label for this button.
     * @param emoji    The emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button create(String customId, ButtonStyle style, String label, Emoji emoji) {
        return create(customId, style, label, emoji, false);
    }

    /**
     * Create a new button with the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param style    The button style for this button.
     * @param label    The label for this button.
     * @param emoji    The emoji for this button.
     * @param disabled Whether the button is disabled.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button create(String customId, ButtonStyle style, String label, Emoji emoji, boolean disabled) {
        if (style == ButtonStyle.LINK) {
            throw new IllegalArgumentException("You can not use the link style with this method."
                    + "Please use Button#link() instead or create a custom ButtonBuilder by "
                    + "calling 'new ButtonBuilder()'.");
        }
        return new ButtonBuilder()
                .setCustomId(customId)
                .setStyle(style)
                .setLabel(label)
                .setEmoji(emoji)
                .setDisabled(disabled)
                .build();
    }

    /**
     * Create a new button with the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId     The custom id for this button.
     * @param style        The button style for this button.
     * @param label        The label for this button.
     * @param unicodeEmoji The unicode emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button create(String customId, ButtonStyle style, String label, String unicodeEmoji) {
        return create(customId, style, label, unicodeEmoji, false);
    }

    /**
     * Create a new button with the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId     The custom id for this button.
     * @param style        The button style for this button.
     * @param label        The label for this button.
     * @param unicodeEmoji The unicode emoji for this button.
     * @param disabled     Whether the button is disabled.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button create(String customId, ButtonStyle style, String label, String unicodeEmoji, boolean disabled) {
        if (style == ButtonStyle.LINK) {
            throw new IllegalArgumentException("You can not use the link style with this method."
                    + "Please use Button#link() instead or create a custom ButtonBuilder by "
                    + "calling 'new ButtonBuilder()'.");
        }
        return new ButtonBuilder()
                .setCustomId(customId)
                .setStyle(style)
                .setLabel(label)
                .setEmoji(unicodeEmoji)
                .setDisabled(disabled)
                .build();
    }

    /**
     * Create a new button with the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param style    The button style for this button.
     * @param emoji    The emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button create(String customId, ButtonStyle style, Emoji emoji) {
        return create(customId, style, null, emoji, false);
    }
}

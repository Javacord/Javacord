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
     * Get whether or not the button is disabled.
     *
     * @return Whether or not the button is disabled.
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
     * @param label The label for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button danger(String customId, String label) {
        return create(customId, ButtonStyle.DANGER, label, (Emoji) null);
    }

    /**
     * Create a new button with the red danger style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label The label for this button.
     * @param emoji The emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button danger(String customId, String label, Emoji emoji) {
        return create(customId, ButtonStyle.DANGER, label, emoji);
    }

    /**
     * Create a new button with the red danger style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label The label for this button.
     * @param unicodeEmoji The emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button danger(String customId, String label, String unicodeEmoji) {
        return create(customId, ButtonStyle.DANGER, label, unicodeEmoji);
    }

    /**
     * Create a new button with the red danger style and the given emoji.
     * If you want to use an unicode emoji, use {@link Button#danger(String, String, String)} instead
     * and pass {@code null} as your label.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param emoji The emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button danger(String customId, Emoji emoji) {
        return create(customId, ButtonStyle.DANGER, null, emoji);
    }

    /**
     * Create a new button with the blurple primary style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label The label for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button primary(String customId, String label) {
        return create(customId, ButtonStyle.PRIMARY, label, (Emoji) null);
    }

    /**
     * Create a new button with the blurple primary style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label The label for this button.
     * @param emoji The emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button primary(String customId, String label, Emoji emoji) {
        return create(customId, ButtonStyle.PRIMARY, label, emoji);
    }

    /**
     * Create a new button with the blurple primary style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label The label for this button.
     * @param unicodeEmoji The emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button primary(String customId, String label, String unicodeEmoji) {
        return create(customId, ButtonStyle.PRIMARY, label, unicodeEmoji);
    }

    /**
     * Create a new button with the blurple primary style and the given emoji.
     * If you want to use an unicode emoji, use {@link Button#primary(String, String, String)} instead
     * and pass {@code null} as your label.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param emoji The emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button primary(String customId, Emoji emoji) {
        return create(customId, ButtonStyle.PRIMARY, null, emoji);
    }

    /**
     * Create a new button with the grey secondary style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label The label for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button secondary(String customId, String label) {
        return create(customId, ButtonStyle.SECONDARY, label, (Emoji) null);
    }

    /**
     * Create a new button with the grey secondary style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label The label for this button.
     * @param emoji The emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button secondary(String customId, String label, Emoji emoji) {
        return create(customId, ButtonStyle.SECONDARY, label, emoji);
    }

    /**
     * Create a new button with the grey secondary style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label The label for this button.
     * @param unicodeEmoji The emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button secondary(String customId, String label, String unicodeEmoji) {
        return create(customId, ButtonStyle.SECONDARY, label, unicodeEmoji);
    }

    /**
     * Create a new button with the grey secondary style and the given emoji.
     * If you only want to use an unicode emoji, use {@link Button#secondary(String, String, String)} instead
     * and pass {@code null} as your label.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param emoji The emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button secondary(String customId, Emoji emoji) {
        return create(customId, ButtonStyle.SECONDARY, null, emoji);
    }

    /**
     * Create a new button with the green success style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label The label for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button success(String customId, String label) {
        return create(customId, ButtonStyle.SUCCESS, label, (Emoji) null);
    }

    /**
     * Create a new button with the green success style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label The label for this button.
     * @param emoji The emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button success(String customId, String label, Emoji emoji) {
        return create(customId, ButtonStyle.SUCCESS, label, emoji);
    }

    /**
     * Create a new button with the green success style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label The label for this button.
     * @param unicodeEmoji The emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button success(String customId, String label, String unicodeEmoji) {
        return create(customId, ButtonStyle.SUCCESS, label, unicodeEmoji);
    }

    /**
     * Create a new button with the green success style and the given emoji.
     * If you only want to use an unicode emoji, use {@link Button#success(String, String, String)} instead
     * and pass {@code null} as your label.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param emoji The emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button success(String customId, Emoji emoji) {
        return create(customId, ButtonStyle.SUCCESS, null, emoji);
    }

    /**
     * Create a new button for a link button.
     * This is a convenience shorthand method.
     *
     * @param url The url for this link button.
     * @param label The label for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button link(String url, String label) {
        return new ButtonBuilder()
            .setStyle(ButtonStyle.LINK)
            .setUrl(url)
            .setLabel(label)
            .build();
    }

    /**
     * Create a new button for a link button.
     * This is a convenience shorthand method.
     *
     * @param url The url for this link button.
     * @param label The label for this button.
     * @param emoji The emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button link(String url, String label, Emoji emoji) {
        return new ButtonBuilder()
            .setStyle(ButtonStyle.LINK)
            .setUrl(url)
            .setLabel(label)
            .setEmoji(emoji)
            .build();
    }

    /**
     * Create a new button for a link button.
     * This is a convenience shorthand method.
     *
     * @param url The url for this link button.
     * @param label The label for this button.
     * @param unicodeEmoji The emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button link(String url, String label, String unicodeEmoji) {
        return new ButtonBuilder()
            .setStyle(ButtonStyle.LINK)
            .setUrl(url)
            .setLabel(label)
            .setEmoji(unicodeEmoji)
            .build();
    }

    /**
     * Create a new button for a link button.
     * If you only want to use an unicode emoji, use {@link Button#link(String, String, String)} instead
     * and pass {@code null} as your label.
     * This is a convenience shorthand method.
     *
     * @param url The url for this link button.
     * @param emoji The emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button link(String url, Emoji emoji) {
        return new ButtonBuilder()
            .setStyle(ButtonStyle.LINK)
            .setUrl(url)
            .setEmoji(emoji)
            .build();
    }

    /**
     * Create a new button with the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param style The button style for this button.
     * @param label The label for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button create(String customId, ButtonStyle style, String label) {
        return create(customId, style, label, (Emoji) null);
    }

    /**
     * Create a new button with the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param style The button style for this button.
     * @param label The label for this button.
     * @param emoji The emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button create(String customId, ButtonStyle style, String label, Emoji emoji) {
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
            .build();
    }

    /**
     * Create a new button with the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param style The button style for this button.
     * @param label The label for this button.
     * @param unicodeEmoji The unicode emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button create(String customId, ButtonStyle style, String label, String unicodeEmoji) {
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
            .build();
    }

    /**
     * Create a new button with the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param style The button style for this button.
     * @param emoji The emoji for this button.
     * @return the new button to be used with an ActionRow or a message
     */
    static Button create(String customId, ButtonStyle style, Emoji emoji) {
        return create(customId, style, null, emoji);
    }
}

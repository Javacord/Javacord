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
     * Create a new button builder with the red danger style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label    The label for this button.
     * @return the new button builder to be used with an ActionRow or a message
     */
    static ButtonBuilder danger(String customId, String label) {
        return with(customId, ButtonStyle.DANGER, label, (Emoji) null);
    }

    /**
     * Create a new button builder with the red danger style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label    The label for this button.
     * @param emoji    The emoji for this button.
     * @return the new button builder to be used with an ActionRow or a message
     */
    static ButtonBuilder danger(String customId, String label, Emoji emoji) {
        return with(customId, ButtonStyle.DANGER, label, emoji);
    }

    /**
     * Create a new button builder with the red danger style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId     The custom id for this button.
     * @param label        The label for this button.
     * @param unicodeEmoji The emoji for this button.
     * @return the new button builder to be used with an ActionRow or a message
     */
    static ButtonBuilder danger(String customId, String label, String unicodeEmoji) {
        return with(customId, ButtonStyle.DANGER, label, unicodeEmoji);
    }

    /**
     * Create a new button builder with the red danger style and the given emoji.
     * Due to signature collisions, there is no such shorthand for unicode emojis; please use
     * the {@code with(...)} method instead.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param emoji    The emoji for this button.
     * @return the new button builder to be used with an ActionRow or a message
     */
    static ButtonBuilder danger(String customId, Emoji emoji) {
        return with(customId, ButtonStyle.DANGER, null, emoji);
    }

    /**
     * Create a new button builder with the red danger style.
     * Note: You MUST call at least one of {@code setEmoji()} or {@code setLabel()} on the returned builder,
     * as the button will be invalid otherwise.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @return the new button builder to be used with an ActionRow or a message
     */
    static ButtonBuilder danger(String customId) {
        return with(customId, ButtonStyle.DANGER);
    }

    /**
     * Create a new button builder with the blurple primary style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label    The label for this button.
     * @return the new button builder to be used with an ActionRow or a message
     */
    static ButtonBuilder primary(String customId, String label) {
        return with(customId, ButtonStyle.PRIMARY, label, (Emoji) null);
    }

    /**
     * Create a new button builder with the blurple primary style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label    The label for this button.
     * @param emoji    The emoji for this button.
     * @return the new button builder to be used with an ActionRow or a message
     */
    static ButtonBuilder primary(String customId, String label, Emoji emoji) {
        return with(customId, ButtonStyle.PRIMARY, label, emoji);
    }

    /**
     * Create a new button builder with the blurple primary style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId     The custom id for this button.
     * @param label        The label for this button.
     * @param unicodeEmoji The emoji for this button.
     * @return the new button builder to be used with an ActionRow or a message
     */
    static ButtonBuilder primary(String customId, String label, String unicodeEmoji) {
        return with(customId, ButtonStyle.PRIMARY, label, unicodeEmoji);
    }

    /**
     * Create a new button builder with the blurple primary style and the given emoji.
     * Due to signature collisions, there is no such shorthand for unicode emojis; please use
     * the {@code with(...)} method instead.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param emoji    The emoji for this button.
     * @return the new button builder to be used with an ActionRow or a message
     */
    static ButtonBuilder primary(String customId, Emoji emoji) {
        return with(customId, ButtonStyle.PRIMARY, null, emoji);
    }

    /**
     * Create a new button builder with the blurple primary style.
     * Note: You MUST call at least one of {@code setEmoji()} or {@code setLabel()} on the returned builder,
     * as the button will be invalid otherwise.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @return the new button builder to be used with an ActionRow or a message
     */
    static ButtonBuilder primary(String customId) {
        return with(customId, ButtonStyle.PRIMARY);
    }

    /**
     * Create a new button builder with the grey secondary style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label    The label for this button.
     * @return the new button builder to be used with an ActionRow or a message
     */
    static ButtonBuilder secondary(String customId, String label) {
        return with(customId, ButtonStyle.SECONDARY, label, (Emoji) null);
    }

    /**
     * Create a new button builder with the grey secondary style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label    The label for this button.
     * @param emoji    The emoji for this button.
     * @return the new button builder to be used with an ActionRow or a message
     */
    static ButtonBuilder secondary(String customId, String label, Emoji emoji) {
        return with(customId, ButtonStyle.SECONDARY, label, emoji);
    }

    /**
     * Create a new button builder with the grey secondary style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId     The custom id for this button.
     * @param label        The label for this button.
     * @param unicodeEmoji The emoji for this button.
     * @return the new button builder to be used with an ActionRow or a message
     */
    static ButtonBuilder secondary(String customId, String label, String unicodeEmoji) {
        return with(customId, ButtonStyle.SECONDARY, label, unicodeEmoji);
    }

    /**
     * Create a new button builder with the grey secondary style and the given emoji.
     * Due to signature collisions, there is no such shorthand for unicode emojis; please use
     * the {@link Button#with(String, ButtonStyle)} method and call the emoji setter method on the
     * returned builder instead.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param emoji    The emoji for this button.
     * @return the new button builder to be used with an ActionRow or a message
     */
    static ButtonBuilder secondary(String customId, Emoji emoji) {
        return with(customId, ButtonStyle.SECONDARY, null, emoji);
    }

    /**
     * Create a new button builder with the grey secondary style.
     * Note: You MUST call at least one of {@code setEmoji()} or {@code setLabel()} on the returned builder,
     * as the button will be invalid otherwise.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @return the new button builder to be used with an ActionRow or a message
     */
    static ButtonBuilder secondary(String customId) {
        return with(customId, ButtonStyle.SECONDARY);
    }

    /**
     * Create a new button builder with the green success style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label    The label for this button.
     * @return the new button builder to be used with an ActionRow or a message
     */
    static ButtonBuilder success(String customId, String label) {
        return with(customId, ButtonStyle.SUCCESS, label, (Emoji) null);
    }

    /**
     * Create a new button builder with the green success style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param label    The label for this button.
     * @param emoji    The emoji for this button.
     * @return the new button builder to be used with an ActionRow or a message
     */
    static ButtonBuilder success(String customId, String label, Emoji emoji) {
        return with(customId, ButtonStyle.SUCCESS, label, emoji);
    }

    /**
     * Create a new button builder with the green success style and the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId     The custom id for this button.
     * @param label        The label for this button.
     * @param unicodeEmoji The emoji for this button.
     * @return the new button builder to be used with an ActionRow or a message
     */
    static ButtonBuilder success(String customId, String label, String unicodeEmoji) {
        return with(customId, ButtonStyle.SUCCESS, label, unicodeEmoji);
    }

    /**
     * Create a new button builder with the green success style and the given emoji.
     * Due to signature collisions, there is no such shorthand for unicode emojis; please use
     * the {@link Button#success(String)} method and call the emoji setter method on the
     * returned builder instead.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param emoji    The emoji for this button.
     * @return the new button builder to be used with an ActionRow or a message
     */
    static ButtonBuilder success(String customId, Emoji emoji) {
        return with(customId, ButtonStyle.SUCCESS, null, emoji);
    }

    /**
     * Create a new button builder with the green success style.
     * Note: You MUST call at least one of {@code setEmoji()} or {@code setLabel()} on the returned builder,
     * as the button will be invalid otherwise.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @return the new button builder to be used with an ActionRow or a message
     */
    static ButtonBuilder success(String customId) {
        return with(customId, ButtonStyle.SUCCESS);
    }

    /**
     * Create a new button builder for a link button.
     * This is a convenience shorthand method.
     *
     * @param url   The url for this link button.
     * @param label The label for this button.
     * @return the new button builder to be used with an ActionRow or a message
     */
    static ButtonBuilder link(String url, String label) {
        return new ButtonBuilder()
                .setStyle(ButtonStyle.LINK)
                .setUrl(url)
                .setLabel(label);
    }

    /**
     * Create a new button builder for a link button.
     * This is a convenience shorthand method.
     *
     * @param url   The url for this link button.
     * @param label The label for this button.
     * @param emoji The emoji for this button.
     * @return the new button builder to be used with an ActionRow or a message
     */
    static ButtonBuilder link(String url, String label, Emoji emoji) {
        return new ButtonBuilder()
                .setStyle(ButtonStyle.LINK)
                .setUrl(url)
                .setLabel(label)
                .setEmoji(emoji);
    }

    /**
     * Create a new button builder for a link button.
     * This is a convenience shorthand method.
     *
     * @param url          The url for this link button.
     * @param label        The label for this button.
     * @param unicodeEmoji The emoji for this button.
     * @return the new button builder to be used with an ActionRow or a message
     */
    static ButtonBuilder link(String url, String label, String unicodeEmoji) {
        return new ButtonBuilder()
                .setStyle(ButtonStyle.LINK)
                .setUrl(url)
                .setLabel(label)
                .setEmoji(unicodeEmoji);
    }

    /**
     * Create a new button builder for a link button.
     * Due to signature collisions, there is no such shorthand for unicode emojis; please use
     * the {@link Button#link(String)} method and call the emoji setter method on the
     * returned builder instead.
     * This is a convenience shorthand method.
     *
     * @param url   The url for this link button.
     * @param emoji The emoji for this button.
     * @return the new button builder to be used with an ActionRow or a message
     */
    static ButtonBuilder link(String url, Emoji emoji) {
        return new ButtonBuilder()
                .setStyle(ButtonStyle.LINK)
                .setUrl(url)
                .setEmoji(emoji);
    }

    /**
     * Create a new button builder for a link button.
     * Note: You MUST call at least one of {@code setEmoji()} or {@code setLabel()} on the returned builder,
     * as the button will be invalid otherwise.
     * This is a convenience shorthand method.
     *
     * @param url The url for this link button.
     * @return the new button builder to be used with an ActionRow or a message
     */
    static ButtonBuilder link(String url) {
        return new ButtonBuilder()
                .setStyle(ButtonStyle.LINK)
                .setUrl(url);
    }

    /**
     * Create a new button builder with the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param style    The button style for this button.
     * @param label    The label for this button.
     * @return the new button builder to be used with an ActionRow or a message
     */
    static ButtonBuilder with(String customId, ButtonStyle style, String label) {
        return with(customId, style, label, (Emoji) null);
    }

    /**
     * Create a new button builder with the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param style    The button style for this button.
     * @param label    The label for this button.
     * @param emoji    The emoji for this button.
     * @return the new button builder to be used with an ActionRow or a message
     */
    static ButtonBuilder with(String customId, ButtonStyle style, String label, Emoji emoji) {
        if (style == ButtonStyle.LINK) {
            throw new IllegalArgumentException("You can not use the link style with this method."
                    + "Please use Button#link() instead or create a custom ButtonBuilder by "
                    + "calling 'new ButtonBuilder()'.");
        }
        return new ButtonBuilder()
                .setCustomId(customId)
                .setStyle(style)
                .setLabel(label)
                .setEmoji(emoji);
    }

    /**
     * Create a new button builder with the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId     The custom id for this button.
     * @param style        The button style for this button.
     * @param label        The label for this button.
     * @param unicodeEmoji The unicode emoji for this button.
     * @return the new button builder to be used with an ActionRow or a message
     */
    static ButtonBuilder with(String customId, ButtonStyle style, String label, String unicodeEmoji) {
        if (style == ButtonStyle.LINK) {
            throw new IllegalArgumentException("You can not use the link style with this method."
                    + "Please use Button#link() instead or create a custom ButtonBuilder by "
                    + "calling 'new ButtonBuilder()'.");
        }
        return new ButtonBuilder()
                .setCustomId(customId)
                .setStyle(style)
                .setLabel(label)
                .setEmoji(unicodeEmoji);
    }

    /**
     * Create a new button builder with the given properties.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param style    The button style for this button.
     * @param emoji    The emoji for this button.
     * @return the new button builder to be used with an ActionRow or a message
     */
    static ButtonBuilder with(String customId, ButtonStyle style, Emoji emoji) {
        return with(customId, style, null, emoji);
    }

    /**
     * Create a new button builder with the given properties.
     * Note: You MUST call at least one of {@code setEmoji()} or {@code setLabel()} on the returned builder,
     * as the button will be invalid otherwise.
     * This is a convenience shorthand method.
     *
     * @param customId The custom id for this button.
     * @param style    The button style for this button.
     * @return the new button builder to be used with an ActionRow or a message
     */
    static ButtonBuilder with(String customId, ButtonStyle style) {
        return with(customId, style, null, (Emoji) null);
    }
}

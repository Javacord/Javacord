package de.btobastian.javacord.utils;

import java.util.regex.Pattern;

/**
 * This class contains some useful precompiled regex patterns.
 */
public class DiscordRegexPattern {

    /**
     * A pattern which checks for mentioned users (e.g. {@code <@1234567890>}).
     */
    public static final Pattern USER_MENTION = Pattern.compile("<@!?(?<id>[0-9]+)>");

    /**
     * A pattern which checks for mentioned channels (e.g. {@code <#1234567890>}).
     */
    public static final Pattern CHANNEL_MENTION = Pattern.compile("<#(?<id>[0-9]+)>");

    /**
     * A pattern which checks for custom emojis (e.g. {@code <:my_emoji:1234567890>}).
     */
    public static final Pattern CUSTOM_EMOJI = Pattern.compile("<a?:(?<name>[0-9a-zA-Z_]+):(?<id>[0-9]+)>");

    /**
     * A pattern which checks for unicode emojis (e.g. {@code 🤔}
     */
    public static final Pattern UNICODE_EMOJI = Pattern.compile("\\p{So}|[\\uD83C-\\uDBFF\\uDC00-\\uDFFF]+");

    /**
     * You are not meant to create instances of this class.
     *
     * @throws UnsupportedOperationException
     */
    private DiscordRegexPattern() {
        throw new UnsupportedOperationException();
    }

}

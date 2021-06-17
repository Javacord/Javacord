package org.javacord.api.util;

import java.util.regex.Pattern;

/**
 * This class contains some useful precompiled regex patterns.
 */
public class DiscordRegexPattern {

    /**
     * A pattern which checks for mentioned users (e.g. {@code <@1234567890>}).
     */
    public static final Pattern USER_MENTION =
            Pattern.compile("(?x)                  # enable comment mode \n"
                            + "(?<!                # negative lookbehind \n"
                            + "                    # (do not have uneven amount of backslashes before) \n"
                            + "    (?<!\\\\)       # negative lookbehind (do not have one backslash before) \n"
                            + "    (?:\\\\{2}+)    # exactly two backslashes \n"
                            + "    {0,1000000000}+ # 0 to 1_000_000_000 times \n"
                            + "                    # (basically *, but a lookbehind has to have a maximum length) \n"
                            + "    \\\\            # the one escaping backslash \n"
                            + ")                   # \n"
                            + "<@!?+               # '<@' or '<@!' \n"
                            + "(?<id>[0-9]++)      # the user id as named group \n"
                            + ">                   # '>'");

    /**
     * A pattern which checks for mentioned roles (e.g. {@code <@&1234567890>}).
     */
    public static final Pattern ROLE_MENTION =
            Pattern.compile("(?x)                  # enable comment mode \n"
                            + "(?<!                # negative lookbehind \n"
                            + "                    # (do not have uneven amount of backslashes before) \n"
                            + "    (?<!\\\\)       # negative lookbehind (do not have one backslash before) \n"
                            + "    (?:\\\\{2}+)    # exactly two backslashes \n"
                            + "    {0,1000000000}+ # 0 to 1_000_000_000 times \n"
                            + "                    # (basically *, but a lookbehind has to have a maximum length) \n"
                            + "    \\\\            # the one escaping backslash \n"
                            + ")                   # \n"
                            + "<@&                 # '<@&' \n"
                            + "(?<id>[0-9]++)      # the role id as named group \n"
                            + ">                   # '>'");

    /**
     * A pattern which checks for mentioned channels (e.g. {@code <#1234567890>}).
     */
    public static final Pattern CHANNEL_MENTION =
            Pattern.compile("(?x)                  # enable comment mode \n"
                            + "(?<!                # negative lookbehind \n"
                            + "                    # (do not have uneven amount of backslashes before) \n"
                            + "    (?<!\\\\)       # negative lookbehind (do not have one backslash before) \n"
                            + "    (?:\\\\{2}+)    # exactly two backslashes \n"
                            + "    {0,1000000000}+ # 0 to 1_000_000_000 times \n"
                            + "                    # (basically *, but a lookbehind has to have a maximum length) \n"
                            + "    \\\\            # the one escaping backslash \n"
                            + ")                   # \n"
                            + "(?-x:<#)            # '<#' with disabled comment mode due to the # \n"
                            + "(?<id>[0-9]++)      # the channel id as named group \n"
                            + ">                   # '>'");

    /**
     * A pattern which checks for custom emojis (e.g. {@code <:my_emoji:1234567890>}).
     */
    public static final Pattern CUSTOM_EMOJI =
            Pattern.compile("(?x)                  # enable comment mode \n"
                            + "(?<!                # negative lookbehind \n"
                            + "                    # (do not have uneven amount of backslashes before) \n"
                            + "    (?<!\\\\)       # negative lookbehind (do not have one backslash before) \n"
                            + "    (?:\\\\{2}+)    # exactly two backslashes \n"
                            + "    {0,1000000000}+ # 0 to 1_000_000_000 times \n"
                            + "                    # (basically *, but a lookbehind has to have a maximum length) \n"
                            + "    \\\\            # the one escaping backslash \n"
                            + ")                   # \n"
                            + "<a?+:               # '<:' or '<a:' \n"
                            + "(?<name>\\w++)      # the custom emoji name as named group \n"
                            + ":                   # ':' \n"
                            + "(?<id>[0-9]++)      # the custom emoji id as named group \n"
                            + ">                   # '>' \n");

    /**
     * A pattern which checks for message links (e.g. {@code https://discord.com/channels/@me/1234/5678}
     */
    public static final Pattern MESSAGE_LINK =
            Pattern.compile("(?x)                               # enable comment mode \n"
                            + "(?i)                             # ignore case \n"
                            + "(?:https?+://)?+                 # 'https://' or 'http://' or '' \n"
                            + "(?:(?:canary|ptb)\\.)?+          # 'canary.' or 'ptb.'\n"
                            + "discord(?:app)?+\\.com/channels/ # 'discord(app).com/channels/' \n"
                            + "(?:(?<server>[0-9]++)|@me)       # '@me' or the server id as named group \n"
                            + "/                                # '/' \n"
                            + "(?<channel>[0-9]++)              # the textchannel id as named group \n"
                            + "/                                # '/' \n"
                            + "(?<message>[0-9]++)              # the message id as named group \n");

    /**
     * A pattern which checks for webhook urls (e.g. {@code https://discord.com/api/webhooks/1234/abcd}
     */
    public static final Pattern WEBHOOK_URL =
            Pattern.compile("(?x)                                   # enable comment mode \n"
                            + "(?i)                                 # ignore case \n"
                            + "(?:https?+://)?+                     # 'https://' or 'http://' or '' \n"
                            + "(?:(?:canary|ptb)\\.)?+              # 'canary.' or 'ptb.'\n"
                            + "discord(?:app)?+\\.com/api/webhooks/ # 'discord(app).com/api/webhooks' \n"
                            + "(?<id>[0-9]++)                       # the webhook id as named group \n"
                            + "/                                    # '/' \n"
                            + "(?<token>[^/\\s]++)                  # the webhook token as named group \n");

    /**
     * A pattern to match snowflakes.
     */
    public static final Pattern SNOWFLAKE =
            Pattern.compile("(?<id>[0-9]{15,25})");

    /**
     * You are not meant to create instances of this class.
     */
    private DiscordRegexPattern() {
        throw new UnsupportedOperationException();
    }

}

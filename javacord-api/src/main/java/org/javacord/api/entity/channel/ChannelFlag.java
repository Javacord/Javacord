package org.javacord.api.entity.channel;

/**
 * An enum with all different channel flags.
 */
public enum ChannelFlag {
    /**
     * This thread is pinned to the top of its parent GUILD_FORUM channel.
     */
    PINNED(1 << 1),
    /**
     * Whether a tag is required to be specified when creating a thread
     * in a GUILD_FORUM channel.
     * Tags are specified in the applied_tags field.
     */
    REQUIRE_TAG(1 << 4),
    /**
     * When a set hides the embedded media download options.
     * Available only for a media channel
     */
    HIDE_MEDIA_DOWNLOAD_OPTIONS(1 << 15),
    /**
     * This flag in unknown.
     */
    UNKNOWN(-1);

    private final int flag;

    /**
     * Class constructor.
     *
     * @param flag The bitmask of the flag.
     */
    ChannelFlag(int flag) {
        this.flag = flag;
    }

    /**
     * Gets the integer value of the flag.
     *
     * @return The integer value of the flag.
     */
    public int getValue() {
        return flag;
    }

    /**
     * Gets the flag from the given integer.
     *
     * @param flag The integer to get the flag from.
     * @return The flag.
     */
    public static ChannelFlag getByValue(int flag) {
        for (ChannelFlag channelFlag : values()) {
            if (channelFlag.getValue() == flag) {
                return channelFlag;
            }
        }
        return UNKNOWN;
    }
}

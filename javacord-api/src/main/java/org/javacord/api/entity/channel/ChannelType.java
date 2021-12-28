package org.javacord.api.entity.channel;

import java.util.Arrays;

/**
 * An enum with all different channel types.
 */
public enum ChannelType {

    SERVER_TEXT_CHANNEL(0, true, false, true, true),
    PRIVATE_CHANNEL(1, true, true, false, false),
    SERVER_VOICE_CHANNEL(2, false, true, true, true),
    GROUP_CHANNEL(3, true, true, false, false),
    CHANNEL_CATEGORY(4, false, false, true, true),
    SERVER_NEWS_CHANNEL(5, true, false, true, true),
    SERVER_STORE_CHANNEL(6, true, false, true, true),
    SERVER_NEWS_THREAD(10, true, false, true, false),
    SERVER_PUBLIC_THREAD(11, true, false, true, false),
    SERVER_PRIVATE_THREAD(12, true, false, true, false),
    SERVER_STAGE_VOICE_CHANNEL(13, false, true, true, true),
    UNKNOWN(-1, false, false, false, false);

    private static final ChannelType[] textChannelTypes = Arrays.stream(ChannelType.values())
            .filter(ChannelType::isTextChannelType)
            .toArray(ChannelType[]::new);

    private static final ChannelType[] voiceChannelTypes = Arrays.stream(ChannelType.values())
            .filter(ChannelType::isVoiceChannelType)
            .toArray(ChannelType[]::new);

    private static final ChannelType[] serverChannelTypes = Arrays.stream(ChannelType.values())
            .filter(ChannelType::isServerChannelType)
            .toArray(ChannelType[]::new);

    private static final ChannelType[] regularServerChannelTypes = Arrays.stream(ChannelType.values())
            .filter(ChannelType::isRegularServerChannelType)
            .toArray(ChannelType[]::new);

    /**
     * The id of the channel type.
     */
    private final int id;

    private final boolean textChannelType;
    private final boolean voiceChannelType;
    private final boolean serverChannelType;
    private final boolean regularServerChannelType;

    /**
     * Creates a new channel type.
     *
     * @param id                The id of the channel type.
     * @param textChannelType   Whether this type is a text channel type or not.
     * @param voiceChannelType  Whether this type is a voice channel type or not.
     * @param serverChannelType Whether this type is a server channel type or not.
     * @param regularServerChannelType Whether this type is a regular server channel type or not.
     */
    ChannelType(int id, boolean textChannelType, boolean voiceChannelType, boolean serverChannelType,
                boolean regularServerChannelType) {
        this.id = id;
        this.textChannelType = textChannelType;
        this.voiceChannelType = voiceChannelType;
        this.serverChannelType = serverChannelType;
        this.regularServerChannelType = regularServerChannelType;
    }

    /**
     * Gets the id of the channel type.
     *
     * @return The id of the channel type.
     */
    public int getId() {
        return id;
    }

    /**
     * Checks if this type is a text channel type.
     *
     * @return Whether this type is a text channel type or not.
     */
    public boolean isTextChannelType() {
        return textChannelType;
    }

    /**
     * Checks if this type is a voice channel type.
     *
     * @return Whether this type is a voice channel type or not.
     */
    public boolean isVoiceChannelType() {
        return voiceChannelType;
    }

    /**
     * Checks if this type is a server channel type.
     *
     * @return Whether this type is a server channel type or not.
     */
    public boolean isServerChannelType() {
        return serverChannelType;
    }

    /**
     * Checks if this type is a regular server channel type.
     *
     * @return Whether this type is a server channel type or not.
     */
    public boolean isRegularServerChannelType() {
        return regularServerChannelType;
    }

    /**
     * Gets a channel type by its id.
     *
     * @param id The id of the channel type.
     * @return The channel type with the given id.
     */
    public static ChannelType fromId(int id) {
        for (ChannelType type : values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        return UNKNOWN;
    }

    /**
     * Gets an array with all types that are text channel types.
     *
     * @return All types that are text channel types.
     */
    public static ChannelType[] getTextChannelTypes() {
        return textChannelTypes;
    }

    /**
     * Gets an array with all type that are voice channel types.
     *
     * @return All types that are voice channel types.
     */
    public static ChannelType[] getVoiceChannelTypes() {
        return voiceChannelTypes;
    }

    /**
     * Gets an array with all types that are server channel types.
     *
     * @return All types that are server channel types.
     */
    public static ChannelType[] getServerChannelTypes() {
        return serverChannelTypes;
    }

    /**
     * Gets an array with all types that are regular server channel types.
     *
     * @return All types that are server channel types.
     */
    public static ChannelType[] getRegularServerChannelTypes() {
        return regularServerChannelTypes;
    }
}

package org.javacord.api.entity.activity;

public enum ActivityFlag {

    INSTANCE(1 << 0),
    JOIN(1 << 1),
    SPECTATE(1 << 2),
    JOIN_REQUEST(1 << 3),
    SYNC(1 << 4),
    PLAY(1 << 5),
    PARTY_PRIVACY_FRIENDS(1 << 6),
    PARTY_PRIVACY_VOICE_CHANNEL(1 << 7),
    EMBEDDED(1 << 8),
    UNKNOWN(-1);

    private final int flag;

    /**
     * The class constructor.
     * @param flag The bitmask of the flag.
     */
    ActivityFlag(int flag) {
        this.flag = flag;
    }

    /**
     * Gets the activity flag of the given integer value.
     * @param flags The integer value of the flag.
     * @return The activity flag of the given integer value.
     */
    public static ActivityFlag getActivityFlagById(int flags) {
        for (ActivityFlag activityFlag : values()) {
            if (activityFlag.flag == flags) {
                return activityFlag;
            }
        }
        return UNKNOWN;
    }

    /**
     * Gets the integer value of the activity flag.
     * @return Gets the integer value of the activity flag.
     */
    public int asInt() {
        return flag;
    }
}


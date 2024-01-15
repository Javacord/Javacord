package org.javacord.api.entity.permission;

public enum RoleFlag {

    /**
     * Role can be selected by members in an onboarding prompt.
     */
    IN_PROMPT(1 << 0),

    /**
     * An unknown flag.
     */
    UNKNOWN(-1);

    private final int flag;

    /**
     * Class constructor.
     *
     * @param flag The bitmask of the flag.
     */
    RoleFlag(int flag) {
        this.flag = flag;
    }

    /**
     * Gets the integer value of the activity flag.
     * @return Gets the integer value of the activity flag.
     */
    public int asInt() {
        return flag;
    }
}

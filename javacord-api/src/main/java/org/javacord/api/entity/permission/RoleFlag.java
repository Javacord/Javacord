package org.javacord.api.entity.permission;

public enum RoleFlag {
    IN_PROMPT(1 << 0);

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

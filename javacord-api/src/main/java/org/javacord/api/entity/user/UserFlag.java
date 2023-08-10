package org.javacord.api.entity.user;

public enum UserFlag {

    NONE(0, "None"),
    STAFF(1 << 0, "Discord Employee"),
    PARTNER(1 << 1, "Partnered Server Owner"),
    HYPESQUAD(1 << 2, "HypeSquad Events Coordinator"),
    BUG_HUNTER_LEVEL_1(1 << 3, "Bug Hunter Level 1"),
    HOUSE_BRAVERY(1 << 6, "House Bravery Member"),
    HOUSE_BRILLIANCE(1 << 7, "House Brilliance Member"),
    HOUSE_BALANCE(1 << 8, "House Balance Member"),
    PREMIUM_EARLY_SUPPORTER(1 << 9, "Early Nitro Supporter"),
    TEAM_PSEUDO_USER(1 << 10, "User is a team"),
    BUG_HUNTER_LEVEL_2(1 << 14, "Bug Hunter Level 2"),
    VERIFIED_BOT(1 << 16, "Verified Bot"),
    VERIFIED_DEVELOPER(1 << 17, "Early Verified Bot Developer"),
    CERTIFIED_MODERATOR(1 << 18, "Discord Certified Moderator"),
    BOT_HTTP_INTERACTIONS(1 << 19, "Bot uses only HTTP interactions and is shown in the online member list"),
    ACTIVE_DEVELOPER(1 << 22, "User is an active Developer");

    private final int flag;
    private final String description;

    /**
     * Class constructor.
     *
     * @param flag The bitmask of the flag.
     * @param description The description of the flag.
     */
    UserFlag(int flag, String description) {
        this.flag = flag;
        this.description = description;
    }

    /**
     * Gets the integer value of the flag.
     *
     * @return The integer value of the flag.
     */
    public int asInt() {
        return flag;
    }

    /**
     * Gets the description of the flag.
     *
     * @return The description of the flag.
     */
    public String getDescription() {
        return description;
    }
}

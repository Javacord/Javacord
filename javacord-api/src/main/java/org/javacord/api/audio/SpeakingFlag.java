package org.javacord.api.audio;

public enum SpeakingFlag {

    /**
     * The speaking flag. This flag indicates whether a client is or is not sending audio.
     */
    SPEAKING(1),

    /**
     * The soundshare flag. This flag is used for sending audio while sharing screens. Not useful to bot accounts.
     */
    SOUNDSHARE(2),

    /**
     * The priority speaker flag. This flag is used for sending audio as a priority speaker (Louder than other clients).
     */
    PRIORITY_SPEAKER(4);

    private int flag;

    /**
     * Creates a new SpeakingFlag.
     *
     * @param flag The integer value of the flag.
     */
    SpeakingFlag(int flag) {
        this.flag = flag;
    }

    /**
     * Gets the integer value of the flag.
     *
     * @return The integer value of the flag.
     */
    public int asInt() {
        return flag;
    }
}

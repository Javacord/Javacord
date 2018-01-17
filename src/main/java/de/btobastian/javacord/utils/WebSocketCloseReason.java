package de.btobastian.javacord.utils;

/**
 * A enum with all different channel types.
 */
public enum WebSocketCloseReason {

    DISCONNECT(1000),
    HEARTBEAT_NOT_PROPERLY_ANSWERED(1001, "Heartbeat was not answered properly");

    /**
     * The close code.
     */
    private final int closeCode;

    /**
     * The close reason.
     */
    private final String closeReason;

    /**
     * Creates a new web socket close reason.
     *
     * @param closeCode The close code.
     */
    WebSocketCloseReason(int closeCode) {
        this(closeCode, null);
    }

    /**
     * Creates a new web socket close reason.
     *
     * @param closeCode The close code.
     * @param closeReason The close reason.
     */
    WebSocketCloseReason(int closeCode, String closeReason) {
        this.closeCode = closeCode;
        this.closeReason = closeReason;
    }

    /**
     * Gets the close code.
     *
     * @return The close code.
     */
    public int getCloseCode() {
        return closeCode;
    }

    /**
     * Gets the close reason.
     *
     * @return The close reason.
     */
    public String getCloseReason() {
        return closeReason;
    }

}

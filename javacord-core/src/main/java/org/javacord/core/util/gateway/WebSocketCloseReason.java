package org.javacord.core.util.gateway;

/**
 * An enum with all different web socket close reasons.
 */
public enum WebSocketCloseReason {

    DISCONNECT(WebSocketCloseCode.NORMAL),
    HEARTBEAT_NOT_PROPERLY_ANSWERED(WebSocketCloseCode.UNKNOWN_ERROR, "Heartbeat was not answered properly"),
    INVALID_SESSION_RECONNECT(WebSocketCloseCode.INVALID_SESSION_RECONNECT, "Session is invalid (Received opcode 9)"),
    COMMANDED_RECONNECT(WebSocketCloseCode.COMMANDED_RECONNECT, "Discord commanded a reconnect (Received opcode 7)");

    /**
     * The web socket close code.
     */
    private final WebSocketCloseCode closeCode;

    /**
     * The close reason.
     */
    private final String closeReason;

    /**
     * Creates a new web socket close reason.
     *
     * @param closeCode The web socket close code.
     */
    WebSocketCloseReason(WebSocketCloseCode closeCode) {
        this(closeCode, null);
    }

    /**
     * Creates a new web socket close reason.
     *
     * @param closeCode The web socket close code.
     * @param closeReason The close reason.
     */
    WebSocketCloseReason(WebSocketCloseCode closeCode, String closeReason) {
        this.closeCode = closeCode;
        this.closeReason = closeReason;
    }

    /**
     * Gets the actual numeric close code.
     *
     * @return The actual numeric close code.
     */
    public int getNumericCloseCode() {
        return closeCode.getCode();
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

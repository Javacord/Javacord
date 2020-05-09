package org.javacord.core.util.gateway;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * An enum with all gateway opcode as defined by
 * <a href="https://discord.com/developers/docs/topics/opcodes-and-status-codes#gateway-gateway-opcodes">Discord</a>.
 */
public enum GatewayOpcode {

    /**
     * Dispatches an event.
     */
    DISPATCH(0),

    /**
     * Used for ping checking.
     */
    HEARTBEAT(1),

    /**
     * Used for client handshake.
     */
    IDENTIFY(2),

    /**
     * Used to update the client status.
     */
    STATUS_UPDATE(3),

    /**
     * Used to join/move/leave voice channels.
     */
    VOICE_STATE_UPDATE(4),

    /**
     * Used for voice ping checking.
     */
    VOICE_SERVER_PING(5),

    /**
     * Used to resume a closed connection.
     */
    RESUME(6),

    /**
     * Used to tell clients to reconnect to the gateway.
     */
    RECONNECT(7),

    /**
     * Used to request guild members.
     */
    REQUEST_GUILD_MEMBERS(8),

    /**
     * Used to notify client they have an invalid session id.
     */
    INVALID_SESSION(9),

    /**
     * Sent immediately after connecting, contains heartbeat and server debug information.
     */
    HELLO(10),

    /**
     * Sent immediately following a client heartbeat that was received.
     */
    HEARTBEAT_ACK(11);

    /**
     * A map for retrieving the enum instances by code.
     */
    private static final Map<Integer, GatewayOpcode> instanceByCode;

    /**
     * The actual numeric code.
     */
    private final int code;

    static {
        instanceByCode = Collections.unmodifiableMap(
                Arrays.stream(values())
                        .collect(Collectors.toMap(GatewayOpcode::getCode, Function.identity())));
    }

    /**
     * Creates a new gateway opcode.
     *
     * @param code The actual numeric code.
     */
    GatewayOpcode(int code) {
        this.code = code;
    }

    /**
     * Gets the gateway opcode by actual numeric code.
     *
     * @param code The actual numeric code.
     * @return The gateway opcode with the actual numeric code.
     */
    public static Optional<GatewayOpcode> fromCode(int code) {
        return Optional.ofNullable(instanceByCode.get(code));
    }

    /**
     * Gets the actual numeric code.
     *
     * @return The actual numeric code.
     */
    public int getCode() {
        return code;
    }

}

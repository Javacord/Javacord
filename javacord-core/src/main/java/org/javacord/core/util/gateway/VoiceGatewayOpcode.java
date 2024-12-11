package org.javacord.core.util.gateway;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * An enum with all voice gateway opcode as defined by
 * <a href="https://discord.com/developers/docs/topics/opcodes-and-status-codes#voice-voice-opcodes">Discord</a>.
 */
public enum VoiceGatewayOpcode {

    /**
     * begin a voice websocket connection.
     *
     * <p>Sent by client
     */
    IDENTIFY(0),

    /**
     * select the voice protocol.
     *
     * <p>Sent by client
     */
    SELECT_PROTOCOL(1),

    /**
     * complete the websocket handshake.
     *
     * <p>Sent by server
     */
    READY(2),

    /**
     * keep the websocket connection alive.
     *
     * <p>Sent by client
     */
    HEARTBEAT(3),

    /**
     * describe the session.
     *
     * <p>Sent by server
     */
    SESSION_DESCRIPTION(4),

    /**
     * indicate which users are speaking.
     *
     * <p>Sent by client and server
     */
    SPEAKING(5),

    /**
     * sent immediately following a received client heartbeat.
     *
     * <p>Sent by server
     */
    HEARTBEAT_ACK(6),

    /**
     * resume a connection.
     *
     * <p>Sent by client
     */
    RESUME(7),

    /**
     * the continuous interval in milliseconds after which the client should send a heartbeat.
     *
     * <p>Sent by server
     */
    HELLO(8),

    /**
     * acknowledge Resume.
     *
     * <p>Sent by server
     */
    RESUMED(9),

    /**
     * a client connected to the voice channel.
     *
     * <p>Sent by server
     */
    CLIENT_CONNECT(11),

    /**
     * a client has disconnected from the voice channel.
     *
     * <p>Sent by server
     */
    CLIENT_DISCONNECT(13),

    /**
     * downgrade from the DAVE protocol is coming.
     *
     * <p>Sent by server
     */
    DAVE_PREPARE_TRANSITION(21),

    /**
     * execute the previously announced transition.
     *
     * <p>Sent by server
     */
    DAVE_EXECUTE_TRANSITION(22),

    /**
     * acknowledge readiness for previous transition.
     *
     * <p>Sent by client
     */
    DAVE_TRANSITION_READY(23),

    /**
     * DAVE protocol version or group change is coming.
     *
     * <p>Sent by server
     */
    DAVE_PREPARE_EPOCH(24),

    /**
     * MLS external sender credentials and public key.
     *
     * <p>Binary
     *
     * <p>Sent by server
     */
    DAVE_MLS_EXTERNAL_SENDER(25),

    /**
     * key package for member pending add to MLS.
     *
     * <p>Binary
     *
     * <p>Sent by client
     */
    DAVE_MLS_KEY_PACKAGE(26),

    /**
     * MLS proposal to be added or removed.
     *
     * <p>Binary
     *
     * <p>Sent by server
     */
    DAVE_MLS_PROPOSALS(27),

    /**
     * MLS commit with optional message.
     *
     * <p>Binary
     *
     * <p>Sent by client
     */
    DAVE_MLS_COMMIT_WELCOME(28),

    /**
     * MLS commit for upcoming transition.
     *
     * <p>Sent by server
     */
    DAVE_MLS_COMMIT_TRANSITION(29),

    /**
     * MLS welcome to group for upcoming transition.
     *
     * <p>Binary
     *
     * <p>Sent by server
     */
    DAVE_MLS_WELCOME(30),

    /**
     * invalid commit or welcome, request re-add.
     *
     * <p>Sent by client
     */
    DAVE_MLS_INVALID_COMMIT(31);

    /**
     * A map for retrieving the enum instances by code.
     */
    private static final Map<Integer, VoiceGatewayOpcode> instanceByCode;

    /**
     * The actual numeric code.
     */
    private final int code;

    static {
        instanceByCode = Collections.unmodifiableMap(
                Arrays.stream(values())
                        .collect(Collectors.toMap(VoiceGatewayOpcode::getCode, Function.identity())));
    }

    /**
     * Creates a new voice gateway opcode.
     *
     * @param code The actual numeric code.
     */
    VoiceGatewayOpcode(int code) {
        this.code = code;
    }

    /**
     * Gets the voice gateway opcode by actual numeric code.
     *
     * @param code The actual numeric code.
     * @return The voice gateway opcode with the actual numeric code.
     */
    public static Optional<VoiceGatewayOpcode> fromCode(int code) {
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

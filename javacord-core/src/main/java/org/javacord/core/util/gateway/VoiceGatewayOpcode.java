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
     */
    IDENTIFY(0),

    /**
     * select the voice protocol.
     */
    SELECT_PROTOCOL(1),

    /**
     * complete the websocket handshake.
     */
    READY(2),

    /**
     * keep the websocket connection alive.
     */
    HEARTBEAT(3),

    /**
     * describe the session.
     */
    SESSION_DESCRIPTION(4),

    /**
     * indicate which users are speaking.
     */
    SPEAKING(5),

    /**
     * sent immediately following a received client heartbeat.
     */
    HEARTBEAT_ACK(6),

    /**
     * resume a connection.
     */
    RESUME(7),

    /**
     * the continuous interval in milliseconds after which the client should send a heartbeat.
     */
    HELLO(8),

    /**
     * acknowledge Resume.
     */
    RESUMED(9),

    /**
     * a client connected to the voice channel.
     */
    CLIENT_CONNECT(12),

    /**
     * a client has disconnected from the voice channel.
     */
    CLIENT_DISCONNECT(13);

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

package org.javacord.core.util.gateway;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * An enum with all web socket close reasons as
 * defined by <a href="https://tools.ietf.org/html/rfc6455#section-7.4">RFC 6455</a> (1000-2999),
 * assigned by <a href="https://www.iana.org/assignments/websocket/websocket.xml">IANA</a> (3000-3999),
 * assigned by <a href="https://discord.com/developers/docs/topics/gateway#disconnections">Discord</a> (4000-4998) or
 * self-assigned (4999).
 */
public enum WebSocketCloseCode {

    /**
     * 1000.
     * 
     * <i>* 1000 indicates a normal closure, meaning that the purpose for
     * which the connection was established has been fulfilled.</i>
     */
    NORMAL(com.neovisionaries.ws.client.WebSocketCloseCode.NORMAL, Usage.BOTH),

    /**
     * 1001.
     * 
     * <i>1001 indicates that an endpoint is "going away", such as a server
     * going down or a browser having navigated away from a page.</i>
     */
    AWAY(com.neovisionaries.ws.client.WebSocketCloseCode.AWAY, Usage.BOTH),

    /**
     * 1002.
     * 
     * <i>1002 indicates that an endpoint is terminating the connection due
     * to a protocol error.</i>
     */
    UNCONFORMED(com.neovisionaries.ws.client.WebSocketCloseCode.UNCONFORMED, Usage.BOTH),

    /**
     * 1003.
     * 
     * <i>1003 indicates that an endpoint is terminating the connection
     * because it has received a type of data it cannot accept
     * (e&#46;g&#46;, an endpoint that understands only text data MAY
     * send this if it receives a binary message).</i>
     */
    UNACCEPTABLE(com.neovisionaries.ws.client.WebSocketCloseCode.UNACCEPTABLE, Usage.BOTH),

    /**
     * 1005.
     * 
     * <i>1005 is a reserved value and MUST NOT be set as a status code in a
     * Close control frame by an endpoint&#46;  It is designated for use in
     * applications expecting a status code to indicate that no status
     * code was actually present.</i>
     */
    NONE(com.neovisionaries.ws.client.WebSocketCloseCode.NONE, Usage.BOTH),

    /**
     * 1006.
     * 
     * <i>1006 is a reserved value and MUST NOT be set as a status code in a
     * Close control frame by an endpoint&#46;  It is designated for use in
     * applications expecting a status code to indicate that the
     * connection was closed abnormally, e&#46;g&#46;, without sending or
     * receiving a Close control frame.</i>
     */
    ABNORMAL(com.neovisionaries.ws.client.WebSocketCloseCode.ABNORMAL, Usage.BOTH),

    /**
     * 1007.
     * 
     * <i>1007 indicates that an endpoint is terminating the connection
     * because it has received data within a message that was not
     * consistent with the type of the message (e&#46;g&#46;, non-UTF-8
     * [<a href="http://tools.ietf.org/html/rfc3629">RFC3629</a>] data
     * within a text message).</i>
     */
    INCONSISTENT(com.neovisionaries.ws.client.WebSocketCloseCode.INCONSISTENT, Usage.BOTH),

    /**
     * 1008.
     * 
     * <i>1008 indicates that an endpoint is terminating the connection
     * because it has received a message that violates its policy&#46;
     * This is a generic status code that can be returned when there
     * is no other more suitable status code (e&#46;g&#46;, 1003 or 1009)
     * or if there is a need to hide specific details about the policy.</i>
     */
    VIOLATED(com.neovisionaries.ws.client.WebSocketCloseCode.VIOLATED, Usage.BOTH),

    /**
     * 1009.
     * 
     * <i>1009 indicates that an endpoint is terminating the connection
     * because it has received a message that is too big for it to
     * process.</i>
     */
    OVERSIZE(com.neovisionaries.ws.client.WebSocketCloseCode.OVERSIZE, Usage.BOTH),

    /**
     * 1010.
     * 
     * <i>1010 indicates that an endpoint (client) is terminating the
     * connection because it has expected the server to negotiate
     * one or more extension, but the server didn't return them in
     * the response message of the WebSocket handshake&#46;  The
     * list of extensions that are needed SHOULD appear in the
     * /reason/ part of the Close frame&#46; Note that this status
     * code is not used by the server, because it can fail the
     * WebSocket handshake instead.</i>
     */
    UNEXTENDED(com.neovisionaries.ws.client.WebSocketCloseCode.UNEXTENDED, Usage.BOTH),

    /**
     * 1011.
     * 
     * <i>1011 indicates that a server is terminating the connection because
     * it encountered an unexpected condition that prevented it from
     * fulfilling the request.</i>
     */
    UNEXPECTED(com.neovisionaries.ws.client.WebSocketCloseCode.UNEXPECTED, Usage.BOTH),

    /**
     * 1015.
     * 
     * <i>1015 is a reserved value and MUST NOT be set as a status code in a
     * Close control frame by an endpoint&#46;  It is designated for use in
     * applications expecting a status code to indicate that the
     * connection was closed due to a failure to perform a TLS handshake
     * (e&#46;g&#46;, the server certificate can't be verified).</i>
     */
    INSECURE(com.neovisionaries.ws.client.WebSocketCloseCode.INSECURE, Usage.BOTH),

    /**
     * We're not sure what went wrong. Try reconnecting?
     */
    UNKNOWN_ERROR(4000, Usage.NORMAL),

    /**
     * You sent an invalid
     * <a href="https://discord.com/developers/docs/topics/gateway#payloads-and-opcodesspec.html">Gateway opcode</a>
     * or an invalid payload for an opcode. Don't do that!
     */
    UNKNOWN_OPCODE(4001, Usage.BOTH),

    /**
     * You sent an invalid <a href="https://discord.com/developers/docs/topics/gateway#sending-payloads">payload</a>
     * to us. Don't do that!
     */
    DECODE_ERROR(4002, Usage.NORMAL),

    /**
     * You sent us a payload prior to <a href="https://discord.com/developers/docs/topics/gateway#identify">
     * identifying</a>.
     */
    NOT_AUTHENTICATED(4003, Usage.BOTH),

    /**
     * The account token sent with your <a href="https://discord.com/developers/docs/topics/gateway#identify">
     * identify payload</a> is incorrect.
     */
    AUTHENTICATION_FAILED(4004, Usage.BOTH),

    /**
     * You sent more than one identify payload. Don't do that!
     */
    ALREADY_AUTHENTICATED(4005, Usage.NORMAL),

    /**
     * Your session is no longer valid.
     */
    SESSION_NO_LONGER_VALID(4006, Usage.VOICE),

    /**
     * The sequence sent when <a href="https://discord.com/developers/docs/topics/gateway#resume">resuming</a>
     * the session was invalid. Reconnect and start a new session.
     */
    INVALID_SEQ(4007, Usage.NORMAL),

    /**
     * Woah nelly! You're sending payloads to us too quickly. Slow it down!
     */
    RATE_LIMITED(4008, Usage.NORMAL),

    /**
     * Your session timed out. Reconnect and start a new one.
     */
    SESSION_TIMEOUT(4009, Usage.BOTH),

    /**
     * You sent us an invalid <a href="https://discord.com/developers/docs/topics/gateway#sharding">shard when
     * identifying</a>.
     */
    INVALID_SHARD(4010, Usage.NORMAL),

    /**
     * The session would have handled too many guilds - you are required to
     * <a href="https://discord.com/developers/docs/topics/gateway#sharding">shard</a>
     * your connection in order to connect.
     */
    SHARDING_REQUIRED(4011, Usage.NORMAL),

    /**
     * The session would have handled too many guilds - you are required to
     * <a href="https://discord.com/developers/docs/topics/gateway#sharding">shard</a>
     * your connection in order to connect.
     */
    SERVER_NOT_FOUND(4011, Usage.VOICE),

    /**
     * The session would have handled too many guilds - you are required to
     * <a href="https://discord.com/developers/docs/topics/gateway#sharding">shard</a>
     * your connection in order to connect.
     */
    UNKNOWN_PROTOCOL(4012, Usage.VOICE),

    /**
     * The session would have handled too many guilds - you are required to
     * <a href="https://discord.com/developers/docs/topics/gateway#sharding">shard</a>
     * your connection in order to connect.
     */
    DISCONNECTED(4014, Usage.VOICE),

    /**
     * The session would have handled too many guilds - you are required to
     * <a href="https://discord.com/developers/docs/topics/gateway#sharding">shard</a>
     * your connection in order to connect.
     */
    VOICE_SERVER_CRASHED(4015, Usage.VOICE),

    /**
     * The session would have handled too many guilds - you are required to
     * <a href="https://discord.com/developers/docs/topics/gateway#sharding">shard</a>
     * your connection in order to connect.
     */
    UNKNOWN_ENCRYPTION_MODE(4016, Usage.VOICE),

    /**
     * Discord asked for a reconnect, and there is no pre-defined matching close reason,
     * thus 4999 is used which is unlikely to get assigned by Discord.
     */
    COMMANDED_RECONNECT(4999, Usage.NORMAL);

    /**
     * A map for retrieving the enum instances by code.
     */
    private static final Map<Integer, WebSocketCloseCode> instanceByCode;

    /**
     * A map for retrieving the voice enum instances by code.
     */
    private static final Map<Integer, WebSocketCloseCode> voiceInstanceByCode;

    /**
     * The actual numeric close code.
     */
    private final int code;

    /**
     * The actual numeric close code.
     */
    private final Usage usage;

    static {
        instanceByCode = Collections.unmodifiableMap(
                Arrays.stream(values())
                        .filter(closeCode -> (closeCode.usage == Usage.BOTH) || (closeCode.usage == Usage.NORMAL))
                        .collect(Collectors.toMap(WebSocketCloseCode::getCode, Function.identity())));

        voiceInstanceByCode = Collections.unmodifiableMap(
                Arrays.stream(values())
                        .filter(closeCode -> (closeCode.usage == Usage.BOTH) || (closeCode.usage == Usage.VOICE))
                        .collect(Collectors.toMap(WebSocketCloseCode::getCode, Function.identity())));
    }

    /**
     * Creates a new web socket close code.
     *
     * @param code The actual numeric close code.
     */
    WebSocketCloseCode(int code, Usage usage) {
        this.code = code;
        this.usage = usage;
    }

    /**
     * Gets the web socket close code by actual numeric close code.
     *
     * @param code The actual numeric close code.
     * @return The web socket close code with the actual numeric close code.
     */
    public static Optional<WebSocketCloseCode> fromCode(int code) {
        return Optional.ofNullable(instanceByCode.get(code));
    }

    /**
     * Gets the voice web socket close code by actual numeric close code.
     *
     * @param code The actual numeric close code.
     * @return The voice web socket close code with the actual numeric close code.
     */
    public static Optional<WebSocketCloseCode> fromCodeForVoice(int code) {
        return Optional.ofNullable(voiceInstanceByCode.get(code));
    }

    /**
     * Gets the actual numeric close code.
     *
     * @return The actual numeric close code.
     */
    public int getCode() {
        return code;
    }

    /**
     * Defines what a close code is used for, normal web socket connections, voice web socket connections or both.
     */
    private enum Usage {
        NORMAL, VOICE, BOTH
    }

}

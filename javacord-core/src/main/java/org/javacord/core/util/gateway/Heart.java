package org.javacord.core.util.gateway;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFrame;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.core.util.logging.LoggerUtil;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Every animal has a heart.
 * As we all know that websockets are animals, they obliviously have a heart, too.
 */
public class Heart {

    /**
     * The logger of this class.
     */
    private static final Logger stethoscope = LoggerUtil.getLogger(Heart.class);

    private final DiscordApi api;
    private final AtomicReference<WebSocket> websocket;
    private final boolean voice;

    private final AtomicReference<Future<?>> heartbeatTimer = new AtomicReference<>();
    private final AtomicBoolean heartbeatAckReceived = new AtomicBoolean();
    private int lastSeq = 0;

    /**
     * Ba boom, ba boom, ba boom, ba boom, ...
     *
     * @param api The heart of every Javacord bot.
     * @param websocket The animal.
     * @param voice Voice websocket hearts beat differently.
     */
    public Heart(DiscordApi api, AtomicReference<WebSocket> websocket, boolean voice) {
        this.api = api;
        this.websocket = websocket;
        this.voice = voice;
    }

    /**
     * Handles the given packet.
     * Usually used to update the last sequence number and listen for acks.
     *
     * @param packet The packet to handle.
     */
    public void handlePacket(JsonNode packet) {
        if (!voice) {
            // For normal websockets, the last sequence number is sent in the heartbeat
            if (packet.has("s") && !packet.get("s").isNull()) {
                lastSeq = packet.get("s").asInt();
            }
        }
        int heartbeatAckOp = voice ? VoiceGatewayOpcode.HEARTBEAT_ACK.getCode() : GatewayOpcode.HEARTBEAT_ACK.getCode();
        if (packet.get("op").asInt() == heartbeatAckOp) {
            stethoscope.debug("Heartbeat ACK received (voice: {}, packet: {})", voice, packet);
            heartbeatAckReceived.set(true);
        }
    }

    /**
     * Ba boom, ba boom, ba boom, ba boom, ..
     * .
     * @param interval Unlike a human heart, websocket hearts have a fixed beating interval.
     */
    public void startBeating(int interval) {
        // first heartbeat should assume last heartbeat was answered properly
        heartbeatAckReceived.set(true);
        heartbeatTimer.updateAndGet(future -> {
            if (future != null) {
                // If there was an old heart beating, crush it
                future.cancel(false);
            }
            return api.getThreadPool().getScheduler().scheduleWithFixedDelay(() -> {
                try {
                    if (heartbeatAckReceived.getAndSet(false)) {
                        beat();
                    } else {
                        websocket.get().sendClose(
                                WebSocketCloseReason.HEARTBEAT_NOT_PROPERLY_ANSWERED.getNumericCloseCode(),
                                WebSocketCloseReason.HEARTBEAT_NOT_PROPERLY_ANSWERED.getCloseReason());
                    }
                } catch (Throwable t) {
                    // R.I.P.
                    stethoscope.error("Failed to send heartbeat or close web socket!", t);
                }
            }, 0, interval, TimeUnit.MILLISECONDS);
        });
    }

    /**
     * Ba boom, ba boom, ba boom, ba boom, ...
     */
    public void beat() {
        ObjectNode heartbeatPacket = JsonNodeFactory.instance.objectNode()
                .put("op", voice ? VoiceGatewayOpcode.HEARTBEAT.getCode() : GatewayOpcode.HEARTBEAT.getCode())
                .put("d", voice ? (int) (Math.random() * Integer.MAX_VALUE) : lastSeq);
        WebSocketFrame heartbeatFrame = WebSocketFrame.createTextFrame(heartbeatPacket.toString());
        websocket.get().sendFrame(heartbeatFrame);
        // Ba boom, ba boom, ba boom, ba boom, ...
        stethoscope.debug("Sent heartbeat (voice: {}, packet: {})", voice, heartbeatPacket);
    }

    /**
     * Squashes the poor heart (stop it from beating).
     */
    public void squash() {
        heartbeatTimer.updateAndGet(future -> {
            if (future != null) {
                future.cancel(false);
            }
            return null;
        });
    }

}
